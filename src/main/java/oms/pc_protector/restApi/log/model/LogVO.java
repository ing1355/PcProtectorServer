package oms.pc_protector.restApi.log.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class LogVO {

    private String managerId;
    private String uri;
    private String method;
    private String ipAddress;

}
