package oms.pc_protector.restApi.manager.controller;

import lombok.extern.slf4j.Slf4j;
import oms.pc_protector.apiConfig.model.SingleResult;
import oms.pc_protector.apiConfig.service.ResponseService;
import oms.pc_protector.restApi.manager.model.FirstLoginRequestManagerVO;
import oms.pc_protector.restApi.manager.model.ManagerLockVO;
import oms.pc_protector.restApi.manager.model.ManagerVO;
import oms.pc_protector.restApi.manager.model.SearchManagerVO;
import oms.pc_protector.restApi.manager.service.ManagerService;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
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
    public SingleResult<?> findManagers(HttpServletRequest httpServletRequest) {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        HashMap<String, Object> map = new HashMap<>();
        List<ManagerVO> list = Optional.ofNullable(managerService.findAll(User_Idx))
                .orElseGet(() -> Collections.EMPTY_LIST);
        map.put("data", list);
        return responseService.getSingleResult(map);
    }

    @GetMapping(value = "/duplicated")
    public SingleResult<?> duplicatedManager(@RequestParam(value = "userId") String id,
                                             HttpServletRequest httpServletRequest) {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        boolean result = managerService.duplicatedManager(id, User_Idx);
        return responseService.getSingleResult(result);
    }

    @GetMapping(value = "/search")
    public SingleResult<?> searchManager(@RequestParam(value = "id", required = false) String id,
                                         @RequestParam(value = "name", required = false) String name,
                                         @RequestParam(value = "mobile", required = false) String mobile,
                                         @RequestParam(value = "email", required = false) String email,
                                         HttpServletRequest httpServletRequest) {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        HashMap<String, Object> map = new HashMap<>();
        SearchManagerVO input = new SearchManagerVO();
        input.setId(id);
        input.setName(name);
        input.setMobile(mobile);
        input.setEmail(email);
        input.setIdx(User_Idx);
        List<ManagerVO> list = Optional.ofNullable(managerService.searchManager(input))
                .orElseGet(() -> Collections.EMPTY_LIST);
        map.put("data", list);
        return responseService.getSingleResult(map);
    }

    @PostMapping(value = "register")
    public SingleResult<?> register(@RequestBody @Valid ManagerVO managerVO) {
        managerService.insertManager(managerVO);
        return responseService.getSingleResult(true);
    }

    @PutMapping(value = "update")
    public SingleResult<?> update(@RequestBody @Valid ManagerVO managerVO) throws JSONException {
        boolean result = true;
        managerService.updateManager(managerVO);
        return responseService.getSingleResult(result);
    }

    @PutMapping(value = "firstlogin")
    public SingleResult<?> updateFirstLogin(@RequestBody @Valid FirstLoginRequestManagerVO firstLoginRequestManagerVO,
                                            HttpServletRequest httpServletRequest) throws Throwable {
        boolean result = true;
        firstLoginRequestManagerVO.setDepartmentIdx(
                managerService.findById(firstLoginRequestManagerVO.getUserId()).getDepartmentIdx());
        managerService.updateManagerFirstLogin(firstLoginRequestManagerVO);
        return responseService.getSingleResult(result);
    }

    @PutMapping(value = "lock")
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public SingleResult<?> updateManagerLock(@RequestBody @Valid ManagerLockVO managerLockVO,
                                             HttpServletRequest httpServletRequest) {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        managerLockVO.setDepartmentIdx(User_Idx);
        managerService.updateManagerLock(managerLockVO);
        return responseService.getSingleResult(true);
    }

    @PutMapping(value = "unlock")
    public SingleResult<?> updateManagerUnLock(@RequestBody @Valid ManagerLockVO managerLockVO,
                                               HttpServletRequest httpServletRequest) {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        managerLockVO.setDepartmentIdx(User_Idx);
        managerService.updateManagerUnLock(managerLockVO);
        return responseService.getSingleResult(true);
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
