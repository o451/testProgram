package com.daiming.test.program.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UserToken {

    /**
     * 用户登陆token
     */
    @NotBlank
    @Size(min = 0, max = 64)
    private String token;
}
