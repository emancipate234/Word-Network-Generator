package com.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 单词表
 * </p>
 *
 * @author 
 * @since 2023-04-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Word对象", description="单词表")
public class Word implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "单词")
    private String name;

    @ApiModelProperty(value = "读音")
    private String phonetic;

    @ApiModelProperty(value = "翻译")
    private String translate;

    @ApiModelProperty(value = "变形")
    private String distortion;

    @ApiModelProperty(value = "短语")
    private String phrase;

    @ApiModelProperty(value = "例句")
    private String samples;


}
