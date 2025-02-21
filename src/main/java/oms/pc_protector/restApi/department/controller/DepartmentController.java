package oms.pc_protector.restApi.department.controller;

import lombok.extern.slf4j.Slf4j;
import oms.pc_protector.apiConfig.model.SingleResult;
import oms.pc_protector.apiConfig.service.ResponseService;
import oms.pc_protector.restApi.department.model.DepartmentDeleteVO;
import oms.pc_protector.restApi.department.model.DepartmentVO;
import oms.pc_protector.restApi.department.model.UpdateDepartmentVO;
import oms.pc_protector.restApi.department.service.DepartmentService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/v1/department")
public class DepartmentController {
    private final ResponseService responseService;
    private final DepartmentService departmentService;

    public DepartmentController(ResponseService responseService,
                                DepartmentService departmentService) {
        this.responseService = responseService;
        this.departmentService = departmentService;
    }

    @GetMapping(value = "")
    public SingleResult<?> findDepartmentAll(HttpServletRequest httpServletRequest,
                                             @RequestParam(value = "isAdmin", required = false) boolean isAdmin) {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        if (isAdmin) {
            return responseService.getSingleResult(departmentService.findAllRoot(User_Idx));
        } else {
            return responseService.getSingleResult(departmentService.findAll(User_Idx));
        }
    }

    @GetMapping(value = "check")
    public SingleResult<?> findUserInDepartment(@RequestParam(value = "departmentIdx") String departmentIdx) {
        boolean result = departmentService.findUserInDepartment(departmentIdx);
        return responseService.getSingleResult(result);
    }

    @PostMapping(value = "/change")
    public SingleResult<?> changeDepartment(@RequestBody @Valid List<DepartmentVO> departmentVOList,
                                            HttpServletRequest httpServletRequest) {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        departmentService.changeDepartment(departmentVOList, User_Idx);
        return responseService.getSingleResult(true);
    }

    @PostMapping(value = "/delete")
    public SingleResult<?> deleteDepartment(@RequestBody @Valid DepartmentDeleteVO departmentDeleteVO,
                                            HttpServletRequest httpServletRequest) {
        String User_Idx = httpServletRequest.getHeader("dptIdx");
        departmentService.delete(departmentDeleteVO, User_Idx);
        return responseService.getSingleResult(true);
    }

    @PostMapping(value = "")
    public SingleResult<?> insertDepartment(@RequestBody @Valid DepartmentVO departmentVO) {
        return responseService.getSingleResult(departmentService.register(departmentVO));
    }

    @PutMapping(value = "")
    public SingleResult<?> updateDepartment(@RequestBody @Valid UpdateDepartmentVO updateDepartmentVO) {
        departmentService.update(updateDepartmentVO);
        return responseService.getSingleResult(true);
    }
}
