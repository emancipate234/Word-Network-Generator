package com.service.impl;

import com.entity.Word;
import com.entity.WordRootsAffixes;
import com.mapper.WordRootsAffixesMapper;
import com.service.WordRootsAffixesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 词根词缀表 服务实现类
 * </p>
 *
 * @author
 * @since 2023-04-21
 */
@Service
public class WordRootsAffixesServiceImpl extends ServiceImpl<WordRootsAffixesMapper, WordRootsAffixes> implements WordRootsAffixesService {

    @Override
    public List<Word> getRootList(String root, Long wordId, Long userId) {
        return baseMapper.getRootList(root, wordId, userId);
    }

    @Override
    public List<Word> getAffixeList(String affixe, Long wordId, Long userId) {
        return baseMapper.getAffixeList(affixe, wordId, userId);
    }
}
