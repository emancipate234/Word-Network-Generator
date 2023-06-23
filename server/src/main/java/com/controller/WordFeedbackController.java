package com.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.User;
import com.entity.WordFeedback;
import com.service.TUserService;
import com.service.WordFeedbackService;
import com.utils.RespBean;
import com.vo.CommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * <p>
 * 反馈表 前端控制器
 * </p>
 *
 * @author
 * @since 2023-04-21
 */
@RestController
@RequestMapping("/wordFeedback")
public class WordFeedbackController {
    @Autowired
    WordFeedbackService wordFeedbackService;
    @Autowired
    TUserService tUserService;

    /**
     * 分页查询
     * @param commentVo
     * @return
     */
    @GetMapping("/list")
    public RespBean list(CommentVo commentVo, Authentication authentication){
        RespBean respBean = RespBean.build();
        User user = (User)authentication.getPrincipal();
        //创建Page对象
        Page<WordFeedback> page = new Page<>(commentVo.getCurrent(),commentVo.getSize());
        //构建条件
        QueryWrapper<WordFeedback> wrapper = new QueryWrapper<>();
        if("isMy".equals(commentVo.getIsMy())){
            wrapper.eq("create_by",user.getId());
        }
        if(StringUtils.hasText(commentVo.getName())){
            wrapper.like("content",commentVo.getName());
        }
        wrapper.orderByDesc("create_time");
        //调用mybatis plus分页方法进行查询
        IPage<WordFeedback> iPage = wordFeedbackService.page(page, wrapper);
        for (WordFeedback record : iPage.getRecords()) {
            record.setUser(tUserService.getById(record.getCreateBy()));
        }
        respBean.setStatus(200);
        respBean.setObj(iPage);
        return respBean;
    }

    /**
     * 增加
     * @param wordFeedback
     * @return
     */
    @PostMapping("/add")
    public RespBean add(@RequestBody WordFeedback wordFeedback, Authentication authentication){
        RespBean respBean = RespBean.build();
        User user = (User)authentication.getPrincipal();
        wordFeedback.setStatus(1);
        wordFeedback.setCreateBy(user.getId());
        wordFeedback.setCreateTime(new Date());
        if (wordFeedbackService.save(wordFeedback)){
            respBean.setStatus(200);
            respBean.setMsg("添加成功！");
        }else {
            respBean.setStatus(500);
            respBean.setMsg("添加失败！");
        }
        return respBean;
    }

    /**
     * 更新
     * @param wordFeedback
     * @return
     */
    @PutMapping("/update")
    public RespBean update(@RequestBody WordFeedback wordFeedback){
        RespBean respBean = RespBean.build();
        wordFeedback.setStatus(2);
        wordFeedback.setReplyTime(new Date());
        if (wordFeedbackService.updateById(wordFeedback)){
            respBean.setMsg("回复成功");
            respBean.setStatus(200);
            return respBean;
        }
        respBean.setMsg("回复失败");
        return respBean;
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @DeleteMapping("delete")
    public RespBean delete(Long id){
        RespBean respBean = RespBean.build();
        if (wordFeedbackService.removeById(id)){
            respBean.setStatus(200);
            respBean.setMsg("删除成功");
            return respBean;
        }
        respBean.setMsg("删除失败");
        return respBean;
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @GetMapping("/getById")
    public RespBean getById(Long id){
        RespBean respBean = RespBean.build();
        WordFeedback wordFeedback = wordFeedbackService.getById(id);
        respBean.setStatus(200);
        respBean.setObj(wordFeedback);
        return respBean;
    }
}
