package com.service;

import com.entity.Word;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 单词表 服务类
 * </p>
 *
 * @author
 * @since 2023-04-21
 */
public interface WordService extends IService<Word> {

    List<Word> getWord(Long number, Long userId);
}
