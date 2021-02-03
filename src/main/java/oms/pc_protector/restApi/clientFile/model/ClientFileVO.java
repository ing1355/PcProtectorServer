package oms.pc_protector.restApi.clientFile.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ClientFileVO {

    private String fileName;
    private Long fileSize;
    private String md5;
    private String updateTime;
    private String version;
    private String[] b64temp;
    private String idx;
}
