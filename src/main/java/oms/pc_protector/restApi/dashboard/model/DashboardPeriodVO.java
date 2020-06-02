package oms.pc_protector.restApi.dashboard.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashboardPeriodVO {
    private String startDate;
    private String endDate;

    public DashboardPeriodVO(String format, String format1) {
        this.startDate = format;
        this.endDate = format1;
    }
}
