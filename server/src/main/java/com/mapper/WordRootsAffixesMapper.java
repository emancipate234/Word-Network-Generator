package com.mapper;

import com.entity.Word;
import com.entity.WordRootsAffixes;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 词根词缀表 Mapper 接口
 * </p>
 *
 * @author
 * @since 2023-04-21
 */
public interface WordRootsAffixesMapper extends BaseMapper<WordRootsAffixes> {


    @Select("SELECT\n" +
            "	DISTINCT w.*\n" +
            "FROM\n" +
            "	word_roots_affixes ra\n" +
            "LEFT JOIN word w ON ra.word_id = w.id\n" +
            "WHERE\n" +
            "	ra.root = #{root}\n" +
            "AND ra.word_id != #{wordId}\n" +
            "AND (\n" +
            "	ra.create_by = #{userId}\n" +
            "	OR ra.`status` = 2\n" +
            ")\n" +
            "ORDER BY\n" +
            "	w.`name` ASC")
    List<Word> getRootList(@Param("root") String root, @Param("wordId") Long wordId, @Param("userId") Long userId);

    @Select("SELECT\n" +
            "	DISTINCT w.*\n" +
            "FROM\n" +
            "	word_roots_affixes ra\n" +
            "LEFT JOIN word w ON ra.word_id = w.id\n" +
            "WHERE\n" +
            "	ra.affixe = #{affixe}\n" +
            "AND ra.word_id != #{wordId}\n" +
            "AND (\n" +
            "	ra.create_by = #{userId}\n" +
            "	OR ra.`status` = 2\n" +
            ")\n" +
            "ORDER BY\n" +
            "	w.`name` ASC")
    List<Word> getAffixeList(@Param("affixe") String affixe, @Param("wordId") Long wordId, @Param("userId") Long userId);
}
