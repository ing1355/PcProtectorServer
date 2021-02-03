package oms.pc_protector.restApi.log.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogRequestVO {

    private String managerId;
    private String ipAddress;
    private String departmentIdx;

    @JsonIgnore
    private String startDay;

    @JsonIgnore
    private String endDay;

}
