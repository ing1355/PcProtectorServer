package oms.pc_protector.restApi.department.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDepartmentVO {
    private String old_name;
    private String new_name;
}
