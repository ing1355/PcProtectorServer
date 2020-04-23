package oms.pc_protector.restApi.clientFile.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Getter
@Setter
public class ClientFileVO {

    private int idx;
    private String version;
    private String fileName;
    private String fileSize;
    private String md5;
    private LocalDate createTime;
    private LocalDate updateTime;

}
