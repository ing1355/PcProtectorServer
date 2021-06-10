package oms.pc_protector.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import oms.pc_protector.restApi.log.model.LogVO;
import oms.pc_protector.restApi.log.service.LogService;
import oms.pc_protector.restApi.login.mapper.LoginMapper;
import oms.pc_protector.restApi.login.model.TokenLoginVO;
import oms.pc_protector.restApi.manager.model.ManagerVO;
import oms.pc_protector.restApi.manager.service.ManagerService;
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
import java.util.ArrayList;
import java.util.Date;

@CrossOrigin
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final ManagerService managerService;
    private final LoginMapper loginMapper;
    private final LogService logService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   ManagerService managerService,
                                   LoginMapper loginMapper,
                                   LogService logService) {
        this.authenticationManager = authenticationManager;
        this.managerService = managerService;
        this.loginMapper = loginMapper;
        this.logService = logService;
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
            log.error(String.valueOf(e));
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
                    log.error("계정 잠금");
                    response.sendError(HttpStatus.NOT_ACCEPTABLE.value(), "계정 잠금");
                    return null;
                }
            }
            return auth;
        } catch (BadCredentialsException ex) {
            ManagerVO managerVO = this.managerService.findById(credentials.getId());
            if (managerVO.getLocked() > 4) {
                log.error("계정 잠금");
                response.sendError(HttpStatus.NOT_ACCEPTABLE.value(), "계정 잠금");
            } else {
                log.error("비밀번호를 " + (managerVO.getLocked() + 1) + "회 틀렸습니다. 5회 틀릴 시 계정이 잠금됩니다.");
                response.sendError(HttpStatus.NOT_ACCEPTABLE.value(), "비밀번호가 틀렸습니다. 5회 틀릴 시 계정이 잠금됩니다.");
            }
        } catch (InternalAuthenticationServiceException ia) {
            log.error("아이디가 존재하지 않습니다.");
            ia.printStackTrace();
            response.sendError(HttpStatus.NOT_ACCEPTABLE.value(), "아이디가 존재하지 않습니다.");
        }
        return null;
    }

    private boolean excludeURI(HttpServletRequest request) {
        String uri = request.getRequestURI().toString().replaceAll("v1/", "");
        return uri.startsWith("/client/");
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // Grab principal
        String token = null;
        String refresh_token = null;
        if (authResult.getPrincipal().getClass() == ManagerPrincipal.class) {
            ManagerPrincipal principal = (ManagerPrincipal) authResult.getPrincipal();
            this.managerService.initManagerLock(principal.getUsername());
            String ompassUrl = "";
             if(principal.getManagerVO().getOmpass().equals("Y")){
                ompassUrl = "https://www.ompass.kr:8383/authenticate/did/30?type=u2f"+"&access_key=test&domain=192.168.182.32:3000&redirect_uri=192.168.182.32:3000/fido-login&username="+principal.getUsername();
            }
            token = JWT.create()
                    .withSubject(principal.getUsername())
                    .withClaim("ompass",ompassUrl)
                    .withClaim("role", "MANAGER")
                    .withClaim("idx", principal.getManagerVO().getDepartmentIdx())
                    .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_TIME))
                    .withAudience(String.valueOf(passwordEncoder.matches("dmFWh++LdJf6eBKb/uhDwFfBybghv3ajctRl8EDNGUE", principal.getPassword())))
                    .sign(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()));
            refresh_token = JWT.create()
                    .withSubject(principal.getUsername())
                    .withClaim("role", "MANAGER")
                    .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.REFRESH_TIME))
                    .sign(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()));
            LogVO logVO = new LogVO();
            logVO.setUri(request.getRequestURI());
            logVO.setMethod(request.getMethod());
            logVO.setIpAddress(request.getRemoteAddr());
            if (!(logVO.getUri().contains("chunk")) && !(logVO.getUri().contains(".svg"))
                    && !(logVO.getUri().contains(".json")) && !(logVO.getUri().contains(".png"))
                    && !(logVO.getUri().contains(".woff")) && !(logVO.getUri().contains(".ttf"))) {
                boolean hasClientURI = excludeURI(request);
                if (!hasClientURI) logVO.setManagerId(principal.getUsername());
                logVO.setDepartmentIdx(principal.getManagerVO().getDepartmentIdx());
                logService.register(logVO);
            }
        } else {
            ClientPrincipal principal = (ClientPrincipal) authResult.getPrincipal();
            token = JWT.create()
                    .withSubject(principal.getUsername())
                    .withClaim("role", "CLIENT")
                    .withAudience(principal.getPassword())
                    .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.CLIENT_ACCESS_TIME))
                    .sign(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()));
        }
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // Create JWT Token

        // Add token in response
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + token);
        response.addHeader(JwtProperties.REFRESH_STRING, JwtProperties.TOKEN_PREFIX + refresh_token);
    }
}
