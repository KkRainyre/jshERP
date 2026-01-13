package com.jsh.erp.service;

import com.aliyun.oss.OSS;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.InputStream;

@Service
public class FileUploadService {

    @Resource
    private OSS ossClient;

    @Value("${aliyun.oss.bucket}")
    private String bucket;

    public String upload(InputStream in, String key) {
        ossClient.putObject(bucket, key, in);
        return key;
    }
}
