package oms.pc_protector.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import oms.pc_protector.restApi.client.model.ClientVO;
import oms.pc_protector.restApi.login.mapper.LoginMapper;
import oms.pc_protector.restApi.login.model.ClientLoginVO;
import oms.pc_protector.restApi.manager.model.ManagerVO;
import oms.pc_protector.restApi.manager.service.ManagerService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@CrossOrigin
@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private ManagerService managerService;
    private LoginMapper loginMapper;


    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, ManagerService managerService, LoginMapper loginMapper) {
        super(authenticationManager);
        this.managerService = managerService;
        this.loginMapper = loginMapper;
    }

    // endpoint every request hit with authorization
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Read the Authorization header, where the JWT Token should be
        String header = request.getHeader(JwtProperties.HEADER_STRING);

        // If header does not contain BEARER or is null delegate to Spring impl and exit
        if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
            // rest of the spring pipeline
            chain.doFilter(request, response);
            return;
        }

        // If header is present, try grab user principal from database and perform authorization
        Authentication authentication = null;
        try {
            String token = request.getHeader(JwtProperties.HEADER_STRING);
            if (token != null) {
                // parse the token and validate it (decode)
                DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()))
                        .build()
                        .verify(token.replace(JwtProperties.TOKEN_PREFIX, ""));
                String userId = decodedJWT.getSubject();
                String role = decodedJWT.getClaim("role").asString();
                if (userId != null) {
                    if(role.equals("MANAGER")) {
                        ManagerVO manager = managerService.findById(userId);
                        ManagerPrincipal principal = new ManagerPrincipal(manager);
                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userId, null, principal.getAuthorities());
                        authentication = auth;
                    }
                    else if(role.equals("CLIENT")) {
                        String ipAddress = decodedJWT.getAudience().get(0);
                        ClientVO client = loginMapper.findClient(new ClientLoginVO(userId, ipAddress, request.getHeader("departmentIdx")));
                        ClientPrincipal principal = new ClientPrincipal(client);
                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userId, ipAddress, principal.getAuthorities());
                        authentication = auth;
                    }
                }

                // Search in the DB if we find the user by token subject (username)
                // If so, then grab user details and create spring auth token using username, pass, authorities/roles
            }
        } catch (TokenExpiredException ex) {
            log.info("토큰 만료입니다!!!!!!!");
            response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, "토큰 만료!");
            return;
        } catch (Exception e) {
            log.error(String.valueOf(e));
        } catch (Throwable throwable) {
            log.error(String.valueOf(throwable));
            throwable.printStackTrace();
        }


        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Continue filter execution
        chain.doFilter(request, response);
    }

}
