package com.jsh.erp.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Factory to create OSS clients using STS temporary credentials.
 * Caller is responsible for shutting down the returned client when finished.
 */
@Component
public class OssClientFactory {
    private static final Logger logger = LoggerFactory.getLogger(OssClientFactory.class);

    @Value("${aliyun.oss.endpoint:}")
    private String endpoint;

    @Resource
    private StsCredentialsProvider stsProvider;

    // Package-private setter for tests
    void setStsProvider(StsCredentialsProvider provider) {
        this.stsProvider = provider;
    }

    public OSS createClient() throws Exception {
        StsCredentialsProvider.Credentials c = stsProvider.getCredentials();
        if (c == null) throw new IllegalStateException("STS credentials unavailable");
        logger.debug("Creating OSS client with temporary credentials, expireAt={}", c.expireAt);

        // Use OSSClientBuilder that accepts temporary token
        return new OSSClientBuilder().build(endpoint, c.accessKeyId, c.accessKeySecret, c.securityToken);
    }
}
