package oms.pc_protector.restApi.department.mapper;

import oms.pc_protector.restApi.department.model.DepartmentVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentMapper {

    public List<DepartmentVO> selectDepartmentAll();

    public void insertDepartment(DepartmentVO departmentVO);

    public void updateDepartment(DepartmentVO departmentVO);

    public void deleteDepartment(int departmentIdx);

}
