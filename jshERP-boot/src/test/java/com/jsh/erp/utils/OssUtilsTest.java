package com.jsh.erp.utils;

import com.aliyun.oss.OSS;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;

public class OssUtilsTest {

    @Test
    public void testUploadDelegatesToClient() throws Exception {
        OssUtils utils = new OssUtils();

        // mock client and factory
        OSS mockClient = Mockito.mock(OSS.class);
        OssClientFactory mockFactory = Mockito.mock(OssClientFactory.class);
        Mockito.when(mockFactory.createClient()).thenReturn(mockClient);

        // inject
        utils.setOssClientFactory(mockFactory);

        // set bucket via reflection
        java.lang.reflect.Field f = OssUtils.class.getDeclaredField("bucket");
        f.setAccessible(true);
        f.set(utils, "test-bucket");

        String key = "opt/jshERP/upload/agency/1/test.png";
        byte[] bytes = "hello".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);

        String returned = utils.upload(in, bytes.length, "image/png", key);

        Assert.assertEquals(key, returned);
        // verify putObject called
        Mockito.verify(mockClient, Mockito.times(1)).putObject(Mockito.eq("test-bucket"), Mockito.eq(key), Mockito.any(), Mockito.any());
        Mockito.verify(mockClient, Mockito.times(1)).shutdown();
    }
}