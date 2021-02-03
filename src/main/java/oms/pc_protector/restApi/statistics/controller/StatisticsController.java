package oms.pc_protector.restApi.statistics.controller;


import oms.pc_protector.apiConfig.model.SingleResult;
import oms.pc_protector.apiConfig.service.ResponseService;
import oms.pc_protector.restApi.statistics.service.StatisticsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "v1/statistics")
public class StatisticsController {

    private StatisticsService statisticsService;

    private ResponseService responseService;

    public StatisticsController(StatisticsService statisticsService, ResponseService responseService) {
        this.statisticsService = statisticsService;
        this.responseService = responseService;
    }

    @GetMapping(value = "")
    public SingleResult<?> findAll(@RequestParam(required = false) String yearMonth,
                                   @RequestParam(required = false) String department,
                                   HttpServletRequest httpServletRequest) {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        List<HashMap<String, Object>> result = statisticsService.findAllByYearMonthOrDepartment(yearMonth, department, User_Idx);
        return responseService.getSingleResult(result);
    }

}
