package com.jsh.erp.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;

import java.io.InputStream;

public class OssUtils {
    // Constants removed as they are now passed as parameters

    public static String upload(String endpoint, String bucketName, InputStream in, long size, String contentType,
            String objectKey,
            String accessKeyId, String accessKeySecret) {
        OSS oss = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentLength(size);
            if (contentType != null)
                meta.setContentType(contentType);

            oss.putObject(bucketName, objectKey, in, meta);

            // Return full URL
            // Assuming endpoint is like "https://oss-na-south-1.aliyuncs.com" or
            // "oss-na-south-1.aliyuncs.com"
            String domain = endpoint;
            if (domain.startsWith("https://")) {
                domain = domain.substring(8);
            } else if (domain.startsWith("http://")) {
                domain = domain.substring(7);
            }
            return "https://" + bucketName + "." + domain + "/" + objectKey;
        } finally {
            oss.shutdown();
        }
    }
}
