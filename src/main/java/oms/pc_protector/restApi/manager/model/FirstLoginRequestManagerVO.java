package oms.pc_protector.restApi.manager.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FirstLoginRequestManagerVO {
    private String userId;
    private String password;
    private String departmentIdx;
}
