package com.ego.upload;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.domain.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @Author TheKing
 * @Date 2019/9/25 22:35
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EgoUploadService.class)
public class FastDFSTest {
    @Autowired
    private FastFileStorageClient storageClient; // 上传图
    @Autowired
    private ThumbImageConfig thumbImageConfig; // 上传图并且生成缩略图

    @Test
    public void testUpload() throws FileNotFoundException {
        File file = new File("D:\\kakaxi.jpg");
        // 上传
        StorePath storePath = this.storageClient.uploadFile(new FileInputStream(file), file.length(), "jpg", null);
        // 带分组的路径
        System.out.println(storePath.getFullPath());
        // 不带分组的路径
        System.out.println(storePath.getPath());
    }

    @Test
    public void testUploadAndCreateThumb() throws FileNotFoundException {
        File file = new File("D:\\kakaxi.jpg");
        // 上传图并且生成缩略图
        StorePath storePath = this.storageClient.uploadImageAndCrtThumbImage(new FileInputStream(file),file.length(),"png",null);
        // 带分组的路径
        System.out.println(storePath.getFullPath());
        // 不带分组的路径
        System.out.println(storePath.getPath());
        // 获取缩略图路径
        String path = thumbImageConfig.getThumbImagePath(storePath.getPath());
        System.out.println(path);
    }
}
