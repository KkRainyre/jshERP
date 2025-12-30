package com.jsh.erp.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.jsh.erp.datasource.entities.LcQuote;
import com.jsh.erp.service.LcQuoteService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/lcquote")
public class LcQuoteController {

    @Resource
    private LcQuoteService lcQuoteService;

    @GetMapping("/list")
    public Object list(@RequestParam(required = false) String quoteNo,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String status) throws Exception {

        List<LcQuote> list = lcQuoteService.select(quoteNo, customerName, status);

        if (list == null) {
            list = new java.util.ArrayList<>();
        }

        PageInfo<LcQuote> pageInfo = new PageInfo<>(list);
        long total = pageInfo.getTotal();

        JSONObject obj = new JSONObject();
        obj.put("data", list);
        obj.put("count", total);

        return obj;
    }

    @GetMapping("/get/{id}")
    public LcQuote get(@PathVariable Long id) throws Exception {
        return lcQuoteService.getQuote(id);
    }

    @PostMapping("/add")
    public int add(@RequestBody com.jsh.erp.datasource.vo.QuoteVo quote) throws Exception {
        return lcQuoteService.insertQuote(quote);
    }

    @PostMapping("/update")
    public int update(@RequestBody com.jsh.erp.datasource.vo.QuoteVo quote) throws Exception {
        return lcQuoteService.updateQuote(quote);
    }

    @DeleteMapping("/delete/{id}")
    public int delete(@PathVariable Long id) throws Exception {
        return lcQuoteService.deleteQuote(id);
    }

    @DeleteMapping("/deleteBatch")
    public int batchDelete(@RequestParam("ids") String ids) throws Exception {
        return lcQuoteService.batchDeleteQuote(ids);
    }
}
