package oms.pc_protector.restApi.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserRequestVO {

    @NotNull(message = "아이디가 null입니다.")
    private String userId;

    @NotNull(message = "이름이 null입니다.")
    private String name;

    private String ipAddress;
    private String email;
    private String phone;
    private String department;
    private int departmentIdx;

    @JsonIgnore
    private String startDay;

    @JsonIgnore
    private String endDay;

}
