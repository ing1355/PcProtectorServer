package oms.pc_protector.restApi.department.mapper;

import oms.pc_protector.restApi.department.model.DepartmentVO;
import oms.pc_protector.restApi.department.model.UpdateDepartmentVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentMapper {

    public List<DepartmentVO> selectDepartmentAll();

    public void registerDepartmentByExcel(DepartmentVO departmentVO);
    public void deleteDepartmentAll();

    public void insertDepartment(DepartmentVO departmentVO);

    public void updateDepartment(UpdateDepartmentVO updateDepartmentVO);

    public void deleteDepartment(int departmentIdx);

}
