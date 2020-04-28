package oms.pc_protector.restApi.log.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class LogRequestVO {

    private String userId;
    private String ipAddress;
    private String device;
    private String method;

    @JsonIgnore
    private String startDay;

    @JsonIgnore
    private String endDay;

}
