package oms.pc_protector.restApi.process.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ProcessVO {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<ProcessVO> processVOList;

    private Long Idx;

    private String displayName;

    private String registryItem;

    private String type = "none";

}
