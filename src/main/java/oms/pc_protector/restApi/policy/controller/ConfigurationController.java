package oms.pc_protector.restApi.policy.controller;


import lombok.extern.slf4j.Slf4j;
import oms.pc_protector.apiConfig.model.SingleResult;
import oms.pc_protector.apiConfig.service.ResponseService;
import oms.pc_protector.restApi.policy.model.*;
import oms.pc_protector.restApi.policy.service.ConfigurationService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
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
        LinkedHashMap checkListMap = configurationService.findConfiguration();
        return responseService.getSingleResult(checkListMap);
    }

    @GetMapping(value = "/schedule")
    public SingleResult<?> findScheduleAll() {
        List<PeriodDateVO> scheduleList = configurationService.findScheduleAll();
        return responseService.getSingleResult(scheduleList);
    }

    @GetMapping(value = "/schedule/applied")
    public SingleResult<?> findApply() {
        PeriodDateVO result = configurationService.findAppliedSchedule();
        return responseService.getSingleResult(result);
    }

    @PutMapping(value = "/check/setting/config")
    public SingleResult<?> updateConfig(@RequestBody @Valid ConfigurationVO configurationVO) {
        configurationService.updateConfiguration_service(configurationVO);
        return responseService.getSingleResult(true);
    }

    @PutMapping(value = "/check/setting/edit")
    public SingleResult<?> updateEdit(@RequestBody @Valid EditProgramVO editProgramVO) {
        configurationService.updateEditProgramFlag_service(editProgramVO);
        return responseService.getSingleResult(true);
    }

    @PutMapping(value = "/check/setting/usb")
    public SingleResult<?> updateUsb(@RequestBody @Valid SecurityUsbDetailsVO securityUsbDetailsVO) {
        configurationService.updateSecurityUsbDetails_service(securityUsbDetailsVO);
        return responseService.getSingleResult(true);
    }

    @PutMapping(value = "/check/force")
    public SingleResult<?> updateForceRun(@RequestBody @Valid boolean param) {
        configurationService.updateForceRun(param);
        return responseService.getSingleResult(true);
    }

    @PostMapping(value = "/schedule/insert")
    public SingleResult<?> registerSchedule(@RequestBody PeriodDateVO periodDateVO) {
        int resultNum = configurationService.registerSchedule(periodDateVO);
        boolean responseResult = resultNum > 0;
        return responseService.getSingleResult(responseResult);
    }

    @PutMapping(value = "/schedule/update")
    public SingleResult<?> updateSchedule(@RequestBody RequestPeriodDateVO requestPeriodDateVO) throws ParseException {
        int resultNum = configurationService.updateSchedule(requestPeriodDateVO);
        boolean responseResult = resultNum > 0;
        return responseService.getSingleResult(responseResult);
    }

    @PutMapping(value = "/schedule/apply")
    public SingleResult<?> updateApply(@RequestParam(value = "old_idx") Long old_idx,
                                       @RequestParam(value = "new_idx") Long new_idx) {
        int resultNum = configurationService.updateApply(old_idx, new_idx);
        boolean responseResult = resultNum > 0;
        return responseService.getSingleResult(responseResult);
    }

    @DeleteMapping(value = "/schedule/delete")
    public SingleResult<?> deleteSchedule(@RequestBody PeriodDateVO periodDateVO) {
        int resultNum = configurationService.deleteSchedule(periodDateVO);
        boolean responseResult = resultNum > 0;
        return responseService.getSingleResult(responseResult);
    }
}

