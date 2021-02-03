package oms.pc_protector.restApi.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSearchInputVO {

    private String userId;
    private String name;
    private String phone;
    private Long departmentIdx;
    private String UserIdx;
}
