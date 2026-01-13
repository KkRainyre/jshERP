package com.jsh.erp.controller;

import com.jsh.erp.utils.OssUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/oss")
public class OssFileController {

    @Resource
    private OssUtils ossUtils;

    @GetMapping("/urls")
    public Map<String, String> getUrls(
            @RequestParam String objectKey,
            @RequestParam String fileName
    ) throws UnsupportedEncodingException {
        Map<String, String> res = new HashMap<>();
        res.put("previewUrl", ossUtils.generatePreviewUrl(objectKey, 300));   // 5分钟
        res.put("downloadUrl", ossUtils.generateDownloadUrl(objectKey, fileName, 60)); // 1分钟
        return res;
    }

    @PostMapping("/previewUrls")
    public Map<String, String> previewUrls(@RequestBody Map<String, Object> body) {
        List<String> keys = (List<String>) body.get("objectKeys");
        Map<String, String> res = new HashMap<>();
        if (keys == null) return res;

        for (String key : keys) {
            if (key != null && !key.startsWith("http")) {
                res.put(key, ossUtils.generatePreviewUrl(key, 300));
            }
        }
        return res;
    }

}
