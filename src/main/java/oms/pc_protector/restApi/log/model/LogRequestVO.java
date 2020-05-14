package oms.pc_protector.restApi.log.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class LogRequestVO {

    private String managerId;
    private String ipAddress;

    @JsonIgnore
    private String startDay;

    @JsonIgnore
    private String endDay;

}
