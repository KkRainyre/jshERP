package com.jsh.erp.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.jsh.erp.datasource.entities.Lcagent;
import com.jsh.erp.service.LcagentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/lcagent")
public class LcagentController {

    @Resource
    private LcagentService lcagentService;
    
    @GetMapping("/select")
    public Object select(@RequestParam(required = false) String name,
                         @RequestParam(required = false) String category,
                         @RequestParam(required = false) String phone,
                         @RequestParam(required = false) String address,
                         @RequestParam(required = false) String companyId) throws Exception {

        List<Lcagent> list = lcagentService.select(name, category, phone, address, companyId);

        // Handle null list
        if (list == null) {
            list = new java.util.ArrayList<>();
        }

        // Get pagination info from the list
        PageInfo<Lcagent> pageInfo = new PageInfo<>(list);
        long total = pageInfo.getTotal();

        JSONObject obj = new JSONObject();
        obj.put("data", list);
        obj.put("count", total);

        return obj;
    }

    @GetMapping("/get/{id}")
    public Lcagent get(@PathVariable Long id) throws Exception {
        return lcagentService.getAgent(id);
    }

    @PostMapping("/add")
    public int add(@RequestBody Lcagent agent) throws Exception {
        return lcagentService.insertAgent(agent);
    }

    @PostMapping("/update")
    public int update(@RequestBody Lcagent agent) throws Exception {
        return lcagentService.updateAgent(agent);
    }

    @DeleteMapping("/delete/{id}")
    public int delete(@PathVariable Long id) throws Exception {
        return lcagentService.deleteAgent(id);
    }

    @PostMapping("/updateLogo/{id}")
    public int updateLogo(@PathVariable Long id, @RequestBody byte[] logo) {
        return lcagentService.updateLogo(id, logo);
    }
}

