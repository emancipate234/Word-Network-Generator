package com.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.User;
import com.entity.Word;
import com.service.TUserService;
import com.service.WordService;
import com.utils.RespBean;
import com.vo.CommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 单词表 前端控制器
 * </p>
 *
 * @author
 * @since 2023-04-21
 */
@RestController
@RequestMapping("/word")
public class WordController {
    @Autowired
    WordService wordService;
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
        //创建Page对象
        Page<Word> page = new Page<>(commentVo.getCurrent(),commentVo.getSize());
        //构建条件
        QueryWrapper<Word> wrapper = new QueryWrapper<>();
        if(StringUtils.hasText(commentVo.getName())){
            wrapper.like("name",commentVo.getName());
        }
        if(StringUtils.hasText(commentVo.getTranslate())){
            wrapper.like("translate",commentVo.getTranslate());
        }
        wrapper.orderByAsc("name");
        //调用mybatis plus分页方法进行查询
        IPage<Word> iPage = wordService.page(page, wrapper);
        respBean.setStatus(200);
        respBean.setObj(iPage);
        return respBean;
    }

    @GetMapping("/getAll")
    public RespBean getAll(){
        RespBean respBean = RespBean.build();
        //构建条件
        QueryWrapper<Word> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("name");
        //调用mybatis plus分页方法进行查询
        List<Word> list = wordService.list(wrapper);
        respBean.setStatus(200);
        respBean.setObj(list);
        return respBean;
    }

    /**
     * 增加
     * @param leave
     * @return
     */
    @PostMapping("/add")
    public RespBean add(@RequestBody Word leave){
        RespBean respBean = RespBean.build();
        if (wordService.save(leave)){
            respBean.setStatus(200);
            respBean.setMsg("添加成功！");
        }else {
            respBean.setStatus(500);
            respBean.setMsg("添加失败！");
        }
        return respBean;
    }

    //excel导入
    @PostMapping("/importExcel")
    public Boolean importExcel(MultipartFile file) throws IOException, ParseException {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);

        //方式二：忽略表头中文，直接获取表格数据
        List<List<Object>> list = reader.read(1);
        List<Word> words = CollUtil.newArrayList();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        for(List<Object> row:list){
            Word word =new Word();
            word.setName((String) row.get(0));
            word.setPhonetic((String) row.get(1));
            word.setTranslate((String) row.get(2));
            word.setDistortion((String) row.get(3));
            word.setPhrase((String) row.get(4));
            word.setSamples((String) row.get(5));
            words.add(word);
        }

        //将excel导入的数据保存到数据库
        wordService.saveBatch(words);
        return true;
    }

    /**
     * 更新
     * @param leave
     * @return
     */
    @PutMapping("/update")
    public RespBean update(@RequestBody Word leave){
        RespBean respBean = RespBean.build();
        if (wordService.updateById(leave)){
            respBean.setMsg("更新成功");
            respBean.setStatus(200);
            return respBean;
        }
        respBean.setMsg("更新失败");
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
        if (wordService.removeById(id)){
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
        Word word = wordService.getById(id);
        respBean.setStatus(200);
        respBean.setObj(word);
        return respBean;
    }

    @GetMapping("/getWord/{number}")
    public RespBean getWord(@PathVariable Long number, Authentication authentication){
        RespBean respBean = RespBean.build();
        User user = (User)authentication.getPrincipal();
        List<Word> list = wordService.getWord(number, user.getId());
        respBean.setStatus(200);
        respBean.setObj(list);
        return respBean;
    }
}
