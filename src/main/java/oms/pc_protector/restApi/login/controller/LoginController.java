package oms.pc_protector.restApi.login.controller;

import oms.pc_protector.apiConfig.model.SingleResult;
import oms.pc_protector.apiConfig.service.ResponseService;
import oms.pc_protector.restApi.login.model.LoginVO;
import oms.pc_protector.restApi.login.service.LoginService;
import oms.pc_protector.restApi.user.model.UserVO;
import oms.pc_protector.restApi.user.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "v1/login")
public class LoginController {

    private ResponseService responseService;

    private LoginService loginService;

    private UserService userService;

    public LoginController(ResponseService responseService,
                           LoginService loginService,
                           UserService userService){
        this.responseService = responseService;
        this.loginService = loginService;
        this.userService = userService;
    }

    @PostMapping(value = "")
    public SingleResult<?> login(@RequestBody @Valid LoginVO login){
        boolean isLogin = loginService.login(login);
        UserVO user = new UserVO();
        if(isLogin) user = userService.findById(login.getId());
        return responseService.getSingleResult(user);
    }


}
