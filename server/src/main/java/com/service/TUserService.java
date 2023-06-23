package com.service;

import com.entity.User;
import com.utils.RespBean;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 */

public interface TUserService extends IService<User>  {

    RespBean deleteUserById(Long id);

    RespBean updateUser(User user);

    RespBean getUserById(Long id);

    RespBean getAllUser();

    void deleteShopId(Long id);
}
