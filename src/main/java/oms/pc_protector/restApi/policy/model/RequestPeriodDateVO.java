package oms.pc_protector.restApi.policy.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestPeriodDateVO {
    private PeriodDateVO old_data;
    private PeriodDateVO new_data;
}
