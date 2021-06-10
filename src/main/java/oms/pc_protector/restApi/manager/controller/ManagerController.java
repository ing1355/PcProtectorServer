package oms.pc_protector.restApi.manager.controller;

import lombok.extern.slf4j.Slf4j;
import oms.pc_protector.apiConfig.model.SingleResult;
import oms.pc_protector.apiConfig.service.ResponseService;
import oms.pc_protector.restApi.manager.model.FirstLoginRequestManagerVO;
import oms.pc_protector.restApi.manager.model.ManagerLockVO;
import oms.pc_protector.restApi.manager.model.ManagerVO;
import oms.pc_protector.restApi.manager.model.SearchManagerVO;
import oms.pc_protector.restApi.manager.service.ManagerService;
import oms.pc_protector.restApi.manager.service.OmpassService;
import org.json.JSONObject;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.PrematureCloseException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.ConnectException;
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
    private OmpassService ompassService;

    public ManagerController(ResponseService responseService,
                             ManagerService managerService,
                             OmpassService ompassService) {
        this.responseService = responseService;
        this.managerService = managerService;
        this.ompassService = ompassService;
    }

    @PostMapping (value="/ompass/2fa")
    public SingleResult<?> ompass2Fa(@RequestBody String input, HttpServletResponse response) throws Throwable {
        JSONObject verifyInfo = new JSONObject(input);
        String userId = verifyInfo.getString("userId");
        boolean isSuccess = ompassService.verifyAccessToken(verifyInfo);
        if(isSuccess) {
            ManagerVO managerVO = managerService.findById(userId);
            log.info("ompass 인증 결과 : {}",isSuccess);
            Cookie myCookie = new Cookie("OMPASS","Y");
            myCookie.setMaxAge(3600);
            myCookie.setPath("/");
            response.addCookie(myCookie);
            return responseService.getSingleResult(managerVO.getDepartmentIdx());
        } else {
            return responseService.getSingleResult("ompass 2차 인증 실패!");
        }
    }

    @GetMapping(value = "/logout")
    public SingleResult<?> logout(HttpServletResponse response){
        log.info("아이고 배고프다");
        Cookie myCookie = new Cookie("OMPASS",null);
        myCookie.setMaxAge(0);
        myCookie.setPath("/");
        response.addCookie(myCookie);
        return responseService.getSingleResult("쿠키 초기화 완료!");
    }

    @GetMapping(value="cancel-2fa")
    public SingleResult<?> cancel2FA(@RequestParam(name="userId") String userId,HttpServletResponse response) throws Throwable {
        ompassService.deleteOmpass(userId);
        managerService.manageOmpass(userId,"N");
        log.info("ompass 삭제 및 2차인증 해제 완료! : {}",userId);

        Cookie myCookie = new Cookie("OMPASS",null);
        myCookie.setMaxAge(0);
        myCookie.setPath("/");
        response.addCookie(myCookie);

        return responseService.getSingleResult("삭제성공");

    }

    @GetMapping(value="register-ompass-url")
    public SingleResult<?> generateOmpassRegisterUrl(@RequestParam(name="userId") String userId) throws Throwable {
        ManagerVO managerVO =  managerService.findById(userId);
        if(managerVO == null){
            return responseService.getSingleResult("존재하지 않는 사용자 입니다.");
        } else {
            String ompassRegisterUrl = ompassService.generateOmpassRegisterUrl(managerVO);
            log.info("ompass reigster url : {}",ompassRegisterUrl);
            return responseService.getSingleResult(ompassRegisterUrl);
        }

    }

    @PostMapping(value = "/register-ompass")
    public SingleResult<?> registerOmpass(@RequestBody String token, HttpServletResponse response){
        JSONObject verifyTokenData = new JSONObject(token);
        String userId = verifyTokenData.getString("userId");
        if(verifyTokenData.has("accessToken") && verifyTokenData.has("key")){
            boolean verifyTokenResult = ompassService.verifyAccessToken(verifyTokenData);
            if(verifyTokenResult){
                managerService.manageOmpass(userId,"Y");
                Cookie myCookie = new Cookie("OMPASS","Y");
                myCookie.setMaxAge(3600);
                myCookie.setPath("/");
                response.addCookie(myCookie);
            }
        }
        return  null;
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
    public SingleResult<?> duplicatedManager(@RequestParam(value = "userId") String id) {
        boolean result = managerService.duplicatedManager(id);
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
        input.setDepartmentIdx(User_Idx);
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
    public SingleResult<?> updateFirstLogin(@RequestBody @Valid FirstLoginRequestManagerVO firstLoginRequestManagerVO) throws Throwable {
        boolean result = true;
        firstLoginRequestManagerVO.setDepartmentIdx(
                managerService.findById(firstLoginRequestManagerVO.getUserId()).getDepartmentIdx());
        managerService.updateManagerFirstLogin(firstLoginRequestManagerVO);
        return responseService.getSingleResult(result);
    }

    @PutMapping(value = "lock")
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public SingleResult<?> updateManagerLock(@RequestBody @Valid ManagerLockVO managerLockVO) {
        managerService.updateManagerLock(managerLockVO);
        return responseService.getSingleResult(true);
    }

    @PutMapping(value = "unlock")
    public SingleResult<?> updateManagerUnLock(@RequestBody @Valid ManagerLockVO managerLockVO) {
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
