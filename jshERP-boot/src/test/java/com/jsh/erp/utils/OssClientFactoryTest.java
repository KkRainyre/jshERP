package com.jsh.erp.utils;

import com.aliyun.oss.OSS;
import org.junit.Assert;
import org.junit.Test;

public class OssClientFactoryTest {

    @Test
    public void testCreateClientWithStubbedProvider() throws Exception {
        OssClientFactory factory = new OssClientFactory();
        // configure endpoint via reflection since there's no setter
        java.lang.reflect.Field f = OssClientFactory.class.getDeclaredField("endpoint");
        f.setAccessible(true);
        f.set(factory, "http://oss-cn-hangzhou.aliyuncs.com");

        // stub provider
        StsCredentialsProvider.Credentials creds = new StsCredentialsProvider.Credentials("AKID", "AKSECRET", "TOKEN", System.currentTimeMillis() + 3600_000);
        StsCredentialsProvider stub = new StsCredentialsProvider() {
            @Override
            public Credentials getCredentials() {
                return creds;
            }
        };
        factory.setStsProvider(stub);

        OSS client = null;
        try {
            client = factory.createClient();
            Assert.assertNotNull(client);
        } finally {
            if (client != null) client.shutdown();
        }
    }
}