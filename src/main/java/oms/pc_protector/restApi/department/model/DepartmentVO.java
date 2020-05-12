package oms.pc_protector.restApi.department.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentVO {

    private String name;
    private int code;
    private int parentCode;
}
