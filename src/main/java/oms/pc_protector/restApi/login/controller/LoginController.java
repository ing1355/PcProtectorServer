package oms.pc_protector.restApi.login.controller;

import oms.pc_protector.apiConfig.model.SingleResult;
import oms.pc_protector.apiConfig.service.ResponseService;
import oms.pc_protector.restApi.login.model.LoginVO;
import oms.pc_protector.restApi.login.service.LoginService;
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

    public LoginController(ResponseService responseService,
                           LoginService loginService){
        this.responseService = responseService;
        this.loginService = loginService;
    }

    @PostMapping(value = "")
    public SingleResult<?> login(@RequestBody @Valid LoginVO login){
        boolean isLogin = loginService.login(login);
        return responseService.getSingleResult(isLogin);
    }


}
