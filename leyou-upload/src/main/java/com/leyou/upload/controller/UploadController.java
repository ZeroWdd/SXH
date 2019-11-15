package com.leyou.upload.controller;

import com.leyou.upload.util.OssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Auther: wdd
 * @Date: 2019/10/22 19:46
 * @Description:
 */
@RestController
@RequestMapping("/upload")
public class UploadController {
    @Autowired
    private OssUtil ossUtil;

    @PostMapping("/image")
    public String ossUpload(@RequestParam("file") MultipartFile file,@RequestParam(value = "folder",required = false) String folder){
        return ossUtil.uploadImage(file,folder);
    }
}
