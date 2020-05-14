package oms.pc_protector.restApi.log.controller;

import lombok.extern.log4j.Log4j2;
import oms.pc_protector.apiConfig.model.SingleResult;
import oms.pc_protector.apiConfig.service.ResponseService;
import oms.pc_protector.restApi.log.model.LogRequestVO;
import oms.pc_protector.restApi.log.model.LogVO;
import oms.pc_protector.restApi.log.service.LogService;
import oms.pc_protector.restApi.result.service.ResultService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Log4j2
@CrossOrigin
@RestController
@RequestMapping(value = "v1/log")
public class LogController {
    private final ResponseService responseService;

    private final LogService logService;

    public LogController(ResponseService responseService,
                         ResultService resultService, LogService logService) {
        this.responseService = responseService;
        this.logService = logService;
    }

    @GetMapping(value = "")
    public SingleResult<?> getAllLog() {
        List<?> result = logService.getAllLog();
        return responseService.getSingleResult(result);
    }

    @GetMapping(value = "/search")
    public SingleResult<?> findByItems(
            @RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "ip", required = false) String ipAddress,
            @RequestParam(value = "startDay", required = false) String startDay,
            @RequestParam(value = "endDay", required = false) String endDay) {
        HashMap<String, Object> map = new HashMap<>();
        LogRequestVO logRequestVO = new LogRequestVO();
        Optional.ofNullable(id).ifPresent(logRequestVO::setManagerId);
        Optional.ofNullable(ipAddress).ifPresent(logRequestVO::setIpAddress);
        Optional.ofNullable(startDay).ifPresent(logRequestVO::setStartDay);
        Optional.ofNullable(endDay).ifPresent(logRequestVO::setEndDay);
        List<?> list = logService.search(logRequestVO);
        map.put("results", list);
        return responseService.getSingleResult(map);
    }
}
