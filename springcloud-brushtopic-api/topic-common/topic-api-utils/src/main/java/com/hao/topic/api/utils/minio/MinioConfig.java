package com.hao.topic.api.utils.minio;

import com.hao.topic.api.utils.properties.MinIOConfigProperties;
import io.minio.MinioClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description: minio配置类
 */
@Data
@Configuration
@EnableConfigurationProperties({MinIOConfigProperties.class})
public class MinioConfig {

    @Autowired
    private MinIOConfigProperties minIOConfigProperties;

    /**
     * 连接Minio
     *
     * @return
     */
    @Bean
    public MinioClient buildMinioClient() {
        return MinioClient
                .builder()
                .credentials(minIOConfigProperties.getAccessKey(), minIOConfigProperties.getSecretKey())
                .endpoint(minIOConfigProperties.getEndpoint())
                .build();
    }
}
