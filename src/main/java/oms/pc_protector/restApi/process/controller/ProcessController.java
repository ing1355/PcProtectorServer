package oms.pc_protector.restApi.process.controller;


import lombok.extern.slf4j.Slf4j;
import oms.pc_protector.apiConfig.model.SingleResult;
import oms.pc_protector.apiConfig.service.ResponseService;
import oms.pc_protector.restApi.process.model.ProcessVO;
import oms.pc_protector.restApi.process.service.ProcessService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Slf4j
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
    public SingleResult<?> findProcessAll(HttpServletRequest httpServletRequest) {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        List<ProcessVO> processList = processService.findProcessAll(User_Idx);
        return responseService.getSingleResult(processList);
    }

    @GetMapping(value = "/search")
    public SingleResult<?> searchProcess(@RequestParam(value = "displayName", required = false) String displayName,
                                         @RequestParam(value = "registryName", required = false) String registryName,
                                         HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        List<ProcessVO> processList = processService.searchProcess(displayName, registryName, User_Idx);
        return responseService.getSingleResult(processList);
    }

    @PostMapping(value = "/insert")
    public SingleResult<?> insertProcessDirect(@RequestBody @Valid ProcessVO processVO,
                                               HttpServletRequest httpServletRequest,
                                               HttpServletResponse response) throws IOException {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        processVO.setDepartmentIdx(User_Idx);
        if (processService.existProcess(processVO) != null) {
            response.sendError(400, "중복된 프로세스입니다.");
            return null;
        }
        processService.insertProcessDirect(processVO);
        return responseService.getSingleResult(processService.findProcessAll(User_Idx));
    }

    @PutMapping(value = "modify-unapproved-process")
    public SingleResult<?> modifyToUnApprovedProcess(@RequestBody @Valid Long idx,
                                                     HttpServletRequest httpServletRequest) {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        processService.modifyToUnApprovedProcess(idx);
        return responseService.getSingleResult(processService.findProcessAll(User_Idx));
    }

    @PutMapping(value = "modify-required-process")
    public SingleResult<?> modifyToRequiredProcess(@RequestBody @Valid Long idx,
                                                   HttpServletRequest httpServletRequest) {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        processService.modifyToRequiredProcess(idx);
        return responseService.getSingleResult(processService.findProcessAll(User_Idx));
    }

    //////////////////////////////// 비인가 프로그램 ////////////////////////////////////////

    @GetMapping(value = "/unapproved-process")
    public SingleResult<?> findUnApprovedProcess(HttpServletRequest httpServletRequest) {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        List<ProcessVO> unApprovedProcessList = processService.findUnApprovedProcessList(User_Idx);
        return responseService.getSingleResult(unApprovedProcessList);
    }

    @PostMapping(value = "unapproved-process/insert")
    public SingleResult<?> insertUnApprovedProcess(@RequestBody @Valid ProcessVO processVO, HttpServletResponse response,
                                                   HttpServletRequest httpServletRequest) throws IOException {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        processVO.setDepartmentIdx(User_Idx);
        ProcessVO isProcess = processService.existProcess(processVO);
        if (isProcess != null && isProcess.getType().equals("unApproved")) {
            response.sendError(400, "중복된 프로세스입니다.");
            return null;
        } else if (isProcess != null) {
            processService.modifyToUnApprovedProcess(isProcess.getIdx());
            return responseService.getSingleResult(processService.findUnApprovedProcessList(User_Idx));
        }
        return responseService.getSingleResult(processService.insertUnApprovedProcess(processVO));
    }

    @PutMapping(value = "unapproved-process/modify")
    public SingleResult<?> modifyUnApprovedProcess(@RequestBody @Valid ProcessVO processVO,
                                                   HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        processService.updateProcess(processVO);
        return responseService.getSingleResult(processService.findUnApprovedProcessList(User_Idx));
    }

    @PutMapping(value = "/unapproved-process/delete")
    public SingleResult<?> deleteUnApprovedProcess(@RequestBody @Valid ProcessVO processVO,
                                                   HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        processService.deleteProcess(processVO);
        return responseService.getSingleResult(processService.findUnApprovedProcessList(User_Idx));
    }

    //////////////////////////////// 필수 프로그램 ////////////////////////////////////////

    @GetMapping(value = "/required-process")
    public SingleResult<?> findRequiredProcess(HttpServletRequest httpServletRequest) {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        List<ProcessVO> requiredProcessList = processService.findRequiredProcessList(User_Idx);
        return responseService.getSingleResult(requiredProcessList);
    }

    @PostMapping(value = "required-process/insert")
    public SingleResult<?> insertRequiredProcess(@RequestBody @Valid ProcessVO processVO, HttpServletResponse response,
                                                 HttpServletRequest httpServletRequest) throws IOException {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        ProcessVO isProcess = processService.existProcess(processVO);
        if (isProcess != null && isProcess.getType().equals("required")) {
            response.sendError(400, "중복된 프로세스입니다.");
            return null;
        } else if (isProcess != null) {
            processService.modifyToRequiredProcess(isProcess.getIdx());
            return responseService.getSingleResult(processService.findRequiredProcessList(User_Idx));
        }
        return responseService.getSingleResult(processService.insertRequiredProcess(processVO));
    }

    @PutMapping(value = "/required-process/modify")
    public SingleResult<?> modifyRequiredProcess(@RequestBody @Valid ProcessVO processVO,
                                                 HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        processService.updateProcess(processVO);
        return responseService.getSingleResult(processService.findRequiredProcessList(User_Idx));
    }

    @PutMapping(value = "/required-process/delete")
    public SingleResult<?> deleteRequiredProcess(@RequestBody @Valid ProcessVO processVO,
                                                 HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        processService.deleteProcess(processVO);
        return responseService.getSingleResult(processService.findRequiredProcessList(User_Idx));
    }
}
