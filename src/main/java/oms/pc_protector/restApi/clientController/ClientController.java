package oms.pc_protector.restApi.clientController;

import oms.pc_protector.restApi.client.service.ClientService;
import oms.pc_protector.restApi.clientFile.service.ClientFileService;
import oms.pc_protector.restApi.policy.service.ConfigurationService;
import oms.pc_protector.restApi.user.model.UserVO;
import oms.pc_protector.restApi.user.service.UserService;
import lombok.extern.java.Log;
import oms.pc_protector.restApi.client.model.ClientVO;
import oms.pc_protector.restApi.process.service.ProcessService;
import oms.pc_protector.restApi.process.model.ProcessVO;
import oms.pc_protector.restApi.result.model.InspectionResultsVO;
import oms.pc_protector.restApi.result.service.ResultService;
import oms.pc_protector.restApi.user.model.UserResponseVO;
import oms.pc_protector.apiConfig.model.SingleResult;
import oms.pc_protector.apiConfig.service.ResponseService;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.*;

@Log
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

    public ClientController(ResponseService responseService, UserService userService,
                            ClientFileService clientFileService,
                            ProcessService processService,ConfigurationService configurationService,
                            ResultService resultService, ClientService clientService) {
        this.responseService = responseService;
        this.userService = userService;
        this.clientFileService = clientFileService;
        this.processService = processService;
        this.configurationService = configurationService;
        this.resultService = resultService;
        this.clientService = clientService;
    }


    @GetMapping(value = "")
    public SingleResult<?> getClientList(@RequestParam @Valid String id) {
        List<ClientVO> clientVOList = clientService.getClientList(id);

        return responseService.getSingleResult(clientVOList);
    }



    @PostMapping(value = "/first-request")
    public SingleResult<?> clientStartRequest(@RequestBody ClientVO clientVO) {
        log.info("요청 URL : /v1/client/first-request");
        HashMap<String, Object> map = new HashMap<>();
        boolean forceRun = configurationService.findForceRun().isForceRun();

        HashMap configuration = Optional
                .ofNullable(configurationService.findConfigurationToClient())
                .orElseGet(HashMap::new);

        String department = Optional.ofNullable(userService.clientLogin(clientVO))
                .map(UserResponseVO::getDepartment)
                .orElse("");

        String md5 = Optional.ofNullable(clientFileService.findRecentMd5())
                .orElse("");

        map.put("configuration", configuration);
        map.put("forceRun", forceRun);
        map.put("department", department);
        map.put("md5", md5);
        return responseService.getSingleResult(map);
    }

    @PostMapping(value = "/process")
    public SingleResult<?> clientProcessAdd(@NotNull @RequestBody ProcessVO processVO) {
        log.info("요청 URL : /v1/client/process");
        HashMap<String, Object> map = new HashMap<>();
        Optional.ofNullable(processVO.getProcessVOList())
                .ifPresent(processService::insertProcess);
        return responseService.getSingleResult(map);
    }


    @PostMapping(value = "/install/wrong-md5")
    public SingleResult<?> modulation(@NotNull @RequestBody ClientVO clientVO) {
        log.info("요청 URL : /v1/client/install/wrong-md5");
        HashMap<String, Object> map = new HashMap<>();
        Optional.of(clientVO).ifPresent(clientService::registerWrongMd5);
        return responseService.getSingleResult("");
    }


    @PostMapping(value = "/result")
    public SingleResult<?> postResult(@NotNull @RequestBody InspectionResultsVO inspectionResultVO) {
        log.info("요청 URL : /v1/client/result");
        HashMap<String, Object> map = new HashMap<>();
        resultService.registrationResult(inspectionResultVO);
        return responseService.getSingleResult(map);
    }

    /**
    @GetMapping(value = "/process/approved-process")
    public SingleResult<?> getApprovedProcessList() {
        log.info("요청 URL : /v1/client/process/approved-process");
        HashMap<String, Object> map = new HashMap<>();
        Optional.ofNullable(processService.findRegistryItem("process"))
                .ifPresent(processList -> map.put("registryItem", processList));
        return responseService.getSingleResult(map);
    }


    @GetMapping(value = "/cycle-days-notice")
    public SingleResult<?> cycleDaysNotice() {
        log.info("요청 URL : /v1/client/cycle-days-notice");
        HashMap<String, Object> map = new HashMap<>();
        String cycleCheckDays = Optional.ofNullable(policyService.findCycleCheckDays()).orElse("");
        map.put("notice", cycleCheckDays);
        return responseService.getSingleResult(map);
    }


    @GetMapping(value = "/install/md5")
    public SingleResult<?> getMd5() {
        log.info("요청 URL : /v1/client/install/md5");
        HashMap<String, Object> map = new HashMap<>();
        String md5 = Optional.ofNullable(clientFileService.findRecentMd5()).orElse("");
        map.put("md5", md5);
        return responseService.getSingleResult(map);
    }

    @GetMapping(value = "/process/required-process")
    public SingleResult<?> getRequiredProcessList() {
        log.info("요청 URL : /v1/client/process/required-process");
        HashMap<String, Object> map = new HashMap<>();
        Optional.ofNullable(processService.findRegistryItem("required"))
                .ifPresent(processList -> map.put("registryItem", processList));
        return responseService.getSingleResult(map);
    }

    @GetMapping(value = "/configuration")
    public SingleResult<?> getConfiguration() {
        log.info("요청 URL : /v1/client/configuration");
        HashMap map = Optional.ofNullable(configurationService.findConfiguration())
                .orElseGet(HashMap::new);
        return responseService.getSingleResult(map);
    }


    @GetMapping(value = "/qna/{qnaIdx}")
    public SingleResult<?> getQna(@PathVariable int qnaIdx) {
        log.info("요청 URL : /v1/client/qna/" + qnaIdx);
        HashMap<String, Object> map = new HashMap<>();
        String url = null;
        QnaVO qnaVO = new QnaVO();
        if (qnaService.findQna(qnaIdx) != null) {
            qnaVO = qnaService.findQna(qnaIdx);
            url = qnaVO.getUrl();
        }
        map.put("url", url);
        return responseService.getSingleResult(map);
    }
    **/





}
