package com.jsh.erp.controller;

import com.alibaba.fastjson.JSONObject;

import com.jsh.erp.datasource.entities.LcProject;
import com.jsh.erp.service.LcProjectService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import com.jsh.erp.exception.BusinessRunTimeException;
import com.jsh.erp.utils.BaseResponseInfo;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.jsh.erp.utils.*;
import java.util.List;

@RestController
@RequestMapping("/lcproject")
public class LcProjectController {

    @Resource
    private LcProjectService lcProjectService;

    @GetMapping("/select")
    public Object select(@RequestParam(required = false) String name,
            @RequestParam(required = false) String agency,
            @RequestParam(required = false) String exactClient,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer currentPage,
            @RequestParam(required = false) Integer pageSize) {

        if (currentPage == null)
            currentPage = 1;
        if (pageSize == null)
            pageSize = 10;
        Integer offset = (currentPage - 1) * pageSize;

        List<LcProject> list = lcProjectService.select(name, agency, exactClient, status, location, offset, pageSize);
        int total = lcProjectService.count(name, agency, exactClient, status, location);

        JSONObject obj = new JSONObject();
        obj.put("data", list);
        obj.put("count", total);

        return obj;
    }

    @GetMapping("/analysis")
    public Object analysis(@RequestParam String agency) {
        return lcProjectService.getAnalysis(agency);
    }

    @GetMapping("/get/{id}")
    public LcProject get(@PathVariable Long id) {
        return lcProjectService.getProject(id);
    }

    @PostMapping("/add")
    public int add(@RequestBody LcProject project) {
        return lcProjectService.insertProject(project);
    }

    @PutMapping("/update")
    public int update(@RequestBody LcProject project) {
        return lcProjectService.updateProject(project);
    }

    @DeleteMapping("/delete/{id}")
    public int delete(@PathVariable Long id) {
        return lcProjectService.deleteProject(id);
    }

    @DeleteMapping("/deleteBatch")
    public int batchDelete(@RequestParam("ids") String ids) {
        return lcProjectService.batchDeleteProject(ids);
    }

    @PostMapping("/importProject")
    public BaseResponseInfo importProject(MultipartFile file,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        BaseResponseInfo res = new BaseResponseInfo();
        try {
            lcProjectService.checkFileExt(file);
            res = lcProjectService.importProject(file, request);
        } catch (BusinessRunTimeException e) {
            res.code = e.getCode();
            res.data = e.getData().get("message");
        } catch (Exception e) {
            e.printStackTrace();
            res.code = 500;
            res.data = "Import failed";
        }
        return res;
    }
}
