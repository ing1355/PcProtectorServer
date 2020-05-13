package oms.pc_protector.restApi.result.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchInputVO {

    private String userId;
    private String name;
    private String ipAddress;
    private String startDate;
    private String endDate;
    private Integer departmentCode;

}
