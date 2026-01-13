package com.jsh.erp.utils;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StsCredentialsProviderTest {

    @Test
    public void testCachingWhenNotExpired() throws Exception {
        IAcsClient mockClient = Mockito.mock(IAcsClient.class);

        AssumeRoleResponse mockResp = Mockito.mock(AssumeRoleResponse.class);
        AssumeRoleResponse.Credentials mockCreds = Mockito.mock(AssumeRoleResponse.Credentials.class);

        long future = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5);
        when(mockCreds.getAccessKeyId()).thenReturn("tmpAK");
        when(mockCreds.getAccessKeySecret()).thenReturn("tmpSK");
        when(mockCreds.getSecurityToken()).thenReturn("token");
        when(mockCreds.getExpiration()).thenReturn(new Date(future));
        when(mockResp.getCredentials()).thenReturn(mockCreds);
        when(mockClient.getAcsResponse(any(AssumeRoleRequest.class))).thenReturn(mockResp);

        StsCredentialsProvider provider = new StsCredentialsProvider(profile -> mockClient);

        StsCredentialsProvider.Credentials c1 = provider.getCredentials();
        Assert.assertEquals("tmpAK", c1.accessKeyId);

        StsCredentialsProvider.Credentials c2 = provider.getCredentials();
        Assert.assertSame(c1.accessKeyId, c2.accessKeyId);

        verify(mockClient, times(1)).getAcsResponse(any(AssumeRoleRequest.class));
    }

    @Test
    public void testRefreshWhenExpiringSoon() throws Exception {
        IAcsClient mockClient = Mockito.mock(IAcsClient.class);

        // First response expires soon (within refresh threshold)
        AssumeRoleResponse resp1 = Mockito.mock(AssumeRoleResponse.class);
        AssumeRoleResponse.Credentials creds1 = Mockito.mock(AssumeRoleResponse.Credentials.class);
        long expireSoon = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(20); // < 30s threshold
        when(creds1.getAccessKeyId()).thenReturn("ak1");
        when(creds1.getAccessKeySecret()).thenReturn("sk1");
        when(creds1.getSecurityToken()).thenReturn("token1");
        when(creds1.getExpiration()).thenReturn(new Date(expireSoon));
        when(resp1.getCredentials()).thenReturn(creds1);

        // Second response is long-lived
        AssumeRoleResponse resp2 = Mockito.mock(AssumeRoleResponse.class);
        AssumeRoleResponse.Credentials creds2 = Mockito.mock(AssumeRoleResponse.Credentials.class);
        long later = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1);
        when(creds2.getAccessKeyId()).thenReturn("ak2");
        when(creds2.getAccessKeySecret()).thenReturn("sk2");
        when(creds2.getSecurityToken()).thenReturn("token2");
        when(creds2.getExpiration()).thenReturn(new Date(later));
        when(resp2.getCredentials()).thenReturn(creds2);

        when(mockClient.getAcsResponse(any(AssumeRoleRequest.class))).thenReturn(resp1, resp2);

        StsCredentialsProvider provider = new StsCredentialsProvider(profile -> mockClient);

        StsCredentialsProvider.Credentials first = provider.getCredentials();
        Assert.assertEquals("ak1", first.accessKeyId);

        // Second call should trigger a refresh because first expires soon
        StsCredentialsProvider.Credentials second = provider.getCredentials();
        Assert.assertEquals("ak2", second.accessKeyId);

        verify(mockClient, times(2)).getAcsResponse(any(AssumeRoleRequest.class));
    }
}