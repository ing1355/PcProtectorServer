package oms.pc_protector.restApi.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserVO {

    private Integer departmentCode;
    private int score;
    private String userId;
    private String name;
    private String email;
    private String phone;
    private String department;

}
