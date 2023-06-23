package com.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.*;
import com.service.TUserService;
import com.service.WordFeedbackService;
import com.service.WordNetworkService;
import com.service.WordService;
import com.utils.RespBean;
import com.vo.CommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.*;

/**
 * <p>
 * 单词网络表 前端控制器
 * </p>
 *
 * @author
 * @since 2023-04-21
 */
@RestController
@RequestMapping("/wordNetwork")
public class WordNetworkController {
    @Autowired
    WordNetworkService wordNetworkService;
    @Autowired
    TUserService tUserService;
    @Autowired
    WordService wordService;

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
        Page<WordNetwork> page = new Page<>(commentVo.getCurrent(), commentVo.getSize());
        //构建条件
        QueryWrapper<WordNetwork> wrapper = new QueryWrapper<>();
        if ("isMy".equals(commentVo.getIsMy())) {
            wrapper.eq("create_by", user.getId());
        }
        if (StringUtils.hasText(commentVo.getName())) {
            wrapper.like("name", commentVo.getName());
        }
        wrapper.orderByDesc("create_time");
        //调用mybatis plus分页方法进行查询
        IPage<WordNetwork> iPage = wordNetworkService.page(page, wrapper);
        for (WordNetwork record : iPage.getRecords()) {
            record.setUser(tUserService.getById(record.getCreateBy()));
            record.setWord1(wordService.getById(record.getWordId1()));
            record.setWord2(wordService.getById(record.getWordId2()));
        }
        respBean.setStatus(200);
        respBean.setObj(iPage);
        return respBean;
    }

    /**
     * 增加
     *
     * @param wordNetwork
     * @return
     */
    @PostMapping("/add")
    public RespBean add(@RequestBody WordNetwork wordNetwork, Authentication authentication) {
        RespBean respBean = RespBean.build();
        User user = (User) authentication.getPrincipal();
        if (wordNetwork.getIsOpen() == 2) {
            wordNetwork.setStatus(1);
        }
        wordNetwork.setCreateBy(user.getId());
        wordNetwork.setCreateTime(new Date());
        if (wordNetworkService.save(wordNetwork)) {
            respBean.setStatus(200);
            respBean.setMsg("添加成功！");
        } else {
            respBean.setStatus(500);
            respBean.setMsg("添加失败！");
        }
        return respBean;
    }

    /**
     * 更新
     *
     * @param wordNetwork
     * @return
     */
    @PutMapping("/update")
    public RespBean update(@RequestBody WordNetwork wordNetwork) {
        RespBean respBean = RespBean.build();
        if (wordNetwork.getIsOpen() == 2) {
            wordNetwork.setStatus(1);
        }
        if (wordNetworkService.updateById(wordNetwork)) {
            respBean.setMsg("更新成功");
            respBean.setStatus(200);
            return respBean;
        }
        respBean.setMsg("更新失败");
        return respBean;
    }

    /**
     * 审核
     *
     * @param wordNetwork
     * @return
     */
    @PutMapping("/audit")
    public RespBean audit(@RequestBody WordNetwork wordNetwork) {
        RespBean respBean = RespBean.build();
        WordNetwork byId = wordNetworkService.getById(wordNetwork.getId());
        byId.setStatus(wordNetwork.getStatus());
        if (wordNetworkService.updateById(wordNetwork)) {
            respBean.setMsg("审核成功");
            respBean.setStatus(200);
            return respBean;
        }
        respBean.setMsg("审核失败");
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
        if (wordNetworkService.removeById(id)) {
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
        WordNetwork wordNetwork = wordNetworkService.getById(id);
        respBean.setStatus(200);
        respBean.setObj(wordNetwork);
        return respBean;
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @GetMapping("/getDetailById")
    public RespBean getDetailById(Long id, Authentication authentication) {
        RespBean respBean = RespBean.build();
        User user = (User) authentication.getPrincipal();
        WordNetwork wordNetwork = wordNetworkService.getById(id);

        List<WordNetwork> sets = wordNetworkService.getByAllNetwordByUserId(wordNetwork.getId(), user.getId());
        Set<WordNetwork> links = new HashSet<>();
        links = queryLinks(wordNetwork, links, sets);

        Set<WordNetwork> data = new HashSet<>();
        WordNetwork wordNetwork1 = null;
        WordNetwork wordNetwork2 = null;
        for (WordNetwork link : links) {
            wordNetwork1 = new WordNetwork();
            wordNetwork2 = new WordNetwork();
            Word word1 = wordService.getById(link.getWordId1());
            Word word2 = wordService.getById(link.getWordId2());
            link.setSource(word1.getName());
            link.setTarget(word2.getName());
            wordNetwork1.setName(word1.getName());
            data.add(wordNetwork1);
            wordNetwork2.setName(word2.getName());
            data.add(wordNetwork2);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("data", data);
        map.put("links", links);
        respBean.setStatus(200);
        respBean.setObj(map);
        return respBean;
    }

    public Set<WordNetwork> queryLinks(WordNetwork wordNetwork, Set<WordNetwork> links, List<WordNetwork> sets) {
        //将获得的所有用户，添加到userSet中
        links.add(wordNetwork);
        for (int i = 0; i < sets.size(); i++) {
            WordNetwork set = sets.get(i);
            if (set.getWordId1() == wordNetwork.getWordId1() || set.getWordId2() == wordNetwork.getWordId1() || set.getWordId1() == wordNetwork.getWordId2() || set.getWordId2() == wordNetwork.getWordId2()) {
                sets.remove(set);
                queryLinks(set, links, sets);
            } else {
                continue;
            }
        }
        return links;
    }
}
