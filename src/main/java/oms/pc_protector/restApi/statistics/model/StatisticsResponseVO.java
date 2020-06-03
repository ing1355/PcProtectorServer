package oms.pc_protector.restApi.statistics.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StatisticsResponseVO {

    private Long departmentCode;
    private String yearMonth;
}
