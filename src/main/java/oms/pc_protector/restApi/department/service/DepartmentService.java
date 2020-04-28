package oms.pc_protector.restApi.department.service;

import oms.pc_protector.restApi.department.mapper.DepartmentMapper;
import oms.pc_protector.restApi.department.model.DepartmentVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentMapper departmentMapper;

    public DepartmentService(DepartmentMapper departmentMapper) {
        this.departmentMapper = departmentMapper;
    }


    public List<DepartmentVO> findAll() {
        return departmentMapper.selectDepartmentAll();
    }


    public void register() {

    }


    public void update() {

    }


    public void delete() {

    }


}
