package com.vo;

import lombok.Data;

@Data
public class PasswordVo {
    private Long id;
    private String oldPassword;
    private String newPassword;
}
