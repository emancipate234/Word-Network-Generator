package com.service;

import com.entity.Word;
import com.entity.WordRootsAffixes;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 词根词缀表 服务类
 * </p>
 *
 * @author
 * @since 2023-04-21
 */
public interface WordRootsAffixesService extends IService<WordRootsAffixes> {

    List<Word> getRootList(String root, Long wordId, Long userId);

    List<Word> getAffixeList(String affixe, Long wordId, Long userId);
}
