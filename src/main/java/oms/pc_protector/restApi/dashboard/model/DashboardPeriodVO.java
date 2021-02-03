package oms.pc_protector.restApi.dashboard.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashboardPeriodVO {
    private String startDate;
    private String endDate;
    private String departmentIdx;

    public DashboardPeriodVO(String format, String format1, String format2) {
        this.startDate = format;
        this.endDate = format1;
        this.departmentIdx = format2;
    }
}
