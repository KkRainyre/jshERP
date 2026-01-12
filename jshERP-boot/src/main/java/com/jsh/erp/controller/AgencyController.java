package com.jsh.erp.controller;

import com.alibaba.fastjson.JSONObject;
import com.jsh.erp.base.BaseController;
import com.jsh.erp.base.TableDataInfo;
import com.jsh.erp.datasource.entities.LcAgency;
import com.jsh.erp.datasource.entities.Lcagent;
import com.jsh.erp.exception.BusinessRunTimeException;
import com.jsh.erp.service.AgencyService;
import com.jsh.erp.utils.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jsh.erp.utils.ResponseJsonUtil.returnJson;
import static com.jsh.erp.utils.ResponseJsonUtil.returnStr;

@RestController
@RequestMapping("/agency")
public class AgencyController extends BaseController {

    @Resource
    private AgencyService lcAgencyService;

    // ---------------------------------------------
    // GET AGENCY BY ID
    // ---------------------------------------------
    @GetMapping("/info")
    public String getInfo(@RequestParam("id") Long id) throws Exception {
        Map<String, Object> map = new HashMap<>();
        LcAgency agency = lcAgencyService.getAgency(id);

        if (agency != null) {
            map.put("info", agency);
            return returnJson(map, ErpInfo.OK.name, ErpInfo.OK.code);
        } else {
            return returnJson(map, ErpInfo.ERROR.name, ErpInfo.ERROR.code);
        }
    }

    // ---------------------------------------------
    // LIST AGENCIES (with search filters)
    // ---------------------------------------------
    @GetMapping("/list")
    public TableDataInfo list(
            @RequestParam(value = Constants.SEARCH, required = false) String search) throws Exception {

        String name = StringUtil.getInfo(search, "name");
        String tier = StringUtil.getInfo(search, "tier");
        String phone = StringUtil.getInfo(search, "phone");
        String city = StringUtil.getInfo(search, "city");
        String state = StringUtil.getInfo(search, "state");
        String postal = StringUtil.getInfo(search, "postal");

        startPage(); // REQUIRED for pagination

        List<LcAgency> list = lcAgencyService.select(name, tier, phone, city, state, postal);
        return getDataTable(list);
    }

    // ---------------------------------------------
    // ADD AGENCY
    // ---------------------------------------------
    @PostMapping("/add")
    public String add(@RequestBody JSONObject obj) throws Exception {
        Map<String, Object> map = new HashMap<>();
        int result = lcAgencyService.insertAgency(obj);
        return returnStr(map, result);
    }

    // ---------------------------------------------
    // UPDATE AGENCY
    // ---------------------------------------------
    @PutMapping("/update")
    public String update(@RequestBody JSONObject obj) throws Exception {
        Map<String, Object> map = new HashMap<>();
        int result = lcAgencyService.updateAgency(obj);
        return returnStr(map, result);
    }

    // ---------------------------------------------
    // DELETE AGENCY
    // ---------------------------------------------
    @DeleteMapping("/delete")
    public String delete(@RequestParam("id") Long id) throws Exception {
        Map<String, Object> map = new HashMap<>();
        int result = lcAgencyService.deleteAgency(id);
        return returnStr(map, result);
    }

    @DeleteMapping("/deleteBatch")
    public String deleteBatch(@RequestParam("ids") String ids) throws Exception {
        Map<String, Object> map = new HashMap<>();
        int result = lcAgencyService.batchDeleteAgency(ids);
        return returnStr(map, result);
    }

    /*
     * @PostMapping("/uploadLogo")
     * public String uploadLogo(@RequestParam("id") Long id,
     * 
     * @RequestParam("file") MultipartFile file) throws Exception {
     * // ... implementation removed for OSS string storage ...
     * return null;
     * }
     */

    @GetMapping("/agent")
    public String getAgentsByCompany(@RequestParam("id") Long companyId,
            @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize) {

        Map<String, Object> map = new HashMap<>();

        // get list
        List<Lcagent> list = lcAgencyService.getAgentsByCompany(companyId, pageNo, pageSize);

        // total count for pagination
        int total = lcAgencyService.countAgentsByCompany(companyId);

        map.put("records", list);
        map.put("total", total);

        return returnJson(map, ErpInfo.OK.name, ErpInfo.OK.code);
    }

    /**
     * Import agencies from Excel
     * 
     * @param file
     * @param request
     * @param response
     * @return
     */
    @PostMapping(value = "/importAgency")
    @ApiOperation(value = "import agencies")
    public BaseResponseInfo importAgency(MultipartFile file,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        BaseResponseInfo res = new BaseResponseInfo();
        try {
            lcAgencyService.checkFileExt(file);
            lcAgencyService.importAgency(file, request);
            res.code = 200;
            res.data = "Import successful";
        } catch (BusinessRunTimeException e) {
            res.code = e.getCode();
            res.data = e.getData().get("message");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            res.code = 500;
            res.data = "Import failed";
        }
        return res;
    }

}
