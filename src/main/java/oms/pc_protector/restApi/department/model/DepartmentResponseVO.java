package oms.pc_protector.restApi.department.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentResponseVO {
    private Integer idx;
    private String dptCode;

    public DepartmentResponseVO(Integer idx, String dptCode) {
        this.idx = idx;
        this.dptCode = dptCode;
    }
}
