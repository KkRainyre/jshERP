package com.jsh.erp.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OssClientConfig {

    @Bean(destroyMethod = "shutdown")
    public OSS ossClient(
            @Value("${aliyun.oss.endpoint}") String endpoint,
            @Value("${aliyun.oss.accessKeyId}") String id,
            @Value("${aliyun.oss.accessKeySecret}") String secret
    ) {
        return new OSSClientBuilder().build(endpoint, id, secret);
    }


}
