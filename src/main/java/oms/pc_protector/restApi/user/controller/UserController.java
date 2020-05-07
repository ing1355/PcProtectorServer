package oms.pc_protector.restApi.user.controller;

import oms.pc_protector.restApi.user.model.RequestUserVO;
import oms.pc_protector.restApi.user.model.UserVO;
import oms.pc_protector.restApi.user.service.UserService;
import lombok.extern.log4j.Log4j2;
import oms.pc_protector.apiConfig.model.SingleResult;
import oms.pc_protector.apiConfig.service.ResponseService;
import oms.pc_protector.restApi.user.model.UserRequestVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Log4j2
@CrossOrigin
@RestController
@RequestMapping("/v1/users")  // 사용자 API
public class UserController {

    private final ResponseService responseService;

    private final UserService userService;

    public UserController(ResponseService responseService, UserService userService) {
        this.responseService = responseService;
        this.userService = userService;
    }


    @GetMapping(value = "")
    public SingleResult<?> findAll() {
        List<UserVO> list = Optional.ofNullable(userService.findAll())
                .orElseGet(() -> Collections.EMPTY_LIST);
        return responseService.getSingleResult(list);
    }

    @GetMapping(value = "/search")
    public SingleResult<?> search(@RequestParam(value = "userId", required = false) String userId,
                                  @RequestParam(value = "name", required = false) String name,
                                  @RequestParam(value = "department", required = false) String department,
                                  @RequestParam(value = "phone", required = false) String phone){
        List<UserVO> list = Optional.ofNullable(userService.search(userId, name, department, phone))
                .orElseGet(() -> Collections.EMPTY_LIST);
        return responseService.getSingleResult(list);
    }

    @PostMapping(value = "/registerList")
    public SingleResult<?> registerList(@RequestBody @Valid List<UserVO> userVOList) {
        userService.registryFromAdminList(userVOList);
        return responseService.getSingleResult(true);
    }

    @PostMapping(value = "/register")
    public SingleResult<?> register(@RequestBody @Valid UserRequestVO userRequestVO) {
        log.info("-------------------------");
        log.info("------사용자 등록 API------");
        //log.info("ID : " + userRequestVO.getUserId());
        log.info("NAME : " + userRequestVO.getName());
        log.info("DEPARTMENT : " + userRequestVO.getDepartment());
        log.info("PHONE : " + userRequestVO.getPhone());
        log.info("EMAIL : " + userRequestVO.getEmail());
        log.info("-------------------------");
        HashMap<String, Object> map = new HashMap<>();
        userService.registryFromAdmin(userRequestVO);
        return responseService.getSingleResult(map);
    }

    @PutMapping(value = "/{id}")
    public SingleResult<?> update(@PathVariable(value = "id") String id,
                                  @RequestBody UserVO userVO) {
        RequestUserVO requestUserVO = new RequestUserVO();
        requestUserVO.setOld_id(id);
        requestUserVO.setUserId(userVO.getUserId());
        requestUserVO.setName(userVO.getName());
        requestUserVO.setDepartment(userVO.getDepartment());
        requestUserVO.setPhone(userVO.getPhone());
        requestUserVO.setEmail(userVO.getEmail());
        boolean update = userService.updateUserInfo(requestUserVO);
        return responseService.getSingleResult(update);
    }

    @PostMapping(value = "/departmentDeletedChild")
    public SingleResult<?> departmentDeletedChild(@RequestBody String departmentName) {
        boolean update = userService.departmentDeletedChild(departmentName);
        return responseService.getSingleResult(update);
    }

    @PostMapping(value = "/departmentDeletedFirst")
    public SingleResult<?> departmentDeletedFirst(@RequestBody String departmentName) {
        boolean update = userService.departmentDeletedFirst(departmentName);
        return responseService.getSingleResult(update);
    }

    @PutMapping(value = "/update/{id}")
    public SingleResult<?> modify(
            @PathVariable(value = "id") String id,
            @RequestBody UserRequestVO userRequestVO) {
        log.info("사용자 정보 수정 : " + id);
        HashMap<String, Object> map = new HashMap<>();
        boolean modify = userService.modifyUserInfo(id, userRequestVO);
        return responseService.getSingleResult(modify);
    }


    @DeleteMapping(value = "/delete")
    public SingleResult<?> delete(@RequestBody @Valid List<String> id) {
        log.info("사용자 정보 삭제 : " + id);
//        HashMap<String, Object> map = new HashMap<>();
        for (String ID : id) {
            userService.removeUserInfo(ID);
        }
        return responseService.getSingleResult(true);
    }

}
