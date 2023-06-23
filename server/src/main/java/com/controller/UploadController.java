package com.controller;

import com.utils.RespBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class UploadController {

    @Value("${img.path}")
    private String imgPath;

    @Value("${img.url}")
    private String imgUrl;
    /**
     * 上传图片
     * @param file
     * @param request
     * @throws IOException
     */
    @RequestMapping( value = "/upload")
    @ResponseBody
    public RespBean upload(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request) throws IOException {
        RespBean respBean = RespBean.build();
        //目前这里是写死的本地硬盘路径
        //String path = "C:/img";
        //获取文件名称
        String fileName = file.getOriginalFilename();
        //获取文件名后缀
        Calendar currTime = Calendar.getInstance();
        String time = String.valueOf(currTime.get(Calendar.YEAR))+String.valueOf((currTime.get(Calendar.MONTH)+1));
        //获取文件名后缀
        String suffix = fileName.substring(file.getOriginalFilename().lastIndexOf("."));
        suffix = suffix.toLowerCase();
        if(suffix.equals(".jpg") || suffix.equals(".jpeg") || suffix.equals(".png")/* || suffix.equals(".gif")*/){
            fileName = UUID.randomUUID().toString()+suffix;
            File targetFile = new File(imgPath, fileName);
            if(!targetFile.getParentFile().exists()){    //注意，判断父级路径是否存在
                targetFile.getParentFile().mkdirs();
            }
            long size = 0;
            //保存
            try {
                file.transferTo(targetFile);
                size = file.getSize();
            } catch (Exception e) {
                e.printStackTrace();
                respBean.setStatus(500);
                respBean.setMsg("上传失败！");
                return respBean;
            }
            //项目url，这里可以使用常量或者去数据字典获取相应的url前缀；
            //String fileUrl="http://localhost:8082";
            //文件获取路径
            String fileUrl = imgUrl + request.getContextPath() + "/img/" + fileName;
            Map<String, Object> map = new HashMap<>();
            map.put("fileUrl",fileUrl);
            respBean.setStatus(200);
            respBean.setObj(map);
            return respBean;
        }else{
            respBean.setStatus(500);
            respBean.setMsg("图片格式有误，请上传.jpg、.png、.jpeg格式的文件！");
            return respBean;
        }

    }
}
