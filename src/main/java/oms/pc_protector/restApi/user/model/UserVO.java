package oms.pc_protector.restApi.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserVO {

    private String departmentIdx;
    private String userId;
    private String name;
    private String email;
    private String phone;
    private String department;
    private String change;
}
