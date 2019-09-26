package com.ego.upload.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @Author TheKing
 * @Date 2019/9/25 15:46
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */

@Service
public class UploadService {

    // 支持的文件类型
    private static final List<String> contentTypes = Arrays.asList("image/png", "image/jpeg");

    @Autowired
    private FastFileStorageClient storageClient; // 上传图

    /**
     * 上传服务器文件
     *
     * @param file
     * @return
     */
    public String uploadImage(MultipartFile file) {
// 1、图片信息校验
        // 1)校验文件类型
        String contentType = file.getContentType();
        if (!contentTypes.contains(contentType)) {
            return null;
        }
        // 2)校验图片内容
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (null == image) {
                return null;
            }
// 2、保存图片
            //获取文件后缀
            String suffix = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
            //上传
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), suffix, null);
            //返回文件路径 http://image.ego.com/group(01)asdadasasd.xxx
            return "http://image.ego.com/" + storePath.getFullPath();
            //测试
            /*// 2.1、生成保存目录
            File dir = new File("D:\\egoImages\\upload");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            // 2.2、保存图片
            file.transferTo(new File(dir, file.getOriginalFilename()));
            // 2.3、拼接图片地址
            String url = "D:\\egoImages\\upload" + file.getOriginalFilename();
            return url;*/
        } catch (IOException e) {
            return null;
        }
    }
}
