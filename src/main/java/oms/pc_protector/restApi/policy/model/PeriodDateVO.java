package oms.pc_protector.restApi.policy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PeriodDateVO {

    private Long idx;
    private int period;
    private int fromWeek;
    private int fromDay;
    private int toWeek;
    private int toDay;
    private boolean apply;
}
