package com.jsh.erp.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.locks.ReentrantLock;

/**
 * STS credentials provider that calls AssumeRole and caches temporary credentials.
 */
@Component
public class StsCredentialsProvider {
    private static final Logger logger = LoggerFactory.getLogger(StsCredentialsProvider.class);

    @Value("${aliyun.sts.accessKeyId:}")
    private String accessKeyId;

    @Value("${aliyun.sts.accessKeySecret:}")
    private String accessKeySecret;

    @Value("${aliyun.sts.roleArn:}")
    private String roleArn;

    @Value("${aliyun.sts.roleSessionName:jsh-erp-session}")
    private String roleSessionName;

    @Value("${aliyun.sts.durationSeconds:3600}")
    private Integer durationSeconds;

    private volatile CachedCredentials cached = null;
    private final ReentrantLock lock = new ReentrantLock();

    @PostConstruct
    public void init() {
        logger.info("StsCredentialsProvider initialized. roleArn={}", roleArn);
    }

    public Credentials getCredentials() throws ClientException {
        // Fast path
        CachedCredentials c = cached;
        if (c != null && !c.isExpiredSoon()) {
            return c.toCredentials();
        }

        lock.lock();
        try {
            c = cached;
            if (c != null && !c.isExpiredSoon()) {
                return c.toCredentials();
            }

            // call AssumeRole
            logger.debug("Requesting new STS credentials for role: {}", roleArn);
            IClientProfile profile = DefaultProfile.getProfile("", accessKeyId, accessKeySecret);
            IAcsClient client = new DefaultAcsClient(profile);

            AssumeRoleRequest request = new AssumeRoleRequest();
            request.setRoleArn(roleArn);
            request.setRoleSessionName(roleSessionName);
            request.setDurationSeconds(durationSeconds);

            AssumeRoleResponse response = client.getAcsResponse(request);

            String tmpAccessKeyId = response.getCredentials().getAccessKeyId();
            String tmpSecret = response.getCredentials().getAccessKeySecret();
            String token = response.getCredentials().getSecurityToken();
            long expirationEpoch = response.getCredentials().getExpiration().getTime();

            cached = new CachedCredentials(tmpAccessKeyId, tmpSecret, token, expirationEpoch);
            logger.info("Obtained STS credentials, expireAt={}", response.getCredentials().getExpiration());
            return cached.toCredentials();
        } finally {
            lock.unlock();
        }
    }

    private static class CachedCredentials {
        final String accessKeyId;
        final String accessKeySecret;
        final String securityToken;
        final long expireAt; // ms

        CachedCredentials(String ak, String sk, String token, long expireAt) {
            this.accessKeyId = ak;
            this.accessKeySecret = sk;
            this.securityToken = token;
            this.expireAt = expireAt;
        }

        boolean isExpiredSoon() {
            // Refresh if within 30 seconds of expiry or already expired
            return System.currentTimeMillis() + 30_000L >= expireAt;
        }

        Credentials toCredentials() {
            return new Credentials(accessKeyId, accessKeySecret, securityToken, expireAt);
        }
    }

    public static class Credentials {
        public final String accessKeyId;
        public final String accessKeySecret;
        public final String securityToken;
        public final long expireAt;

        public Credentials(String accessKeyId, String accessKeySecret, String securityToken, long expireAt) {
            this.accessKeyId = accessKeyId;
            this.accessKeySecret = accessKeySecret;
            this.securityToken = securityToken;
            this.expireAt = expireAt;
        }
    }
}
