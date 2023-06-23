package com.mapper;

import com.entity.Word;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 单词表 Mapper 接口
 * </p>
 *
 * @author
 * @since 2023-04-21
 */
public interface WordMapper extends BaseMapper<Word> {

    @Select("SELECT\n" +
            "	w.*, (\n" +
            "		POWER(2, m.count) * 43200 + m.`interval_num`\n" +
            "	) AS sort\n" +
            "FROM\n" +
            "	word w\n" +
            "LEFT JOIN word_memory m ON w.id = m.word_id\n" +
            "AND m.create_by = #{userId}\n" +
            "ORDER BY\n" +
            "	sort ASC\n" +
            "LIMIT #{number}")
    List<Word> getWord(@Param("number") Long number, @Param("userId") Long userId);
}
