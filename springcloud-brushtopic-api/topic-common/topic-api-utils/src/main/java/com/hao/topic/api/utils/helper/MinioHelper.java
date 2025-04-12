package com.haoma.netdisk.helper;


import com.alibaba.fastjson2.util.DateUtils;
import com.hao.topic.api.utils.minio.MinioConfig;
import com.hao.topic.api.utils.properties.MinIOConfigProperties;
import com.hao.topic.common.enums.ResultCodeEnum;
import com.hao.topic.common.exception.TopicException;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.nio.file.Files;
import java.util.Date;
import java.util.UUID;

/**
 * Description: minio帮助类
 */
@Component
@Slf4j
public class MinioHelper {

    @Autowired
    private MinIOConfigProperties minIOConfigProperties;

    @Autowired
    private MinioConfig minioConfig;

    /**
     * 上传文件到minio
     *
     * @param path   路径
     * @param file   文件
     * @param suffix 多余路径
     * @return
     */
    public String uploadFile(String path, File file, String suffix) {
        try {
            // 连接minio客户端
            MinioClient minioClient = minioConfig.buildMinioClient();
            // 判断存储桶是否存在
            boolean isBucket = minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(minIOConfigProperties.getBucket())
                            .build()
            );
            // 判断
            if (!isBucket) {
                // 不存在创建一个
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(minIOConfigProperties.getBucket())
                        .build());
            } else {
                log.error("桶存在");
            }

            // 设置存储路径
            // 当天作为目录
            String date = DateUtils.format(new Date(), "yyyyMMdd");
            // 唯一id作为图片路径
            String uuid = UUID.randomUUID().toString().replace("-", "");

            // 组合路径
            String fileName = null;
            if (suffix != null) {
                fileName = path + "/" + suffix + "/" + date + "/" + uuid;
            } else {
                fileName = path + "/" + date + "/" + uuid;
            }
            // 构建上传参数
            PutObjectArgs build = PutObjectArgs.builder()
                    .bucket(minIOConfigProperties.getBucket())
                    .object(fileName)
                    .stream(new FileInputStream(file), file.length(), -1)
                    .build();
            // 上传
            minioClient.putObject(build);
            // 将路径返回
            return minIOConfigProperties.getReadPath() + "/" + minIOConfigProperties.getBucket() + "/" + fileName;
        } catch (Exception e) {
            throw new TopicException(ResultCodeEnum.UPLOAD_FILE_ERROR);
        }
    }

    /**
     * 删除minio中的文件
     *
     * @param path 需要删除的文件路径
     */
    public void delUpload(String path) {
        try {
            MinioClient minioClient = minioConfig.buildMinioClient();
            // 删除文件
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(minIOConfigProperties.getBucket()).object(path).build());
            log.info("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("删除失败");
        }
    }
}
