package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.User;
import com.utils.RespBean;
import com.mapper.TUserMapper;
import com.service.TUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 */
@Service
public class TUserServiceImpl extends ServiceImpl<TUserMapper, User> implements TUserService {

    @Override
    public RespBean deleteUserById(Long id) {
        RespBean respBean = RespBean.build();
        User user = baseMapper.selectById(id);
        if(user.getType() == 1){
            QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
            queryWrapper.eq("type",1);
            List<User> list = baseMapper.selectList(queryWrapper);
            if (list.size() == 1){
                respBean.setStatus(500);
                respBean.setMsg("删除失败，管理员角色过少，无法删除");
                return respBean;
            }
        }
        if (baseMapper.deleteById(id) == 1){
            respBean.setStatus(200);
            respBean.setMsg("删除成功");
            return respBean;
        }
        respBean.setMsg("删除失败");
        return respBean;
    }

    @Override
    public RespBean updateUser(User user) {
        RespBean respBean = RespBean.build();
        if (baseMapper.updateById(user) == 1){
            respBean.setObj(user);
            respBean.setMsg("修改成功");
            respBean.setStatus(200);
            return respBean;
        }
        respBean.setMsg("修改失败");
        return respBean;
    }

    @Override
    public RespBean getUserById(Long id) {
        RespBean respBean = RespBean.build();
        User user = baseMapper.selectById(id);
        respBean.setStatus(200);
        respBean.setObj(user);
        return respBean;
    }

    @Override
    public RespBean getAllUser() {
        RespBean respBean = RespBean.build();
        List<User> tUsers = baseMapper.getAllUser();
        respBean.setStatus(200);
        respBean.setObj(tUsers);
        return respBean;
    }

    @Override
    public void deleteShopId(Long id) {
        baseMapper.deleteShopId(id);
    }
}
