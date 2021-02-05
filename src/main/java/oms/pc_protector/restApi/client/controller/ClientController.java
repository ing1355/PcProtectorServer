package oms.pc_protector.restApi.client.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import oms.pc_protector.apiConfig.model.SingleResult;
import oms.pc_protector.apiConfig.service.ResponseService;
import oms.pc_protector.jwt.JwtProperties;
import oms.pc_protector.restApi.client.mapper.ClientMapper;
import oms.pc_protector.restApi.client.model.ClientVO;
import oms.pc_protector.restApi.client.service.ClientService;
import oms.pc_protector.restApi.clientFile.service.ClientFileService;
import oms.pc_protector.restApi.department.mapper.DepartmentMapper;
import oms.pc_protector.restApi.department.model.DepartmentVO;
import oms.pc_protector.restApi.login.model.ClientLoginVO;
import oms.pc_protector.restApi.login.service.LoginService;
import oms.pc_protector.restApi.policy.mapper.ConfigurationMapper;
import oms.pc_protector.restApi.policy.service.ConfigurationService;
import oms.pc_protector.restApi.process.model.ProcessVO;
import oms.pc_protector.restApi.process.service.ProcessService;
import oms.pc_protector.restApi.result.model.InspectionResultsVO;
import oms.pc_protector.restApi.result.service.ResultService;
import oms.pc_protector.restApi.user.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;


/* 해당 클래스는 AGENT와의 통신을 위한 API만 존재. */


@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/v1/client")
public class ClientController {

    private final ResponseService responseService;
    private final UserService userService;
    private final ClientFileService clientFileService;
    private final ProcessService processService;
    private final ConfigurationService configurationService;
    private final ResultService resultService;
    private final ClientService clientService;
    private final ClientMapper clientMapper;
    private final LoginService loginService;
    private final ConfigurationMapper configurationMapper;
    private final DepartmentMapper departmentMapper;

    public ClientController(ResponseService responseService, UserService userService,
                            ClientFileService clientFileService,
                            ProcessService processService,ConfigurationService configurationService,
                            ResultService resultService, ClientService clientService,
                            ConfigurationMapper configurationMapper,
                            LoginService loginService, DepartmentMapper departmentMapper,
                            ClientMapper clientMapper) {
        this.clientMapper = clientMapper;
        this.responseService = responseService;
        this.userService = userService;
        this.clientFileService = clientFileService;
        this.processService = processService;
        this.configurationService = configurationService;
        this.resultService = resultService;
        this.clientService = clientService;
        this.loginService = loginService;
        this.configurationMapper = configurationMapper;
        this.departmentMapper = departmentMapper;
    }

    @PostMapping(value = "/first-request")
    public SingleResult<?> clientStartRequest(@RequestBody ClientVO clientVO,
                                              HttpServletRequest httpServletRequest) throws ParseException {
        String code = httpServletRequest.getHeader("code");
        String departmentIdx = httpServletRequest.getHeader("departmentIdx");
        String rootIdx = departmentMapper.selectByDptCode(code).getIdx();
        clientVO.setDepartmentIdx(departmentIdx);
        boolean isLogin = userService.agentLogin(clientVO, rootIdx);
        if(!isLogin) return responseService.getSingleResult("서버에 등록되지 않은 사용자입니다.");
        else clientService.loginUpdateTime(clientVO.getUserId(), departmentIdx);

        String md5 = Optional.ofNullable(clientFileService.findRecentMd5(rootIdx)).orElse("");
        String version = Optional.ofNullable(clientFileService.findRecentVersion(rootIdx)).orElse("");

        boolean forceRun = configurationService
                .findForceRun(rootIdx);

        HashMap configuration = Optional
                .ofNullable(configurationService.findConfigurationToClient(rootIdx))
                .orElseGet(HashMap::new);

        HashMap<String, Object> map = new HashMap<>();
        map.put("configuration", configuration);
        map.put("forceRun", forceRun);
        map.put("department", clientMapper.selectDepartmentByDepartmentIdx(departmentIdx));
        map.put("md5", md5);
        map.put("version", version);
        return responseService.getSingleResult(map);
    }


    @PostMapping(value = "/process")
    public SingleResult<?> clientProcessAdd(@NotNull @RequestBody ProcessVO processVO,
                                            HttpServletRequest request) throws UnsupportedEncodingException {
        String code = request.getHeader("code");
        DepartmentVO departmentVO = departmentMapper.selectByDptCode(code);
        HashMap<String, Object> map = new HashMap<>();
        if(processVO.getProcessVOList() != null) {
            processService.insertProcess(processVO.getProcessVOList(), departmentVO.getIdx());
        }
        return responseService.getSingleResult(map);
    }


    @PostMapping(value = "/install/wrong-md5")
    public SingleResult<?> modulation(@NotNull @RequestBody ClientVO clientVO,
                                      HttpServletRequest request) {
        String code = request.getHeader("code");
        String client_departmentIdx = request.getHeader("departmentIdx");
        DepartmentVO departmentVO = departmentMapper.selectByDptCode(code);
        clientVO.setDepartmentIdx(client_departmentIdx);
        HashMap<String, Object> map = new HashMap<>();
        Optional.of(clientVO).ifPresent(clientService::registerWrongMd5);
        return responseService.getSingleResult("");
    }


    @PostMapping(value = "/result")
    public SingleResult<?> postResult(@NotNull @RequestBody InspectionResultsVO inspectionResultVO,
                                      HttpServletRequest request) {
        String code = request.getHeader("code");
        String client_departmentIdx = request.getHeader("departmentIdx");
        DepartmentVO departmentVO = departmentMapper.selectByDptCode(code);
        inspectionResultVO.getClientVO().setDepartmentIdx(client_departmentIdx);
        HashMap<String, Object> map = new HashMap<>();
        resultService.registrationResult(inspectionResultVO, departmentVO.getIdx());

        return responseService.getSingleResult(map);
    }


    @PostMapping(value = "/login")
    public SingleResult<?> loginForClient(@RequestBody @Valid ClientLoginVO login,
                                          HttpServletRequest request,
                                          HttpServletResponse response){
        ClientVO client = new ClientVO();
        String code = request.getHeader("code");
        DepartmentVO departmentVO = departmentMapper.selectByDptCode(code);
        String departmentIdx = clientService.findClient(login.getId(), departmentVO.getIdx());
        if(departmentIdx != null) {
            clientService.register(new ClientVO(login.getId(), login.getIpAddress(), departmentIdx));
        }
        else {
            return responseService.getSingleResult("서버에 등록되지 않은 사용자입니다.");
        }
        login.setDepartmentIdx(departmentIdx);
        client = loginService.findClient(login);
        String token = JWT.create()
                .withSubject(client.getUserId())
                .withClaim("role", "CLIENT")
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.REFRESH_TIME))
                .withAudience(client.getMacAddress())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()));
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + token);
        HashMap<String, Object> result = new HashMap<>();
        result.put("client",client);
        result.put("nextPeriodDateArray",configurationMapper.selectNextSchedule(departmentVO.getIdx()));
        return responseService.getSingleResult(result);
    }
}
