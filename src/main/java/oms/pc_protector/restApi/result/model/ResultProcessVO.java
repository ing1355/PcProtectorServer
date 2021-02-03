package oms.pc_protector.restApi.result.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ResultProcessVO {

    private String userId;
    private String ipAddress;
    private String type;
    private String processName;
    private String checkTime;
    private String idx;
}
