package oms.pc_protector.restApi.manager.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestManagerVO {
    private String old_id;
    private ManagerVO new_data;
}
