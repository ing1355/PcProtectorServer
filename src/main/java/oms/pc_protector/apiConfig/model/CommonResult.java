package oms.pc_protector.apiConfig.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResult {     // API 공통 Response 양식

    private boolean success;
    private int status;
    private String msg;
}
