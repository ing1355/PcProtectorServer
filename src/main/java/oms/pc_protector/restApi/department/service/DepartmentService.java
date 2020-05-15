package oms.pc_protector.restApi.department.service;

import oms.pc_protector.restApi.department.mapper.DepartmentMapper;
import oms.pc_protector.restApi.department.model.DepartmentVO;
import oms.pc_protector.restApi.department.model.UpdateDepartmentVO;
import oms.pc_protector.restApi.user.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

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
        return Optional.ofNullable(departmentMapper.selectAll())
                .orElseThrow(() -> new RuntimeException("값이 존재하지 않습니다."));
    }


    @Transactional
    public DepartmentVO findByDepartment(String department) {
        return Optional.ofNullable(departmentMapper.selectByDepartment(department))
                .orElseThrow(() -> new RuntimeException("값이 존재하지 않습니다."));
    }


    @Transactional
    public DepartmentVO findByDepartmentCode(Long departmentCode) {
       return Optional.ofNullable(departmentMapper.selectBycode(departmentCode))
                .orElseThrow(() -> new RuntimeException("값이 존재하지 않습니다."));
    }


    @Transactional
    public List<DepartmentVO> findChildAscByParentCode(Long parentCode) {
        return Optional.ofNullable(departmentMapper.selectChildCodeAscByParentCode(parentCode))
                .orElseThrow(() -> new RuntimeException("값이 존재하지 않습니다."));
    }


    @Transactional
    public List<DepartmentVO> findChildDescByParentCode(Long parentCode) {
        return Optional.ofNullable(departmentMapper.selectChildCodeDescByParentCode(parentCode))
                .orElseThrow(() -> new RuntimeException("값이 존재하지 않습니다."));
    }


    @Transactional
    public void registerByExcel(List<DepartmentVO> departmentVO) {
        departmentMapper.deleteAll();
        for (DepartmentVO dept : departmentVO) {
            departmentMapper.registerByExcel(dept);
        }
    }


    @Transactional
    public void register(DepartmentVO departmentVO) {
        departmentMapper.insert(departmentVO);
    }


    @Transactional
    public void update(UpdateDepartmentVO updateDepartmentVO) {
        departmentMapper.update(updateDepartmentVO);
        userMapper.departmentModified(updateDepartmentVO);
    }


    @Transactional
    public void delete(String name) {
        departmentMapper.deleteByDepartment(name);
    }


}
