package oms.pc_protector.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import oms.pc_protector.restApi.manager.mapper.ManagerMapper;
import oms.pc_protector.restApi.manager.model.ManagerVO;
import oms.pc_protector.restApi.manager.model.ResponseManagerVO;
import oms.pc_protector.restApi.manager.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.bind.annotation.*;

import javax.servlet.FilterChain;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@CrossOrigin
@Log4j2
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private ManagerService managerService;


    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, ManagerService managerService) {
        super(authenticationManager);
        this.managerService = managerService;
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
                String userId = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()))
                        .build()
                        .verify(token.replace(JwtProperties.TOKEN_PREFIX, ""))
                        .getSubject();
                if (userId != null) {
                    ManagerVO manager = managerService.findById(userId);
                    ManagerPrincipal principal = new ManagerPrincipal(manager);
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userId, null, principal.getAuthorities());
                    authentication = auth;
                }

                // Search in the DB if we find the user by token subject (username)
                // If so, then grab user details and create spring auth token using username, pass, authorities/roles
            }
        } catch (TokenExpiredException ex) {
//            response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, "토큰 만료!");
        }


        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Continue filter execution
        chain.doFilter(request, response);
    }

}
