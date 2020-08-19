package oms.pc_protector.restApi.dashboard.controller;

import lombok.extern.slf4j.Slf4j;
import oms.pc_protector.apiConfig.model.SingleResult;
import oms.pc_protector.apiConfig.service.ResponseService;
import oms.pc_protector.restApi.dashboard.service.DashboardService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Slf4j
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

    @GetMapping(value = "")
    public SingleResult<?> dashboard(@RequestParam(value = "term") String term) {
        double beforeTime = System.currentTimeMillis();
        HashMap<String, Object> dashboardTopMap = dashboardService.dashboardTop();
        HashMap<String, Object> dashboardMiddleMap = dashboardService.dashboardMiddle();
        HashMap<String, Object> dashboardBottomMap = dashboardService.dashboardBottom(term);
        HashMap<String, Object> result = new HashMap<>();
        result.put("top",dashboardTopMap);
        result.put("middle",dashboardMiddleMap);
        result.put("bottom",dashboardBottomMap);
        result.put("Period",dashboardService.selectDashboardPeriod());
        double afterTime = System.currentTimeMillis();
        double secDiffTime = (afterTime - beforeTime) / 1000;
        log.info("대시보드 전체 걸린시간 : " + secDiffTime + "초");
        return responseService.getSingleResult(result);
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
    public SingleResult<?> dashboardBottom(@RequestParam(value = "term") String term) {
        HashMap<String, Object> dashboardBottomMap = dashboardService.dashboardBottom(term);
        return responseService.getSingleResult(dashboardBottomMap);
    }
}
