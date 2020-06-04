package oms.pc_protector.restApi.login.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientLoginVO {
    private String id;
    private String ipAddress;

    public ClientLoginVO(String userId, String ipAddress) {
        this.id = userId;
        this.ipAddress = ipAddress;
    }
}
