package com.jsh.erp.controller;

import com.jsh.erp.datasource.entities.LcQuote;
import com.jsh.erp.service.LcQuoteService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/lcquote")
public class LcQuoteController extends com.jsh.erp.base.BaseController {

    @Resource
    private LcQuoteService lcQuoteService;

    @GetMapping("/list")
    public com.jsh.erp.base.TableDataInfo list(
            @RequestParam(value = com.jsh.erp.utils.Constants.SEARCH, required = false) String search)
            throws Exception {

        String quoteNo = com.jsh.erp.utils.StringUtil.getInfo(search, "quoteNo");
        String customerName = com.jsh.erp.utils.StringUtil.getInfo(search, "customerName");
        String status = com.jsh.erp.utils.StringUtil.getInfo(search, "status");
        String minPrice = com.jsh.erp.utils.StringUtil.getInfo(search, "minPrice");
        String maxPrice = com.jsh.erp.utils.StringUtil.getInfo(search, "maxPrice");

        //startPage();
        List<LcQuote> list = lcQuoteService.select(quoteNo, customerName, status, minPrice, maxPrice);
        return getDataTable(list);
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
