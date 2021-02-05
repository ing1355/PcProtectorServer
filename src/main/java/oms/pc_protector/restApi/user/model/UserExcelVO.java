package oms.pc_protector.restApi.user.model;

import lombok.Getter;
import lombok.Setter;
import oms.pc_protector.restApi.department.model.DepartmentVO;

import java.util.List;

@Getter
@Setter
public class UserExcelVO {
    private List<UserVO> userList;
    private List<DepartmentVO> departmentList;
}
