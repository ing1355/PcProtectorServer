package oms.pc_protector.restApi.department.mapper;

import oms.pc_protector.restApi.department.model.DepartmentVO;
import oms.pc_protector.restApi.department.model.UpdateDepartmentVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentMapper {

    public List<DepartmentVO> selectAll();

    public DepartmentVO selectByDepartment(String department);

    public DepartmentVO selectBycode(int departmentCode);

    public List<DepartmentVO> selectChildCodeByParentCode(int parentCode);

    public void registerByExcel(DepartmentVO departmentVO);

    public void deleteAll();

    public void insert(DepartmentVO departmentVO);

    public void update(UpdateDepartmentVO updateDepartmentVO);

    public void deleteByDepartment(String department);

}
