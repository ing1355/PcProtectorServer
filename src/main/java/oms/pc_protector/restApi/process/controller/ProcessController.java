package oms.pc_protector.restApi.process.controller;


import lombok.extern.log4j.Log4j2;
import oms.pc_protector.apiConfig.model.SingleResult;
import oms.pc_protector.apiConfig.service.ResponseService;
import oms.pc_protector.restApi.policy.model.ConfigurationVO;
import oms.pc_protector.restApi.policy.service.ConfigurationService;
import oms.pc_protector.restApi.process.model.ProcessVO;
import oms.pc_protector.restApi.process.service.ProcessService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Log4j2
@CrossOrigin
@RestController
@RequestMapping(value = "**/process")
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
        int resultNum = processService.insertProcessDirect(processVO);
        return responseService.getSingleResult(resultNum);
    }

    @PutMapping(value = "")
    public SingleResult<?> modifyProcess(@RequestBody @Valid ProcessVO processVO) {
        processService.modifyProcessList(processVO);
        return responseService.getSingleResult(true);
    }

    @DeleteMapping(value = "")
    public SingleResult<?> removeProcess(@RequestBody @Valid ProcessVO processVO) {
        processService.removeProcessList(processVO);
        return responseService.getSingleResult(true);
    }

    @PutMapping(value = "modify-unapproved-process")
    public void modifyToUnApprovedProcess(@RequestBody @Valid Long idx) {
        processService.modifyToUnApprovedProcess(idx);
    }

    @PutMapping(value = "modify-required-process")
    public void modifyToRequiredProcess(@RequestBody @Valid Long idx) {
        processService.modifyToRequiredProcess(idx);
    }

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

    @PostMapping(value = "/unapproved-process")
    public SingleResult<?> registerUnApprovedProcess(@RequestBody @Valid ProcessVO processVO) {
        int resultNum = processService.registerUnApprovedProcessList(processVO);
        boolean responseResult = resultNum > 0;
        return responseService.getSingleResult(responseResult);
    }

    @PutMapping(value = "unapproved-process/delete")
    public SingleResult<?> deleteUnApprovedProcess(@RequestBody @Valid ProcessVO processVO) {
        int resultNum = processService.deleteUnApprovedProcess(processVO);
        boolean responseResult = resultNum > 0;
        return responseService.getSingleResult(responseResult);
    }

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

    @PostMapping(value = "/required-process")
    public SingleResult<?> registerRequiredProcess(@RequestBody @Valid ProcessVO processVO) {
        int resultNum = processService.registerRequiredProcessList(processVO);
        boolean responseResult = resultNum > 0;
        return responseService.getSingleResult(responseResult);
    }

    @PutMapping(value = "/required-process/delete")
    public SingleResult<?> deleteRequiredProcess(@RequestBody @Valid ProcessVO processVO) {
        int resultNum = processService.deleteRequiredProcess(processVO);
        boolean responseResult = resultNum > 0;
        return responseService.getSingleResult(responseResult);
    }
}
