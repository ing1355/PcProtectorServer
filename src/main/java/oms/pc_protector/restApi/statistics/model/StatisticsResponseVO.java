package oms.pc_protector.restApi.statistics.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StatisticsResponseVO {

    private Long departmentIdx;
    private String yearMonth;

    public StatisticsResponseVO(String yearMonth, Long code) {
        this.departmentIdx = code;
        this.yearMonth = yearMonth;
    }
}
