package oms.pc_protector.restApi.statistics.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatisticsResponseVO {

    private String departmentIdx;
    private String yearMonth;

    public StatisticsResponseVO(String yearMonth, String departmentIdx) {
        this.departmentIdx = departmentIdx;
        this.yearMonth = yearMonth;
    }
}
