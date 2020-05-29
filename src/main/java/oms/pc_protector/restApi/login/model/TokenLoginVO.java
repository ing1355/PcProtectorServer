package oms.pc_protector.restApi.login.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class TokenLoginVO {
    @NotNull(message = "아이디를 입력하세요.")
    @NotBlank(message = "아이디를 입력하세요.")
    @Size(min = 4, max = 16, message = "아이디는 최소 4자리 최대 16자리 이하입니다.")
    private String id;

    @NotBlank(message = "비밀번호를 입력하세요.")
    private String password;

    private String ipAddress;
}
