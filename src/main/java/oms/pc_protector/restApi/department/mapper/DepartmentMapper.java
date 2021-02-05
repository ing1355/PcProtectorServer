package oms.pc_protector.restApi.department.mapper;

import oms.pc_protector.restApi.department.model.DepartmentVO;
import oms.pc_protector.restApi.department.model.UpdateDepartmentVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentMapper {

    public List<DepartmentVO> selectAll(String departmentIdx);

    public Integer findUserInDepartment(String code);

    public Integer findManagerInDepartment(String code);

    public String selectCodeByIdx(String departmentIdx);

    public Integer findDepartmentCode(String code);

    public DepartmentVO selectByDepartment(String department);

    public DepartmentVO selectBycode(Long departmentIdx);

    public DepartmentVO selectByDptCode(String code);

    public List<DepartmentVO> selectChildCodeDescByParentCode(Long parentCode);

    public List<DepartmentVO> selectChildCodeAscByParentCode(Long parentCode);

    public void registerByExcel(DepartmentVO departmentVO);

    public void deleteAll(String idx);

    public void changeDepartment(DepartmentVO departmentVO);

    public String insert(DepartmentVO departmentVO);

    public String insertRoot(DepartmentVO departmentVO);

    public void update(UpdateDepartmentVO updateDepartmentVO);

    public void deleteRoot(String departmentIdx);

    public void delete(String departmentIdx);
}
