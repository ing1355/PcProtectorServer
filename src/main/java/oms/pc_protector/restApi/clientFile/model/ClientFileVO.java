package oms.pc_protector.restApi.clientFile.model;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

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
}
