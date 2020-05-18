package oms.pc_protector.restApi.login.controller;

import oms.pc_protector.apiConfig.model.SingleResult;
import lombok.extern.log4j.Log4j2;
import oms.pc_protector.apiConfig.service.ResponseService;
import oms.pc_protector.restApi.login.model.LoginVO;
import oms.pc_protector.restApi.login.service.LoginService;
import oms.pc_protector.restApi.manager.model.ManagerVO;
import oms.pc_protector.restApi.manager.model.ResponseManagerVO;
import oms.pc_protector.restApi.manager.service.ManagerService;
import oms.pc_protector.restApi.user.model.UserVO;
import oms.pc_protector.restApi.user.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;

@Log4j2
@RestController
@RequestMapping(value = "v1/login")
public class LoginController {

    private ResponseService responseService;

    private LoginService loginService;

    private ManagerService managerService;

    public LoginController(ResponseService responseService,
                           LoginService loginService,
                           ManagerService managerService){
        this.responseService = responseService;
        this.loginService = loginService;
        this.managerService = managerService;
    }

    @PostMapping(value = "")
    public SingleResult<?> login(@RequestBody @Valid LoginVO login){
        ResponseManagerVO manager = new ResponseManagerVO();
        boolean isLogin = loginService.login(login);
        if(isLogin) manager =  managerService.findById(login.getId());
        HashMap<String, Object> map = new HashMap<>();
        map.put("manager",manager);
        map.put("FirstLogged",login.getPassword().equals("oms20190211"));
        return responseService.getSingleResult(map);
    }


}
