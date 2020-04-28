package oms.pc_protector.restApi.log.controller;

import lombok.extern.log4j.Log4j2;
import oms.pc_protector.apiConfig.service.ResponseService;
import oms.pc_protector.restApi.process.service.ProcessService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@CrossOrigin
@RestController
@RequestMapping(value = "/log")
public class LogController {
//    private final ResponseService responseService;
//
//    private final LogService logService;
//
//    public LogController(ResponseService responseService,
//                             LogService logService) {
//        this.responseService = responseService;
//        this.logService = logService;
//    }
}
