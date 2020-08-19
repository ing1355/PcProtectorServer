package oms.pc_protector.restApi.department.controller;

import lombok.extern.slf4j.Slf4j;
import oms.pc_protector.apiConfig.model.SingleResult;
import oms.pc_protector.apiConfig.service.ResponseService;
import oms.pc_protector.restApi.department.model.DepartmentVO;
import oms.pc_protector.restApi.department.model.UpdateDepartmentVO;
import oms.pc_protector.restApi.department.service.DepartmentService;
import oms.pc_protector.restApi.user.model.UserVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/v1/department")
public class DepartmentController {
    private final ResponseService responseService;
    private final DepartmentService departmentService;

    public DepartmentController(ResponseService responseService, DepartmentService departmentService) {
        this.responseService = responseService;
        this.departmentService = departmentService;
    }

    @GetMapping(value = "")
    public SingleResult<?> findDepartmentAll() {
        List<DepartmentVO> departmentVOList = departmentService.findAll();
        return responseService.getSingleResult(departmentVOList);
    }

    @GetMapping(value = "check")
    public SingleResult<?> findUserInDepartment(@RequestParam(value = "department") String department) {
        boolean result = departmentService.findUserInDepartment(department);
        return responseService.getSingleResult(result);
    }

    @PostMapping(value = "/excel")
    public SingleResult<?> registerDepartmentByExcel(@RequestBody @Valid List<DepartmentVO> departmentVOList) {
        departmentService.registerByExcel(departmentVOList);
        return responseService.getSingleResult(true);
    }

    @PostMapping(value = "")
    public SingleResult<?> insertDepartment(@RequestBody @Valid DepartmentVO departmentVO) {
        departmentService.register(departmentVO);
        return responseService.getSingleResult(true);
    }

    @PutMapping(value = "")
    public SingleResult<?> updateDepartment(@RequestBody @Valid UpdateDepartmentVO updateDepartmentVO) {
        departmentService.update(updateDepartmentVO);
        return responseService.getSingleResult(true);
    }

    @DeleteMapping(value = "")
    public SingleResult<?> deleteDepartment(@RequestParam @Valid String name) {
        departmentService.delete(name);
        return responseService.getSingleResult(true);
    }
}
