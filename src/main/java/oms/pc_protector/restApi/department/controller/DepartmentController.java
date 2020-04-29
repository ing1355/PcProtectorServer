package oms.pc_protector.restApi.department.controller;

import lombok.extern.log4j.Log4j2;
import oms.pc_protector.apiConfig.model.SingleResult;
import oms.pc_protector.apiConfig.service.ResponseService;
import oms.pc_protector.restApi.department.model.DepartmentVO;
import oms.pc_protector.restApi.department.service.DepartmentService;
import oms.pc_protector.restApi.user.model.UserVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Log4j2
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

    @PostMapping(value = "/excel")
    public SingleResult<?> registerDepartmentByExcel(@RequestBody @Valid List<DepartmentVO> departmentVOList) {
        departmentService.registerDepartmentByExcel(departmentVOList);
        return responseService.getSingleResult(true);
    }

    @PostMapping(value = "")
    public SingleResult<?> insertDepartment(@RequestBody @Valid DepartmentVO departmentVO) {
        departmentService.insertDepartment(departmentVO);
        return responseService.getSingleResult(true);
    }
}
