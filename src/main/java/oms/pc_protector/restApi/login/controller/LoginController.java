package oms.pc_protector.restApi.login.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.log4j.Log4j2;
import oms.pc_protector.apiConfig.model.SingleResult;
import oms.pc_protector.apiConfig.service.ResponseService;
import oms.pc_protector.jwt.JwtProperties;
import oms.pc_protector.restApi.client.mapper.ClientMapper;
import oms.pc_protector.restApi.client.model.ClientVO;
import oms.pc_protector.restApi.client.service.ClientService;
import oms.pc_protector.restApi.login.model.ClientLoginVO;
import oms.pc_protector.restApi.login.model.LoginVO;
import oms.pc_protector.restApi.login.service.LoginService;
import oms.pc_protector.restApi.manager.model.ManagerVO;
import oms.pc_protector.restApi.manager.model.ResponseManagerVO;
import oms.pc_protector.restApi.manager.service.ManagerService;
import oms.pc_protector.restApi.user.model.UserVO;
import oms.pc_protector.restApi.user.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;

@Log4j2
@RestController
@RequestMapping(value = "/v1/login")
public class LoginController {

    private ResponseService responseService;

    private LoginService loginService;

    private ManagerService managerService;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public LoginController(ResponseService responseService,
                           LoginService loginService,
                           ManagerService managerService){
        this.responseService = responseService;
        this.loginService = loginService;
        this.managerService = managerService;
    }

    @GetMapping(value = "confirm")
    public SingleResult<?> confirm(@RequestParam(value = "id") String id,
                                   @RequestParam(value = "password") String password) {
        String encodedPassword = new BCryptPasswordEncoder().encode(password);
        String db_password = loginService.findPasswordById(id);
        boolean result = passwordEncoder.matches(password,db_password);
        return responseService.getSingleResult(result);
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

    @PostMapping(value = "client")
    public SingleResult<?> loginForClient(@RequestBody @Valid ClientLoginVO login,
                                          HttpServletRequest request,
                                          HttpServletResponse response){
        ClientVO client = new ClientVO();
        boolean isLogin = loginService.loginForClient(login);
        if(isLogin) client =  loginService.findClient(login);
        String token = null;
        token = JWT.create()
                .withSubject(client.getUserId())
                .withClaim("role", "CLIENT")
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.REFRESH_TIME))
                .withAudience(client.getIpAddress())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()));
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + token);
        return responseService.getSingleResult(client);
    }
}
