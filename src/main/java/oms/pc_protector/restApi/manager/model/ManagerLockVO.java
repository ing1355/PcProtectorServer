package oms.pc_protector.restApi.manager.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter
@Setter
public class ManagerLockVO {
    private String userId;
    private String password;
}
