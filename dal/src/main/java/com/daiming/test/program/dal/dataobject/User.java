package com.daiming.test.program.dal.dataobject;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class User {

    /**
     * 用户first name
     */
    @Size(min = 0, max = 64)
    private String first;

    /**
     * 用户last name
     */
    @Size(min = 0, max = 64)
    private String last;

    /**
     * 用户email
     */
    @Size(min = 0, max = 128)
    @NotBlank
    private String email;

    /**
     * 用户密码
     */
    @Size(min = 0, max = 256)
    @NotBlank
    private String password;

    private String userId;
}
