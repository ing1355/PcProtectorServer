package oms.pc_protector.restApi.policy.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditProgramVO {

    private boolean msWord;
    private boolean msPowerPoint;
    private boolean msExcel;
    private boolean hwp;
    private boolean pdf;
    private String departmentIdx;
}

