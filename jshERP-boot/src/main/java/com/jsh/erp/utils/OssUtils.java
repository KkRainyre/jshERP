package com.jsh.erp.utils;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Date;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
@Component
public class OssUtils {

    @Resource
    private OssClientFactory ossClientFactory;

    // package-private setter for tests
    void setOssClientFactory(OssClientFactory factory) {
        this.ossClientFactory = factory;
    }

    @Value("${aliyun.oss.bucket}")
    private String bucket;

    /**
     * 只负责上传，不负责访问
     */
    public String upload(InputStream in, long size, String contentType, String objectKey) {
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(size);
        meta.setContentType(contentType);

        OSS client = null;
        try {
            client = ossClientFactory.createClient();
            client.putObject(bucket, objectKey, in, meta);
            return objectKey; // ✅ 只返回 objectKey
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload to OSS", e);
        } finally {
            if (client != null) {
                client.shutdown();
            }
        }
    }

    /**
     * 图片 / PDF 预览
     */
    public String generatePreviewUrl(String objectKey, int expireSeconds) {
        Date expiration = new Date(System.currentTimeMillis() + expireSeconds * 1000L);

        GeneratePresignedUrlRequest req =
                new GeneratePresignedUrlRequest(bucket, objectKey, HttpMethod.GET);
        req.setExpiration(expiration);

        OSS client = null;
        try {
            client = ossClientFactory.createClient();
            URL url = client.generatePresignedUrl(req);
            return url.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate presigned URL", e);
        } finally {
            if (client != null) {
                client.shutdown();
            }
        }
    }

    /**
     * 文件下载（强制 attachment）
     */
    public String generateDownloadUrl(String objectKey, String fileName, int expireSeconds) throws UnsupportedEncodingException {
        Date expiration = new Date(System.currentTimeMillis() + expireSeconds * 1000L);

        GeneratePresignedUrlRequest req =
                new GeneratePresignedUrlRequest(bucket, objectKey, HttpMethod.GET);
        req.setExpiration(expiration);

        String safeName = sanitizeFileName(fileName);
        String encoded = URLEncoder.encode(safeName, "UTF-8").replace("+", "%20");


        String disposition =
                "attachment; filename=\"" + safeName + "\"; filename*=UTF-8''" + encoded;

        req.addQueryParameter("response-content-disposition", disposition);

        OSS client = null;
        try {
            client = ossClientFactory.createClient();
            URL url = client.generatePresignedUrl(req);
            return url.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate download URL", e);
        } finally {
            if (client != null) {
                client.shutdown();
            }
        }
    }

    private String sanitizeFileName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "download";
        }
        return name.replaceAll("[\\r\\n\"]", "_");
    }
}
