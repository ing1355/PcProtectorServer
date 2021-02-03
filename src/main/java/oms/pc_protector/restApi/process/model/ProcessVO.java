package oms.pc_protector.restApi.process.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

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

    private String departmentIdx;

    public ProcessVO(String displayName, String registryItem) {
        this.displayName = displayName;
        this.registryItem = registryItem;
    }
}
