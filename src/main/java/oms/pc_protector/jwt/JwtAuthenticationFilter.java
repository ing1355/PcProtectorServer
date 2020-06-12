package oms.pc_protector.jwt;

import ch.qos.logback.core.net.server.Client;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import oms.pc_protector.restApi.login.mapper.LoginMapper;
import oms.pc_protector.restApi.login.model.LoginVO;
import oms.pc_protector.restApi.login.model.TokenLoginVO;
import oms.pc_protector.restApi.manager.mapper.ManagerMapper;
import oms.pc_protector.restApi.manager.model.ManagerVO;
import oms.pc_protector.restApi.manager.service.ManagerService;
import org.apache.catalina.Manager;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@CrossOrigin
@Log4j2
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final ManagerService managerService;
    private final LoginMapper loginMapper;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, ManagerService managerService, LoginMapper loginMapper) {
        this.authenticationManager = authenticationManager;
        this.managerService = managerService;
        this.loginMapper = loginMapper;
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String a = request.getRequestURI();
        // Grab credentials and map then to LoginViewModel
        TokenLoginVO credentials = null;
        HttpServletRequestWrapper temp = new HttpServletRequestWrapper(request);

        try {
            credentials = new ObjectMapper().readValue(temp.getInputStream(), TokenLoginVO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        UsernamePasswordAuthenticationToken authenticationToken = null;
        // Create login token
        if (credentials.getPassword() == null || credentials.getPassword().length() == 0) {
            authenticationToken = new UsernamePasswordAuthenticationToken(
                    credentials.getId(),
                    credentials.getIpAddress(),
                    new ArrayList<>()
            );
        } else {
            authenticationToken = new UsernamePasswordAuthenticationToken(
                    credentials.getId(),
                    credentials.getPassword(),
                    new ArrayList<>()
            );
        }
        // Authenticate user
        try {
            Authentication auth = authenticationManager.authenticate(authenticationToken);
            if (credentials.getPassword() == null) {
                ClientPrincipal clientPrincipal = (ClientPrincipal) auth.getPrincipal();
            } else {
                ManagerPrincipal managerPrincipal = (ManagerPrincipal) auth.getPrincipal();
                ManagerVO manager_info = managerPrincipal.getManagerVO();
                if (manager_info.getLocked() > 4) {
                    response.sendError(HttpStatus.NOT_ACCEPTABLE.value(), "계정 잠금");
                    return null;
                }
            }
            return auth;
        } catch (BadCredentialsException ex) {
            ManagerVO managerVO = this.managerService.findById(credentials.getId());
            if (managerVO.getLocked() > 4) {
                response.sendError(HttpStatus.NOT_ACCEPTABLE.value(), "계정 잠금");
            } else {
                if(managerVO.getLocked() > 1) {
                    response.sendError(HttpStatus.NOT_ACCEPTABLE.value(),"비밀번호를 " + managerVO.getLocked() + "회 틀렸습니다. 5회 틀릴 시 계정이 잠금됩니다.");
                } else {
                    response.sendError(HttpStatus.NOT_ACCEPTABLE.value(), "비밀번호가 틀렸습니다.");
                }
            }
        } catch (InternalAuthenticationServiceException ia) {
            ia.printStackTrace();
            response.sendError(HttpStatus.NOT_ACCEPTABLE.value(), "아이디가 존재하지 않습니다.");
        }
        return null;
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // Grab principal
        String token = null;
        if (authResult.getPrincipal().getClass() == ManagerPrincipal.class) {
            ManagerPrincipal principal = (ManagerPrincipal) authResult.getPrincipal();
            this.managerService.initManagerLock(principal.getUsername());
            token = JWT.create()
                    .withSubject(principal.getUsername())
                    .withClaim("role", "MANAGER")
                    .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_TIME))
                    .withAudience(String.valueOf(passwordEncoder.matches("dmFWh++LdJf6eBKb/uhDwFfBybghv3ajctRl8EDNGUE", principal.getPassword())))
                    .sign(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()));
        } else {
            ClientPrincipal principal = (ClientPrincipal) authResult.getPrincipal();
            token = JWT.create()
                    .withSubject(principal.getUsername())
                    .withClaim("role", "CLIENT")
                    .withAudience(principal.getPassword())
                    .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_TIME))
                    .sign(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()));
        }
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // Create JWT Token

        // Add token in response
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + token);
    }
}
