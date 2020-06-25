package oms.pc_protector.restApi.clientController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.log4j.Log4j2;
import oms.pc_protector.apiConfig.model.SingleResult;
import oms.pc_protector.apiConfig.service.ResponseService;
import oms.pc_protector.jwt.JwtProperties;
import oms.pc_protector.restApi.client.model.ClientVO;
import oms.pc_protector.restApi.client.service.ClientService;
import oms.pc_protector.restApi.clientFile.service.ClientFileService;
import oms.pc_protector.restApi.login.model.ClientLoginVO;
import oms.pc_protector.restApi.login.service.LoginService;
import oms.pc_protector.restApi.policy.mapper.ConfigurationMapper;
import oms.pc_protector.restApi.policy.service.ConfigurationService;
import oms.pc_protector.restApi.process.model.ProcessVO;
import oms.pc_protector.restApi.process.service.ProcessService;
import oms.pc_protector.restApi.result.mapper.ResultMapper;
import oms.pc_protector.restApi.result.model.InspectionResultsVO;
import oms.pc_protector.restApi.result.service.ResultService;
import oms.pc_protector.restApi.user.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;


/* 해당 클래스는 AGENT와의 통신을 위한 API만 존재. */


@Log4j2
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
    private ResultMapper resultMapper;
    private final LoginService loginService;
    private final ConfigurationMapper configurationMapper;

    public ClientController(ResponseService responseService, UserService userService,
                            ClientFileService clientFileService,
                            ProcessService processService,ConfigurationService configurationService,
                            ResultService resultService, ClientService clientService,
                            ResultMapper resultMapper, ConfigurationMapper configurationMapper,
                            LoginService loginService) {
        this.responseService = responseService;
        this.userService = userService;
        this.clientFileService = clientFileService;
        this.processService = processService;
        this.configurationService = configurationService;
        this.resultService = resultService;
        this.clientService = clientService;
        this.resultMapper = resultMapper;
        this.loginService = loginService;
        this.configurationMapper = configurationMapper;
    }

    @PostMapping(value = "/first-request")
    public SingleResult<?> clientStartRequest(@RequestBody ClientVO clientVO) {
        boolean isLogin = userService.agentLogin(clientVO, configurationService);
        if(!isLogin) return responseService.getSingleResult("서버에 등록되지 않은 사용자입니다.");
        else clientService.loginUpdateTime(clientVO.getUserId());

        String department = userService
                .findById(clientVO.getUserId())
                .getDepartment();

        String md5 = Optional.ofNullable(clientFileService.findRecentMd5()).orElse("");
        String version = Optional.ofNullable(clientFileService.findRecentVersion()).orElse("");

        boolean forceRun = configurationService
                .findForceRun()
                .isForceRun();

        HashMap configuration = Optional
                .ofNullable(configurationService.findConfigurationToClient())
                .orElseGet(HashMap::new);

        HashMap<String, Object> map = new HashMap<>();
        map.put("configuration", configuration);
        map.put("forceRun", forceRun);
        map.put("department", department);
        map.put("md5", md5);
        map.put("version", version);
        return responseService.getSingleResult(map);
    }


    @PostMapping(value = "/process")
    public SingleResult<?> clientProcessAdd(@NotNull @RequestBody ProcessVO processVO) {
        HashMap<String, Object> map = new HashMap<>();
        Optional.ofNullable(processVO.getProcessVOList())
                .ifPresent(processService::insertProcess);
        return responseService.getSingleResult(map);
    }


    @PostMapping(value = "/install/wrong-md5")
    public SingleResult<?> modulation(@NotNull @RequestBody ClientVO clientVO) {
        HashMap<String, Object> map = new HashMap<>();
        Optional.of(clientVO).ifPresent(clientService::registerWrongMd5);
        return responseService.getSingleResult("");
    }


    @PostMapping(value = "/result")
    public SingleResult<?> postResult(@NotNull @RequestBody InspectionResultsVO inspectionResultVO) {
        HashMap<String, Object> map = new HashMap<>();
        resultService.registrationResult(inspectionResultVO);

        return responseService.getSingleResult(map);
    }


    @PostMapping(value = "/login")
    public SingleResult<?> loginForClient(@RequestBody @Valid ClientLoginVO login,
                                          HttpServletRequest request,
                                          HttpServletResponse response){
        ClientVO client = new ClientVO();
        boolean isLogin = loginService.loginForClient(login);
        boolean userExist = userService.findSameId(login.getId());
        if(!isLogin && userExist) {
            clientService.register(new ClientVO(login.getId(), login.getIpAddress()));
        }
        else if(!userExist) {
            return responseService.getSingleResult("서버에 등록되지 않은 사용자입니다.");
        }
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
        result.put("nextPeriodDateArray",configurationMapper.selectNextSchedule());
        return responseService.getSingleResult(result);
    }

//    @GetMapping(value = "timeout")
//    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
//    public void timeOutResponse() throws TimeoutException, IOException {
//        return;
//    }
//
//    @GetMapping(value = "none")
//    public void noneResponse() throws InterruptedException {
//        Thread.sleep(30000);
//    }
}
