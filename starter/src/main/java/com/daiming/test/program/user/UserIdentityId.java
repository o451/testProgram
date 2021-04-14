package com.daiming.test.program.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UserIdentityId {

    /**
     * 用户ID
     */
    @NotBlank
    @Size(min = 0, max = 64)
    private String id;
}
