package oms.pc_protector.restApi.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseVO {

    private String userId;
    private String department;
    private int department_idx;
    private int score;
    private String email;
    private String phone;
    private String ipAddress;
    private String macAddress;
    private String pcName;
    private String pcProtectorVersion;
    private String vaccineVersion;
    private String check_time;
}
