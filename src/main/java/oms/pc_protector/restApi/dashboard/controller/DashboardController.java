package oms.pc_protector.restApi.dashboard.controller;

import lombok.extern.log4j.Log4j2;
import oms.pc_protector.apiConfig.model.SingleResult;
import oms.pc_protector.apiConfig.service.ResponseService;
import oms.pc_protector.restApi.dashboard.service.DashboardService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Log4j2
@CrossOrigin
@RestController
@RequestMapping("/v1/dashboard")
public class DashboardController {

    private ResponseService responseService;

    private DashboardService dashboardService;

    public DashboardController(ResponseService responseService,
                               DashboardService dashboardService) {
        this.responseService = responseService;
        this.dashboardService = dashboardService;
    }

    @GetMapping(value = "top")
    public SingleResult<?> dashboardTop() {
        HashMap<String, Object> dashboardTopMap = dashboardService.dashboardTop();
        return responseService.getSingleResult(dashboardTopMap);
    }

    @GetMapping(value = "middle")
    public SingleResult<?> dashboardMiddle() {
        HashMap<String, Object> dashboardMiddleMap = dashboardService.dashboardMiddle();
        return responseService.getSingleResult(dashboardMiddleMap);
    }

    @GetMapping(value = "bottom")
    public SingleResult<?> dashboardBottom() {
        HashMap<String, Object> dashboardMiddleMap = dashboardService.dashboardBottom();
        return responseService.getSingleResult(dashboardMiddleMap);
    }
}
