package oms.pc_protector.restApi.department.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentVO {

    private String name;
    private Long code;
    private Long parentCode;
    private String idx;
    private String dptCode;
}
