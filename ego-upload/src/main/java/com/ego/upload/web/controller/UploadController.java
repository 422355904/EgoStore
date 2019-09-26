package com.ego.upload.web.controller;

import com.ego.upload.service.UploadService;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author TheKing
 * @Date 2019/9/25 15:40
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@RestController
public class UploadController {

    @Autowired
    private FastFileStorageClient storageClient;
    @Autowired
    private UploadService uploadService;

    /**
     * FastDFS上传图片到远程服务器
     * @param file
     * @return
     */
    @PostMapping("image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file){

        String url=uploadService.uploadImage(file);

        if (StringUtils.isBlank(url)){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(url);
    }

}
