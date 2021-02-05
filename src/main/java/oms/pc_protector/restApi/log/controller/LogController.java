package oms.pc_protector.restApi.log.controller;

import lombok.extern.slf4j.Slf4j;
import oms.pc_protector.apiConfig.model.SingleResult;
import oms.pc_protector.apiConfig.service.ResponseService;
import oms.pc_protector.restApi.department.mapper.DepartmentMapper;
import oms.pc_protector.restApi.log.model.LogRequestVO;
import oms.pc_protector.restApi.log.service.LogService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "v1/log")
public class LogController {

    private final ResponseService responseService;
    private final DepartmentMapper departmentMapper;
    private final LogService logService;

    public LogController(ResponseService responseService,
                         LogService logService,
                         DepartmentMapper departmentMapper) {
        this.departmentMapper = departmentMapper;
        this.responseService = responseService;
        this.logService = logService;
    }

    @GetMapping(value = "")
    public SingleResult<?> getAllLog(HttpServletRequest httpServletRequest) {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        List<?> result = logService.getAllLog(User_Idx);
        return responseService.getSingleResult(result);
    }

    @GetMapping(value = "/search")
    public SingleResult<?> findByItems(
            @RequestParam(value = "userId", required = false) String userId,
            @RequestParam(value = "ipAddress", required = false) String ipAddress,
            @RequestParam(value = "startDay", required = false) String startDay,
            @RequestParam(value = "endDay", required = false) String endDay,
            HttpServletRequest httpServletRequest) throws ParseException {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        HashMap<String, Object> map = new HashMap<>();
        LogRequestVO logRequestVO = new LogRequestVO();
        logRequestVO.setDepartmentIdx(User_Idx);
        Optional.ofNullable(userId).ifPresent(logRequestVO::setManagerId);
        Optional.ofNullable(ipAddress).ifPresent(logRequestVO::setIpAddress);
        Optional.ofNullable(startDay).ifPresent(logRequestVO::setStartDay);
        Optional.ofNullable(endDay).ifPresent(logRequestVO::setEndDay);
        List<?> list = logService.search(logRequestVO);
        return responseService.getSingleResult(list);
    }
}
