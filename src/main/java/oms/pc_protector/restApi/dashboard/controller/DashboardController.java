package oms.pc_protector.restApi.dashboard.controller;

import lombok.extern.slf4j.Slf4j;
import oms.pc_protector.apiConfig.model.SingleResult;
import oms.pc_protector.apiConfig.service.ResponseService;
import oms.pc_protector.restApi.dashboard.service.DashboardService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public SingleResult<?> dashboard(@RequestParam(value = "term") String term, HttpServletRequest httpServletRequest) {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
//        double beforeTime = System.currentTimeMillis();
        HashMap<String, Object> dashboardTopMap = dashboardService.dashboardTop(User_Idx);
        HashMap<String, Object> dashboardMiddleMap = dashboardService.dashboardMiddle(User_Idx);
        HashMap<String, Object> dashboardBottomMap = dashboardService.dashboardBottom(term, User_Idx);
        HashMap<String, Object> result = new HashMap<>();
        result.put("top",dashboardTopMap);
        result.put("middle",dashboardMiddleMap);
        result.put("bottom",dashboardBottomMap);
        result.put("Period",dashboardService.selectDashboardPeriod(User_Idx));
//        double afterTime = System.currentTimeMillis();
//        double secDiffTime = (afterTime - beforeTime) / 1000;
//        log.info("대시보드 전체 걸린시간 : " + secDiffTime + "초");
        return responseService.getSingleResult(result);
    }

    @GetMapping(value = "top")
    public SingleResult<?> dashboardTop(HttpServletRequest httpServletRequest) {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        HashMap<String, Object> dashboardTopMap = dashboardService.dashboardTop(User_Idx);
        return responseService.getSingleResult(dashboardTopMap);
    }

    @GetMapping(value = "middle")
    public SingleResult<?> dashboardMiddle(HttpServletRequest httpServletRequest) {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        HashMap<String, Object> dashboardMiddleMap = dashboardService.dashboardMiddle(User_Idx);
        return responseService.getSingleResult(dashboardMiddleMap);
    }

    @GetMapping(value = "bottom")
    public SingleResult<?> dashboardBottom(@RequestParam(value = "term") String term, HttpServletRequest httpServletRequest) {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        HashMap<String, Object> dashboardBottomMap = dashboardService.dashboardBottom(term, User_Idx);
        return responseService.getSingleResult(dashboardBottomMap);
    }
}
