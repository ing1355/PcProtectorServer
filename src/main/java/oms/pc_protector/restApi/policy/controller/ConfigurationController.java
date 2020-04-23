package oms.pc_protector.restApi.policy.controller;


import oms.pc_protector.apiConfig.model.SingleResult;
import oms.pc_protector.apiConfig.service.ResponseService;
import oms.pc_protector.restApi.policy.model.ConfigurationVO;
import oms.pc_protector.restApi.policy.model.PeriodDateVO;
import oms.pc_protector.restApi.policy.model.RequestConfigurationVO;
import oms.pc_protector.restApi.policy.service.ConfigurationService;
import lombok.extern.log4j.Log4j2;
import oms.pc_protector.restApi.process.model.ProcessVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@Log4j2
@RestController
@CrossOrigin
@RequestMapping(value = "v1/policy")
public class ConfigurationController {

    private final ResponseService responseService;

    private final ConfigurationService configurationService;

    public ConfigurationController(ConfigurationService configurationService,
                                   ResponseService responseService) {
        this.configurationService = configurationService;
        this.responseService = responseService;
    }


    @GetMapping(value = "/check")
    public SingleResult<?> findConfiguration() {
        HashMap checkListMap = configurationService.findConfiguration();
        return responseService.getSingleResult(checkListMap);
    }


    @PostMapping(value = "/check")
    public SingleResult<?> registerConfiguration(@RequestBody RequestConfigurationVO requestConfigurationVO) {
        configurationService.registerConfiguration(requestConfigurationVO);
        return responseService.getSingleResult(true);
    }


    @GetMapping(value = "/schedule")
    public SingleResult<?> findScheduleAll() {
        List<PeriodDateVO> scheduleList = configurationService.findScheduleAll();
        return responseService.getSingleResult(scheduleList);
    }


    @PostMapping(value = "/schedule")
    public SingleResult<?> registerSchedule(@RequestBody PeriodDateVO periodDateVO) {
        int resultNum = configurationService.registerSchedule(periodDateVO);
        boolean responseResult = resultNum > 0;
        return responseService.getSingleResult(responseResult);
    }

}

