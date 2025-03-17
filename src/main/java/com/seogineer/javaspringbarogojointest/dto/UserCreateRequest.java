package com.seogineer.javaspringbarogojointest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class UserCreateRequest {

    @NotBlank(message = "ID는 필수 입력값입니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Size(min = 12, message = "비밀번호는 최소 12자 이상이어야 합니다.")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])|(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*])|(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])|(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*]).{12,}$",
        message = "비밀번호는 영어 대문자, 소문자, 숫자, 특수문자 중 3가지 이상을 포함해야 합니다."
    )
    private String password;

    @NotBlank(message = "사용자 이름은 필수 입력값입니다.")
    private String name;

}
