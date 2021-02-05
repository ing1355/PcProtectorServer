package oms.pc_protector.restApi.department.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentResponseVO {
    private String idx;
    private String dptCode;

    public DepartmentResponseVO(String idx, String dptCode) {
        this.idx = idx;
        this.dptCode = dptCode;
    }
}
