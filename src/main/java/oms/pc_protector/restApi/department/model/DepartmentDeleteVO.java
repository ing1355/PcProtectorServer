package oms.pc_protector.restApi.department.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DepartmentDeleteVO {
    private String name;
    private Long code;
    private Long parentCode;
    private String idx;
    private String dptCode;
    private List<DepartmentVO> departmentVOList;
}
