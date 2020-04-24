package oms.pc_protector.restApi.manager.controller;

import lombok.extern.log4j.Log4j2;
import oms.pc_protector.apiConfig.model.SingleResult;
import oms.pc_protector.apiConfig.service.ResponseService;
import oms.pc_protector.restApi.manager.model.ManagerVO;
import oms.pc_protector.restApi.manager.model.UpdateManagerVO;
import oms.pc_protector.restApi.manager.service.ManagerService;
import oms.pc_protector.restApi.process.model.ProcessVO;
import oms.pc_protector.restApi.user.model.UserVO;
import org.apache.catalina.Manager;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

@Log4j2
@RestController
@CrossOrigin
@RequestMapping("v1/manager")
public class ManagerController {

    private ResponseService responseService;
    private ManagerService managerService;

    public ManagerController(ResponseService responseService, ManagerService managerService) {
        this.responseService = responseService;
        this.managerService = managerService;
    }


    @GetMapping(value = "get")
    public SingleResult<?> findManagers() {
        HashMap<String, Object> map = new HashMap<>();
        List<ManagerVO> list = Optional.ofNullable(managerService.findAll())
                .orElseGet(() -> Collections.EMPTY_LIST);
        map.put("data", list);
        return responseService.getSingleResult(map);
    }

    @PostMapping(value = "register")
    public SingleResult<?> register(@RequestBody @Valid ManagerVO managerVO) {
        managerService.insertManager(managerVO);
        return responseService.getSingleResult(true);
    }

    @PutMapping(value = "/update")
    public SingleResult<?> update(@RequestBody @Valid String id) throws JSONException {
        boolean result = true;

        managerService.updateManager(id);
        return responseService.getSingleResult(result);
    }

    @DeleteMapping(value = "/delete/{id}")
    public SingleResult<?> delete(@PathVariable(value = "id") List<String> id) {
        HashMap<String, Object> map = new HashMap<>();
        boolean delete = true;
        for (String ID : id) {
            if (!managerService.removeManager(ID)) {
                delete = false;
                break;
            }
        }
        return responseService.getSingleResult(delete);
    }
}
