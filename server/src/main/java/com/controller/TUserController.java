package com.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.User;
import com.utils.RespBean;
import com.service.TUserService;
import com.vo.PasswordVo;
import com.vo.UserVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/user")
public class TUserController {

    @Autowired
    TUserService tUserService;

    /**
     * 分页查询
     */
    @GetMapping("/list")
    public RespBean list(UserVo userVo, Authentication authentication){
        RespBean respBean = RespBean.build();
        User user = (User)authentication.getPrincipal();
        //创建Page对象
        Page<User> page = new Page<>(userVo.getCurrent(),userVo.getSize());
        //构建条件
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq((userVo.getType() != null), "type", userVo.getType());
        wrapper.like(StringUtils.hasText(userVo.getNickname()),"username",userVo.getNickname());
        wrapper.orderByDesc("id");
        //调用mybatis plus分页方法进行查询
        IPage<User> iPage = tUserService.page(page, wrapper);
        respBean.setStatus(200);
        respBean.setObj(iPage);
        return respBean;
    }

    /**
     * 保存用户
     * @param user
     * @return
     */
    @PostMapping("/add")
    public RespBean saveUser(@RequestBody User user){
        RespBean respBean = RespBean.build();
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("username",user.getUsername());
        List<User> list = tUserService.list(queryWrapper);
        if (list.size() > 0){
            respBean.setStatus(500);
            respBean.setMsg("添加失败，该账号已存在");
            return respBean;
        }

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        if (tUserService.save(user)){
            respBean.setStatus(200);
            respBean.setMsg("添加成功！");
            return respBean;
        }else {
            respBean.setStatus(500);
            respBean.setMsg("添加失败！");
            return respBean;
        }

    }

    /**
     * 查询所有用户
     * @return
     */
    @GetMapping("/getAllUser")
    public RespBean getAllType(){
        return tUserService.getAllUser();
    }

    /**
     * 更新用户
     * @param user
     * @return
     */
    @PutMapping("/updateUser")
    public RespBean updateType(@RequestBody User user){
        RespBean respBean = RespBean.build();
        if (StringUtils.isEmpty(user.getUsername())){
            respBean.setMsg("用户名称不能为空");
            return respBean;
        }
        return tUserService.updateUser(user);
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @DeleteMapping("deleteUserById")
    public RespBean deleteUserById(Long id){
        return tUserService.deleteUserById(id);
    }

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    @GetMapping("/getUserById")
    public RespBean getTypeById(Long id){
        return tUserService.getUserById(id);
    }

    /**
     * 更新密码
     * @param passwordVo
     * @return
     */
    @PutMapping("/updatePassword")
    public RespBean updatePassword(@RequestBody PasswordVo passwordVo){
        RespBean respBean = RespBean.build();
        User user = tUserService.getById(passwordVo.getId());
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        if(!bcryptPasswordEncoder.matches(passwordVo.getOldPassword(),user.getPassword())){
            respBean.setStatus(500);
            respBean.setMsg("旧密码有误，请重新输入");
            return respBean;
        }
        user.setPassword(new BCryptPasswordEncoder().encode(passwordVo.getNewPassword()));
        if (tUserService.updateById(user)){
            respBean.setMsg("修改成功");
            respBean.setStatus(200);
            return respBean;
        }
        respBean.setMsg("修改失败");
        return respBean;
    }

    /**
     * 注册
     * @return
     */
    @PostMapping("/register")
    public RespBean register(@RequestBody User user){
        RespBean respBean = RespBean.build();
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("username",user.getUsername());
        List<User> tBlogList = tUserService.list(queryWrapper);
        if (tBlogList.size() > 0){
            respBean.setStatus(500);
            respBean.setMsg("注册失败，已有该用户名");
            return respBean;
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setType(2);
        Boolean result = tUserService.save(user);
        if (result){
            respBean.setStatus(200);
            respBean.setMsg("注册成功！");
            return respBean;
        }else {
            respBean.setStatus(500);
            respBean.setMsg("注册失败！");
            return respBean;
        }

    }
}
