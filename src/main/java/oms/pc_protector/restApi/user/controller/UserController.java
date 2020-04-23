package oms.pc_protector.restApi.user.controller;

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
@RestController
@RequestMapping("/v1/Users")  // 사용자 API
public class UserController {

    private final ResponseService responseService;

    private final UserService userService;

    public UserController(ResponseService responseService, UserService userService) {
        this.responseService = responseService;
        this.userService = userService;
    }


    @GetMapping(value = "")
    public SingleResult<?> findAll() {
        HashMap<String, Object> map = new HashMap<>();
        List<UserVO> list = Optional.ofNullable(userService.findAll())
                .orElseGet(() -> Collections.EMPTY_LIST);
        map.put("userList", list);
        return responseService.getSingleResult(map);
    }


    @PostMapping(value = "")
    public SingleResult<?> register(@RequestBody @Valid UserRequestVO userRequestVO) {
        HashMap<String, Object> map = new HashMap<>();
        userService.registryFromAdmin(userRequestVO);
        return responseService.getSingleResult(map);
    }


    @PutMapping(value = "/{id}")
    public SingleResult<?> modify(
            @PathVariable(value = "id") String id,
            @RequestBody UserRequestVO userRequestVO) {
        log.info("사용자 정보 수정 : " + id);
        HashMap<String, Object> map = new HashMap<>();
        boolean modify = userService.modifyUserInfo(id, userRequestVO);
        return responseService.getSingleResult(modify);
    }


    @DeleteMapping(value = "/{id}")
    public SingleResult<?> delete(@PathVariable(value = "id") String id) {
        log.info("사용자 정보 삭제 : " + id);
        HashMap<String, Object> map = new HashMap<>();
        boolean delete = userService.removeUserInfo(id);
        return responseService.getSingleResult(delete);
    }

}
