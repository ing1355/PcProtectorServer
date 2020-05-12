package oms.pc_protector.restApi.statistics.controller;


import oms.pc_protector.apiConfig.model.SingleResult;
import oms.pc_protector.apiConfig.service.ResponseService;
import oms.pc_protector.restApi.statistics.service.StatisticsService;
import oms.pc_protector.restApi.user.model.UserVO;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
                                   @RequestParam(required = false) String department) {
        List<Object> result = statisticsService.findAllByYearMonthOrDepartment(yearMonth, department);
        return responseService.getSingleResult(result);
    }

}
