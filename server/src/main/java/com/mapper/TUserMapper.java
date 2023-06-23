package com.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.entity.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 */
public interface TUserMapper extends BaseMapper<User> {

   @Select("select * from user where username=#{username} limit 1")
   User loadUserByUsername(String username);

   @Select("select * from user")
   List<User> getAllUser();

    @Select("update user set shop_id = null where shop_id = #{id}")
    void deleteShopId(Long id);
}
