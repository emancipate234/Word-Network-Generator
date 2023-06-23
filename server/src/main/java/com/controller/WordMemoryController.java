package com.controller;


import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.User;
import com.entity.WordFeedback;
import com.entity.WordMemory;
import com.service.TUserService;
import com.service.WordFeedbackService;
import com.service.WordMemoryService;
import com.utils.RespBean;
import com.vo.CommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * <p>
 * 单词记忆表 前端控制器
 * </p>
 *
 * @author
 * @since 2023-04-21
 */
@RestController
@RequestMapping("/wordMemory")
public class WordMemoryController {
    @Autowired
    WordMemoryService wordMemoryService;
    @Autowired
    TUserService tUserService;

    /**
     * 分页查询
     *
     * @param commentVo
     * @return
     */
    @GetMapping("/list")
    public RespBean list(CommentVo commentVo, Authentication authentication) {
        RespBean respBean = RespBean.build();
        User user = (User) authentication.getPrincipal();
        //创建Page对象
        Page<WordMemory> page = new Page<>(commentVo.getCurrent(), commentVo.getSize());
        //构建条件
        QueryWrapper<WordMemory> wrapper = new QueryWrapper<>();
        if ("isMy".equals(commentVo.getIsMy())) {
            wrapper.eq("user_id", user.getId());
        }
        if (StringUtils.hasText(commentVo.getName())) {
            wrapper.like("cause", commentVo.getName());
        }
        wrapper.orderByDesc("create_time");
        //调用mybatis plus分页方法进行查询
        IPage<WordMemory> iPage = wordMemoryService.page(page, wrapper);
        for (WordMemory record : iPage.getRecords()) {
            record.setUser(tUserService.getById(record.getCreateBy()));
        }
        respBean.setStatus(200);
        respBean.setObj(iPage);
        return respBean;
    }
    /**
     * 更新
     *
     * @param wordMemory
     * @return
     */
    @PutMapping("/update")
    public RespBean update(@RequestBody WordMemory wordMemory, Authentication authentication) {
        RespBean respBean = RespBean.build();
        User user = (User) authentication.getPrincipal();
        Long wordId = wordMemory.getWordId();
        QueryWrapper<WordMemory> wrapper = new QueryWrapper<>();
        wrapper.eq("create_by", user.getId());
        wrapper.eq("word_id", wordId);
        wrapper.last("limit 1");
        WordMemory one = wordMemoryService.getOne(wrapper);
        if(one == null){
            wordMemory.setIntervalNum(0L);
            wordMemory.setCount(1);
            wordMemory.setCreateBy(user.getId());
            wordMemory.setIntervalTime(new Date());
            wordMemory.setUpdateTime(new Date());
            if (wordMemoryService.save(wordMemory)) {
                respBean.setStatus(200);
                return respBean;
            }
        }else {
            one.setCount(one.getCount() + 1);
            Date updateTime = one.getUpdateTime();
            Date dateTime = new DateTime();
            long a = dateTime.getTime();
            long b = updateTime.getTime();
            one.setIntervalNum((Long) ((a - b) / 1000));
            one.setIntervalTime(updateTime);
            one.setUpdateTime(dateTime);
            if (wordMemoryService.updateById(one)) {
                respBean.setStatus(200);
                return respBean;
            }
        }

        respBean.setMsg("更新失败");
        return respBean;
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("delete")
    public RespBean delete(Long id) {
        RespBean respBean = RespBean.build();
        if (wordMemoryService.removeById(id)) {
            respBean.setStatus(200);
            respBean.setMsg("删除成功");
            return respBean;
        }
        respBean.setMsg("删除失败");
        return respBean;
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @GetMapping("/getById")
    public RespBean getById(Long id) {
        RespBean respBean = RespBean.build();
        WordMemory wordMemory = wordMemoryService.getById(id);
        respBean.setStatus(200);
        respBean.setObj(wordMemory);
        return respBean;
    }

    @GetMapping("/getByWordId")
    public RespBean getByWordId(Long wordId, Authentication authentication) {
        RespBean respBean = RespBean.build();
        User user = (User) authentication.getPrincipal();
        QueryWrapper<WordMemory> wrapper = new QueryWrapper<>();
        wrapper.eq("create_by", user.getId());
        wrapper.eq("word_id", wordId);
        wrapper.last("limit 1");
        WordMemory wordMemory = wordMemoryService.getOne(wrapper);
        respBean.setStatus(200);
        respBean.setObj(wordMemory);
        return respBean;
    }
}
