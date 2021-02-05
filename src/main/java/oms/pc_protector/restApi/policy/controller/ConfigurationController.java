package oms.pc_protector.restApi.policy.controller;


import lombok.extern.slf4j.Slf4j;
import oms.pc_protector.apiConfig.model.SingleResult;
import oms.pc_protector.apiConfig.service.ResponseService;
import oms.pc_protector.restApi.policy.model.*;
import oms.pc_protector.restApi.policy.service.ConfigurationService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public SingleResult<?> findConfiguration(HttpServletRequest httpServletRequest) {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        LinkedHashMap checkListMap = configurationService.findConfiguration(User_Idx);
        return responseService.getSingleResult(checkListMap);
    }

    @GetMapping(value = "/schedule")
    public SingleResult<?> findScheduleAll(HttpServletRequest httpServletRequest) {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        List<PeriodDateVO> scheduleList = configurationService.findScheduleAll(User_Idx);
        return responseService.getSingleResult(scheduleList);
    }

    @PutMapping(value = "/check/setting/config")
    public SingleResult<?> updateConfig(@RequestBody @Valid ConfigurationVO configurationVO,
                                        HttpServletRequest httpServletRequest) {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        configurationVO.setDepartmentIdx(User_Idx);
        configurationService.updateConfiguration_service(configurationVO);
        return responseService.getSingleResult(true);
    }

    @PutMapping(value = "/check/setting/edit")
    public SingleResult<?> updateEdit(@RequestBody @Valid EditProgramVO editProgramVO,
                                      HttpServletRequest httpServletRequest) {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        editProgramVO.setDepartmentIdx(User_Idx);
        configurationService.updateEditProgramFlag_service(editProgramVO);
        return responseService.getSingleResult(true);
    }

    @PutMapping(value = "/check/setting/usb")
    public SingleResult<?> updateUsb(@RequestBody @Valid SecurityUsbDetailsVO securityUsbDetailsVO,
                                     HttpServletRequest httpServletRequest) {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        securityUsbDetailsVO.setDepartmentIdx(User_Idx);
        configurationService.updateSecurityUsbDetails_service(securityUsbDetailsVO);
        return responseService.getSingleResult(true);
    }

    @PutMapping(value = "/check/force")
    public SingleResult<?> updateForceRun(@RequestBody @Valid boolean param,
                                          HttpServletRequest httpServletRequest) {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        configurationService.updateForceRun(param, User_Idx);
        return responseService.getSingleResult(true);
    }

    @PutMapping(value = "/schedule/update")
    public SingleResult<?> updateSchedule(@RequestBody RequestPeriodDateVO requestPeriodDateVO,
                                          HttpServletRequest httpServletRequest) throws ParseException {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        requestPeriodDateVO.setDepartmentIdx(User_Idx);
        int resultNum = configurationService.updateSchedule(requestPeriodDateVO);
        boolean responseResult = resultNum > 0;
        return responseService.getSingleResult(responseResult);
    }
}

