package oms.pc_protector.restApi.user.controller;

import lombok.extern.slf4j.Slf4j;
import oms.pc_protector.apiConfig.model.SingleResult;
import oms.pc_protector.apiConfig.service.ResponseService;
import oms.pc_protector.restApi.client.model.ClientVO;
import oms.pc_protector.restApi.client.service.ClientService;
import oms.pc_protector.restApi.user.model.*;
import oms.pc_protector.restApi.user.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/v1/users")  // 사용자 API
public class UserController {

    private final ResponseService responseService;
    private final UserService userService;
    private final ClientService clientService;

    public UserController(ResponseService responseService,
                          UserService userService,
                          ClientService clientService) {
        this.responseService = responseService;
        this.userService = userService;
        this.clientService = clientService;
    }


    @GetMapping(value = "")
    public SingleResult<?> findAll(HttpServletRequest httpServletRequest) {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        List<UserVO> list = Optional.ofNullable(userService.findAll(User_Idx))
                .orElseGet(ArrayList::new);
        return responseService.getSingleResult(list);
    }

    @GetMapping(value = "client")
    public SingleResult<?> findClientList(@RequestParam @Valid String id,
                                          HttpServletRequest httpServletRequest) {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        List<ClientVO> clientVO = clientService.selectClientListById(id, User_Idx);
        return responseService.getSingleResult(clientVO);
    }

    @GetMapping(value = "/duplicated")
    public SingleResult<?> duplicatedId(@RequestParam(value = "userId") String Id,
                                        HttpServletRequest httpServletRequest) {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        return responseService.getSingleResult(userService.findSameId(Id, User_Idx));
    }

    @GetMapping(value = "/search")
    public SingleResult<?> search(@RequestParam(value = "userId", required = false) String userId,
                                  @RequestParam(value = "name", required = false) String name,
                                  @RequestParam(value = "departmentIdx", required = false) Long departmentIdx,
                                  @RequestParam(value = "phone", required = false) String phone,
                                  HttpServletRequest httpServletRequest){
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        UserSearchInputVO userSearchVO = new UserSearchInputVO();
        userSearchVO.setUserId(userId);
        userSearchVO.setName(name);
        userSearchVO.setPhone(phone);
        userSearchVO.setDepartmentIdx(departmentIdx);
        userSearchVO.setUserIdx(User_Idx);
        List<UserVO> userList = Optional.ofNullable(userService.findBySearchInput(userSearchVO))
                .orElseThrow(() -> new RuntimeException("값이 없습니다."));
        return responseService.getSingleResult(userList);
    }


    @PostMapping(value = "/registerList")
    public SingleResult<?> registerList(@RequestBody @Valid UserExcelVO userList,
                                        HttpServletRequest httpServletRequest) {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        userService.registryFromAdminList(userList, User_Idx);
        List<UserVO> list = Optional.ofNullable(userService.findAll(User_Idx))
                .orElseGet(ArrayList::new);
        return responseService.getSingleResult(true);
    }


    @PostMapping(value = "/register")
    public SingleResult<?> register(@RequestBody @Valid UserRequestVO userRequestVO,
                                    HttpServletRequest httpServletRequest) {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        userService.registryFromAdmin(userRequestVO);
        List<UserVO> list = Optional.ofNullable(userService.findAll(User_Idx))
                .orElseGet(ArrayList::new);
        return responseService.getSingleResult(list);
    }


    @PutMapping(value = "/{id}")
    public SingleResult<?> update(@PathVariable(value = "id") String id,
                                  HttpServletRequest httpServletRequest,
                                  @RequestBody UserVO userVO) {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        RequestUserVO requestUserVO = new RequestUserVO();
        requestUserVO.setUserId(id);
        requestUserVO.setName(userVO.getName());
        requestUserVO.setDepartmentIdx(userVO.getDepartmentIdx());
        requestUserVO.setPhone(userVO.getPhone());
        requestUserVO.setEmail(userVO.getEmail());
        requestUserVO.setUserIdx(User_Idx);
        boolean update = userService.updateUserInfo(requestUserVO);
        List<UserVO> list = Optional.ofNullable(userService.findAll(User_Idx))
                .orElseGet(ArrayList::new);
        return responseService.getSingleResult(list);
    }


    @DeleteMapping(value = "/delete")
    public SingleResult<?> delete(@RequestBody @Valid List<String> id,
                                  HttpServletRequest httpServletRequest) {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        for (String ID : id) {
            userService.removeUserInfo(ID, User_Idx);
        }
        List<UserVO> list = Optional.ofNullable(userService.findAll(User_Idx))
                .orElseGet(ArrayList::new);
        return responseService.getSingleResult(list);
    }

}
