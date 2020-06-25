package oms.pc_protector.restApi.process.controller;


import lombok.extern.log4j.Log4j2;
import oms.pc_protector.apiConfig.model.SingleResult;
import oms.pc_protector.apiConfig.service.ResponseService;
import oms.pc_protector.restApi.process.model.ProcessVO;
import oms.pc_protector.restApi.process.service.ProcessService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Log4j2
@CrossOrigin
@RestController
@RequestMapping(value = "v1/process")
public class ProcessController {

    private final ResponseService responseService;

    private final ProcessService processService;

    public ProcessController(ResponseService responseService,
                             ProcessService processService) {
        this.responseService = responseService;
        this.processService = processService;
    }


    @GetMapping(value = "")
    public SingleResult<?> findProcessAll() {
        List<ProcessVO> processList = processService.findProcessAll();
        return responseService.getSingleResult(processList);
    }

    @GetMapping(value = "/search")
    public SingleResult<?> searchProcess(@RequestParam(value = "displayName", required = false) String displayName,
                                         @RequestParam(value = "registryName", required = false) String registryName) {
        List<ProcessVO> processList = processService.searchProcess(displayName, registryName);
        return responseService.getSingleResult(processList);
    }

    @PostMapping(value = "/insert")
    public SingleResult<?> insertProcessDirect(@RequestBody @Valid ProcessVO processVO) {
        processService.insertProcessDirect(processVO);
        return responseService.getSingleResult(processService.findProcessAll());
    }

    @PutMapping(value = "modify-unapproved-process")
    public SingleResult<?> modifyToUnApprovedProcess(@RequestBody @Valid Long idx) {
        processService.modifyToUnApprovedProcess(idx);
        return responseService.getSingleResult(processService.findProcessAll());
    }

    @PutMapping(value = "modify-required-process")
    public SingleResult<?> modifyToRequiredProcess(@RequestBody @Valid Long idx) {
        processService.modifyToRequiredProcess(idx);
        return responseService.getSingleResult(processService.findProcessAll());
    }

    //////////////////////////////// 비인가 프로그램 ////////////////////////////////////////

    @GetMapping(value = "/unapproved-process")
    public SingleResult<?> findUnApprovedProcess() {
        List<ProcessVO> unApprovedProcessList = processService.findUnApprovedProcessList();
        return responseService.getSingleResult(unApprovedProcessList);
    }

    @PostMapping(value = "unapproved-process/insert")
    public SingleResult<?> insertUnApprovedProcess(@RequestBody @Valid ProcessVO processVO) {
        int resultNum = processService.insertUnApprovedProcess(processVO);
        return responseService.getSingleResult(resultNum);
    }

    @PutMapping(value = "unapproved-process/modify")
    public SingleResult<?> modifyUnApprovedProcess(@RequestBody @Valid ProcessVO processVO) {
        processService.updateProcess(processVO);
        return responseService.getSingleResult(processService.findUnApprovedProcessList());
    }

    @PutMapping(value = "/unapproved-process/delete")
    public SingleResult<?> deleteUnApprovedProcess(@RequestBody @Valid ProcessVO processVO) {
        processService.deleteProcess(processVO);
        return responseService.getSingleResult(processService.findUnApprovedProcessList());
    }

    //////////////////////////////// 필수 프로그램 ////////////////////////////////////////

    @GetMapping(value = "/required-process")
    public SingleResult<?> findRequiredProcess() {
        List<ProcessVO> requiredProcessList = processService.findRequiredProcessList();
        return responseService.getSingleResult(requiredProcessList);
    }

    @PostMapping(value = "required-process/insert")
    public SingleResult<?> insertRequiredProcess(@RequestBody @Valid ProcessVO processVO) {
        int resultNum = processService.insertRequiredProcess(processVO);
        return responseService.getSingleResult(resultNum);
    }

    @PutMapping(value = "/required-process/modify")
    public SingleResult<?> modifyRequiredProcess(@RequestBody @Valid ProcessVO processVO) {
        processService.updateProcess(processVO);
        return responseService.getSingleResult(processService.findRequiredProcessList());
    }

    @PutMapping(value = "/required-process/delete")
    public SingleResult<?> deleteRequiredProcess(@RequestBody @Valid ProcessVO processVO) {
        processService.deleteProcess(processVO);
        return responseService.getSingleResult(processService.findRequiredProcessList());
    }
}
