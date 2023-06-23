package com.mapper;

import com.entity.WordNetwork;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 单词网络表 Mapper 接口
 * </p>
 *
 * @author
 * @since 2023-04-21
 */
public interface WordNetworkMapper extends BaseMapper<WordNetwork> {

    @Select("SELECT\n" +
            "	DISTINCT ra.*\n" +
            "FROM\n" +
            "	word_network ra\n" +
            "WHERE\n" +
            "   ra.id != #{wordNetworkId}\n" +
            "   AND (\n" +
            "	ra.create_by = #{userId}\n" +
            "	OR ra.`status` = 2\n" +
            ")")
    List<WordNetwork> getByAllNetwordByUserId(@Param("wordNetworkId") Long wordNetworkId, @Param("userId") Long userId);
}
