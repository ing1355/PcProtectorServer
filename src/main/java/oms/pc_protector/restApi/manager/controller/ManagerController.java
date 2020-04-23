package oms.pc_protector.restApi.manager.controller;

import lombok.extern.log4j.Log4j2;
import oms.pc_protector.apiConfig.model.SingleResult;
import oms.pc_protector.apiConfig.service.ResponseService;
import oms.pc_protector.restApi.manager.model.ManagerVO;
import oms.pc_protector.restApi.manager.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@CrossOrigin
@RequestMapping(value = "v1/manager")
public class ManagerController {

    private ResponseService responseService;
    private ManagerService managerService;

    public ManagerController(ResponseService responseService, ManagerService managerService) {
        this.responseService = responseService;
        this.managerService = managerService;
    }


    @GetMapping(value = "")
    public SingleResult<?> findManagers() {
        ManagerVO managers = managerService.findManagers();
        return responseService.getSingleResult(managers);
    }

}
