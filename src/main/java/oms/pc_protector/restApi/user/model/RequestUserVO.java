package oms.pc_protector.restApi.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestUserVO {
    private String old_id;
    private String userId;
    private String name;
    private String email;
    private String phone;
    private String department;
    private String departmentIdx;
    private String UserIdx;
    private String change;
}
