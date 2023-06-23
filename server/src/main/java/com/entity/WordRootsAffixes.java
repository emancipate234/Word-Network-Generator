package com.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 词根词缀表
 * </p>
 *
 * @author
 * @since 2023-04-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="WordRootsAffixes对象", description="词根词缀表")
public class WordRootsAffixes implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "单词")
    private Long wordId;

    @ApiModelProperty(value = "词根")
    private String root;

    @ApiModelProperty(value = "词缀")
    private String affixe;

    @ApiModelProperty(value = "是否公开：1：否；2：是")
    private Integer isOpen;

    @ApiModelProperty(value = "状态：1：待审核；2：审核通过；3：审核不通过")
    private Integer status;

    @ApiModelProperty(value = "创建人")
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @TableField(exist = false)
    private User user;

    @TableField(exist = false)
    private Word word;

    @TableField(exist = false)
    private List<Word> rootList;

    @TableField(exist = false)
    private List<Word> affixeList;
}
