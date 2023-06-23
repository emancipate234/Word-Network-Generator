package com.vo;

import lombok.Data;

@Data
public class CommentVo {
    private Long current;
    private Long size;
    private Integer status;
    private String isMy;
    private Integer type;
    private String name;
    private String translate;
    private String content;
    private String root;
    private String affixe;
}
