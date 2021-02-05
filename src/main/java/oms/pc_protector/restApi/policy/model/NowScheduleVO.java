package oms.pc_protector.restApi.policy.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NowScheduleVO {
    private String startDate;
    private String endDate;
    private String departmentIdx;

    public NowScheduleVO(String format, String format1, String format2) {
        this.startDate = format;
        this.endDate = format1;
        this.departmentIdx = format2;
    }
}
