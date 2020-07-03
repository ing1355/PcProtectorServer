package oms.pc_protector.restApi.user.controller;

import oms.pc_protector.restApi.client.model.ClientVO;
import oms.pc_protector.restApi.client.service.ClientService;
import oms.pc_protector.restApi.department.service.DepartmentService;
import oms.pc_protector.restApi.user.model.RequestUserVO;
import oms.pc_protector.restApi.user.model.UserSearchInputVO;
import oms.pc_protector.restApi.user.model.UserVO;
import oms.pc_protector.restApi.user.service.UserService;
import lombok.extern.log4j.Log4j2;
import oms.pc_protector.apiConfig.model.SingleResult;
import oms.pc_protector.apiConfig.service.ResponseService;
import oms.pc_protector.restApi.user.model.UserRequestVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@Log4j2
@CrossOrigin
@RestController
@RequestMapping("/v1/users")  // 사용자 API
public class UserController {

    private final ResponseService responseService;
    private final UserService userService;
    private final DepartmentService departmentService;
    private final ClientService clientService;

    public UserController(ResponseService responseService, UserService userService,
                          DepartmentService departmentService, ClientService clientService) {
        this.responseService = responseService;
        this.userService = userService;
        this.departmentService = departmentService;
        this.clientService = clientService;
    }


    @GetMapping(value = "")
    public SingleResult<?> findAll() {
        List<UserVO> list = Optional.ofNullable(userService.findAll())
                .orElseGet(ArrayList::new);
        return responseService.getSingleResult(list);
    }

    @GetMapping(value = "client")
    public SingleResult<?> findClientList(@RequestParam @Valid String id) {
        ClientVO clientVO = clientService.findById(id);
        return responseService.getSingleResult(clientVO);
    }

    @GetMapping(value = "/duplicated")
    public SingleResult<?> duplicatedId(@RequestParam(value = "userId") String Id) {
        return responseService.getSingleResult(userService.findSameId(Id));
    }

    @GetMapping(value = "/search")
    public SingleResult<?> search(@RequestParam(value = "userId", required = false) String userId,
                                  @RequestParam(value = "name", required = false) String name,
                                  @RequestParam(value = "departmentCode", required = false) Long departmentCode,
                                  @RequestParam(value = "phone", required = false) String phone){
        UserSearchInputVO userSearchVO = new UserSearchInputVO();
        userSearchVO.setUserId(userId);
        userSearchVO.setName(name);
        userSearchVO.setPhone(phone);
        userSearchVO.setDepartmentCode(departmentCode);
        List<UserVO> userList = Optional.ofNullable(userService.findBySearchInput(userSearchVO))
                .orElseThrow(() -> new RuntimeException("값이 없습니다."));
        return responseService.getSingleResult(userList);
    }


    @PostMapping(value = "/registerList")
    public SingleResult<?> registerList(@RequestBody @Valid List<UserVO> userVOList) {
        userService.registryFromAdminList(userVOList);
        List<UserVO> list = Optional.ofNullable(userService.findAll())
                .orElseGet(ArrayList::new);
        return responseService.getSingleResult(list);
    }


    @PostMapping(value = "/register")
    public SingleResult<?> register(@RequestBody @Valid UserRequestVO userRequestVO) {
        userService.registryFromAdmin(userRequestVO);
        List<UserVO> list = Optional.ofNullable(userService.findAll())
                .orElseGet(ArrayList::new);
        return responseService.getSingleResult(list);
    }


    @PutMapping(value = "/{id}")
    public SingleResult<?> update(@PathVariable(value = "id") String id,
                                  @RequestBody UserVO userVO) {
        RequestUserVO requestUserVO = new RequestUserVO();
        requestUserVO.setUserId(id);
        requestUserVO.setName(userVO.getName());
        requestUserVO.setDepartment(userVO.getDepartment());
        requestUserVO.setDepartmentCode(userVO.getDepartmentCode());
        requestUserVO.setPhone(userVO.getPhone());
        requestUserVO.setEmail(userVO.getEmail());
        boolean update = userService.updateUserInfo(requestUserVO);
        List<UserVO> list = Optional.ofNullable(userService.findAll())
                .orElseGet(ArrayList::new);
        return responseService.getSingleResult(list);
    }

//    @PutMapping(value = "/update/{id}")
//    public SingleResult<?> modify(
//            @PathVariable(value = "id") String id,
//            @RequestBody UserRequestVO userRequestVO) {
//        HashMap<String, Object> map = new HashMap<>();
//        boolean modify = userService.modifyUserInfo(id, userRequestVO);
//        return responseService.getSingleResult(modify);
//    }


    @DeleteMapping(value = "/delete")
    public SingleResult<?> delete(@RequestBody @Valid List<String> id) {
        for (String ID : id) {
            userService.removeUserInfo(ID);
        }
        List<UserVO> list = Optional.ofNullable(userService.findAll())
                .orElseGet(ArrayList::new);
        return responseService.getSingleResult(list);
    }

}
