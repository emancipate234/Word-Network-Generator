package com.vo;

import lombok.Data;

@Data
public class UserVo {
    private Long current;
    private Long size;
    private String nickname;
    private Integer type;
}
