package com.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.*;
import com.service.TUserService;
import com.service.WordFeedbackService;
import com.service.WordRootsAffixesService;
import com.service.WordService;
import com.utils.RespBean;
import com.vo.CommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 词根词缀表 前端控制器
 * </p>
 *
 * @author
 * @since 2023-04-21
 */
@RestController
@RequestMapping("/wordRootsAffixes")
public class WordRootsAffixesController {
    @Autowired
    WordRootsAffixesService wordRootsAffixesService;
    @Autowired
    TUserService tUserService;
    @Autowired
    WordService wordService;

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
        Page<WordRootsAffixes> page = new Page<>(commentVo.getCurrent(),commentVo.getSize());
        //构建条件
        QueryWrapper<WordRootsAffixes> wrapper = new QueryWrapper<>();
        if("isMy".equals(commentVo.getIsMy())){
            wrapper.eq("create_by",user.getId());
        }
        if(StringUtils.hasText(commentVo.getRoot())){
            wrapper.like("root",commentVo.getRoot());
        }
        if(StringUtils.hasText(commentVo.getAffixe())){
            wrapper.like("affixe",commentVo.getAffixe());
        }
        wrapper.orderByDesc("create_time");
        //调用mybatis plus分页方法进行查询
        IPage<WordRootsAffixes> iPage = wordRootsAffixesService.page(page, wrapper);
        for (WordRootsAffixes record : iPage.getRecords()) {
            record.setUser(tUserService.getById(record.getCreateBy()));
            record.setWord(wordService.getById(record.getWordId()));
        }
        respBean.setStatus(200);
        respBean.setObj(iPage);
        return respBean;
    }

    /**
     * 增加
     * @param wordRootsAffixes
     * @return
     */
    @PostMapping("/add")
    public RespBean add(@RequestBody WordRootsAffixes wordRootsAffixes, Authentication authentication){
        RespBean respBean = RespBean.build();
        User user = (User)authentication.getPrincipal();
        if(wordRootsAffixes.getIsOpen() == 2){
            wordRootsAffixes.setStatus(1);
        }
        wordRootsAffixes.setCreateBy(user.getId());
        wordRootsAffixes.setCreateTime(new Date());
        if (wordRootsAffixesService.save(wordRootsAffixes)){
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
     * @param wordRootsAffixes
     * @return
     */
    @PutMapping("/update")
    public RespBean update(@RequestBody WordRootsAffixes wordRootsAffixes){
        RespBean respBean = RespBean.build();
        if(wordRootsAffixes.getIsOpen() == 2){
            wordRootsAffixes.setStatus(1);
        }
        if (wordRootsAffixesService.updateById(wordRootsAffixes)){
            respBean.setMsg("更新成功");
            respBean.setStatus(200);
            return respBean;
        }
        respBean.setMsg("更新失败");
        return respBean;
    }

    /**
     * 审核
     * @param wordRootsAffixes
     * @return
     */
    @PutMapping("/audit")
    public RespBean audit(@RequestBody WordRootsAffixes wordRootsAffixes){
        RespBean respBean = RespBean.build();
        WordRootsAffixes byId = wordRootsAffixesService.getById(wordRootsAffixes.getId());
        byId.setStatus(wordRootsAffixes.getStatus());
        if (wordRootsAffixesService.updateById(wordRootsAffixes)){
            respBean.setMsg("审核成功");
            respBean.setStatus(200);
            return respBean;
        }
        respBean.setMsg("审核失败");
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
        if (wordRootsAffixesService.removeById(id)){
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
        WordRootsAffixes wordRootsAffixes = wordRootsAffixesService.getById(id);
        respBean.setStatus(200);
        respBean.setObj(wordRootsAffixes);
        return respBean;
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @GetMapping("/getDetailById")
    public RespBean getDetailById(Long id, Authentication authentication){
        RespBean respBean = RespBean.build();
        User user = (User)authentication.getPrincipal();
        WordRootsAffixes wordRootsAffixes = wordRootsAffixesService.getById(id);
        wordRootsAffixes.setWord(wordService.getById(wordRootsAffixes.getWordId()));
        if(StringUtils.hasText(wordRootsAffixes.getRoot())){
            List<Word> rootList = wordRootsAffixesService.getRootList(wordRootsAffixes.getRoot(), wordRootsAffixes.getWordId(), user.getId());
            wordRootsAffixes.setRootList(rootList);
        }
        if(StringUtils.hasText(wordRootsAffixes.getAffixe())){
            List<Word> affixeList = wordRootsAffixesService.getAffixeList(wordRootsAffixes.getAffixe(), wordRootsAffixes.getWordId(), user.getId());
            wordRootsAffixes.setAffixeList(affixeList);
        }
        respBean.setStatus(200);
        respBean.setObj(wordRootsAffixes);
        return respBean;
    }

}
