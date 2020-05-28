package oms.pc_protector.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import oms.pc_protector.restApi.login.model.LoginVO;
import oms.pc_protector.restApi.manager.model.ManagerVO;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String a = request.getRequestURI();
        // Grab credentials and map then to LoginViewModel
        LoginVO credentials = null;
        HttpServletRequestWrapper temp = new HttpServletRequestWrapper(request);

        try {
            credentials = new ObjectMapper().readValue(temp.getInputStream(), LoginVO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create login token
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                credentials.getId(),
                credentials.getPassword(),
                new ArrayList<>()
        );
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info(format.format(new Date(System.currentTimeMillis())));
        log.info(format.format(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_TIME)));
        // Authenticate user
        try {
            Authentication auth = authenticationManager.authenticate(authenticationToken);
            System.out.println("auth : " + auth);
            ManagerPrincipal managerPrincipal = (ManagerPrincipal) auth.getPrincipal();
            ManagerVO manager_info = managerPrincipal.getManagerVO();
            if(manager_info.isLocked()) {
                response.sendError(HttpStatus.NOT_ACCEPTABLE.value(), "계정 잠금");
            }
            return auth;
        } catch(BadCredentialsException ex) {
            response.sendError(HttpStatus.NOT_ACCEPTABLE.value(), "비밀번호가 틀렸습니다.");
        }
        return null;
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // Grab principal
        ManagerPrincipal principal = (ManagerPrincipal) authResult.getPrincipal();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // Create JWT Token
        log.info(format.format(new Date(System.currentTimeMillis())));
        log.info(format.format(new Date(System.currentTimeMillis()+JwtProperties.ACCESS_TIME)));
        String encodedPassword = new BCryptPasswordEncoder().encode("dmFWh++LdJf6eBKb/uhDwFfBybghv3ajctRl8EDNGUE");
        log.info("encode : "  + encodedPassword);
        String token = JWT.create()
                .withSubject(principal.getUsername())
                .withClaim("role", "MANAGER")
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_TIME))
                .withAudience(String.valueOf(passwordEncoder.matches("dmFWh++LdJf6eBKb/uhDwFfBybghv3ajctRl8EDNGUE",principal.getPassword())))
                .sign(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()));

        // Add token in response
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + token);
    }
}
