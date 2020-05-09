package oms.pc_protector.restApi.department.service;

import oms.pc_protector.restApi.department.mapper.DepartmentMapper;
import oms.pc_protector.restApi.department.model.DepartmentVO;
import oms.pc_protector.restApi.department.model.UpdateDepartmentVO;
import oms.pc_protector.restApi.user.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentMapper departmentMapper;
    private final UserMapper userMapper;

    public DepartmentService(DepartmentMapper departmentMapper, UserMapper userMapper) {
        this.departmentMapper = departmentMapper;
        this.userMapper = userMapper;
    }

    @Transactional
    public List<DepartmentVO> findAll() {
        return departmentMapper.selectDepartmentAll();
    }

    @Transactional
    public void registerDepartmentByExcel(List<DepartmentVO> departmentVO) {
        departmentMapper.deleteDepartmentAll();
        for (DepartmentVO dept : departmentVO) {
            departmentMapper.registerDepartmentByExcel(dept);
        }
    }

    @Transactional
    public void insertDepartment(DepartmentVO departmentVO) {
        departmentMapper.insertDepartment(departmentVO);
    }

    @Transactional
    public void updateDepartment(UpdateDepartmentVO updateDepartmentVO) {
        departmentMapper.updateDepartment(updateDepartmentVO);
        userMapper.departmentModified(updateDepartmentVO);
    }

    @Transactional
    public void register() {

    }

    @Transactional
    public void update() {

    }

    @Transactional
    public void delete(String name) {
        departmentMapper.deleteDepartment(name);
    }


}
