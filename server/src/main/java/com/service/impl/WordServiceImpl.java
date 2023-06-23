package com.service.impl;

import com.entity.Word;
import com.mapper.WordMapper;
import com.service.WordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 单词表 服务实现类
 * </p>
 *
 * @author
 * @since 2023-04-21
 */
@Service
public class WordServiceImpl extends ServiceImpl<WordMapper, Word> implements WordService {

    @Override
    public List<Word> getWord(Long number, Long userId) {
        return baseMapper.getWord(number, userId);
    }
}
