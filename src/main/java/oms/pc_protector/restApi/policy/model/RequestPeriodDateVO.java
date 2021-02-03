package oms.pc_protector.restApi.policy.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestPeriodDateVO {
    private PeriodDateVO new_data;
    private String departmentIdx;
}
