package oms.pc_protector.restApi.login.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.log4j.Log4j2;
import oms.pc_protector.apiConfig.model.SingleResult;
import oms.pc_protector.apiConfig.service.ResponseService;
import oms.pc_protector.jwt.JwtProperties;
import oms.pc_protector.restApi.login.service.LoginService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Log4j2
@RestController
@RequestMapping(value = "/v1/login")
public class LoginController {

    private ResponseService responseService;
    private LoginService loginService;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public LoginController(ResponseService responseService,
                           LoginService loginService){
        this.responseService = responseService;
        this.loginService = loginService;
    }

    @GetMapping(value = "confirm")
    public SingleResult<?> confirm(@RequestParam(value = "id") String id,
                                   @RequestParam(value = "password") String password) {
        String db_password = loginService.findPasswordById(id);
        boolean result = passwordEncoder.matches(password,db_password);
        return responseService.getSingleResult(result);
    }

    @GetMapping(value = "refresh")
    public SingleResult<?> refresh(HttpServletResponse response,
                        HttpServletRequest request) throws IOException {
        String header = request.getHeader(JwtProperties.HEADER_STRING);
        String new_token;
        log.info(header);
        if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
            // rest of the spring pipeline
            response.sendError(400,"갱신 실패!");
            return null;
        }
        try {
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()))
                    .build()
                    .verify(header.replace(JwtProperties.TOKEN_PREFIX, ""));
            new_token = JWT.create()
                    .withSubject(decodedJWT.getSubject())
                    .withClaim("role", "MANAGER")
                    .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_TIME))
                    .sign(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()));
        }catch (TokenExpiredException ex) {
            response.sendError(400,"토큰 만료!");
            return null;
        }
        return responseService.getSingleResult(JwtProperties.TOKEN_PREFIX + new_token);
//        String token = JWT.create()
//                .withSubject(client.getUserId())
//                .withClaim("role", "CLIENT")
//                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.REFRESH_TIME))
//                .withAudience(client.getMacAddress())
//                .sign(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()));
//        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + token);
    }

//    @PostMapping(value = "login")
//    public SingleResult<?> login(@RequestBody @Valid LoginVO login){
//        ManagerVO manager = new ManagerVO();
//        boolean isLogin = loginService.login(login);
//        if(isLogin) manager =  managerService.findById(login.getId());
//        HashMap<String, Object> map = new HashMap<>();
//
//        map.put("info",manager);
//        map.put("FirstLogged",login.getPassword().equals("oms20190211"));
//        return responseService.getSingleResult(manager);
//    }

}
