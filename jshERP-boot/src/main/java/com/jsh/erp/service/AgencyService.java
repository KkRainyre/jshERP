package com.jsh.erp.service;

import com.alibaba.fastjson.JSONObject;
import com.jsh.erp.constants.BusinessConstants;
import com.jsh.erp.datasource.entities.*;
//import com.jsh.erp.datasource.mappers.Lc;
import com.jsh.erp.datasource.mappers.LcAgencyMapper;
import com.jsh.erp.datasource.mappers.LcAgencyMapperEx;
import com.jsh.erp.datasource.mappers.LcagentMapper;
import com.jsh.erp.datasource.mappers.LcagentMapperEx;
import com.jsh.erp.exception.JshException;
import com.jsh.erp.utils.BaseResponseInfo;
import com.jsh.erp.utils.ExcelUtils;
import com.jsh.erp.utils.PageUtils;
import com.jsh.erp.utils.StringUtil;

import jxl.Sheet;
import jxl.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class AgencyService {

    private Logger logger = LoggerFactory.getLogger(AgencyService.class);

    @Resource
    private LcAgencyMapper lcAgencyMapper;

    @Resource
    private LcAgencyMapperEx lcAgencyMapperEx;

    @Resource
    private LcagentMapper lcagencyMapper;

    @Resource
    private LcagentMapperEx lcagentMapperEx;

    @Resource
    private LogService logService;

    @Resource
    private UserService userService;

    // ---------------------------------------------------------------------
    // GET AGENCY
    // ---------------------------------------------------------------------
    public LcAgency getAgency(Long id) throws Exception {
        try {
            return lcAgencyMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            JshException.readFail(logger, e);
            return null;
        }
    }

    // ---------------------------------------------------------------------
    // LIST AGENCIES with filters
    // ---------------------------------------------------------------------
    public List<LcAgency> select(String name, String tier, String phone, String city, String state, String postal)
            throws Exception {
        try {
            PageUtils.startPage();
            return lcAgencyMapperEx.selectByCondition(name, tier, phone, city, state, postal);
        } catch (Exception e) {
            JshException.readFail(logger, e);
            return null;
        }
    }

    // ---------------------------------------------------------------------
    // INSERT AGENCY
    // ---------------------------------------------------------------------
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int insertAgency(JSONObject obj) throws Exception {
        LcAgency agency = JSONObject.parseObject(obj.toJSONString(), LcAgency.class);
        int result = 0;
        try {
            agency.setMakeTime(new Date());
            agency.setModifyTime(new Date());
            result = lcAgencyMapper.insertSelective(agency);
        } catch (Exception e) {
            JshException.writeFail(logger, e);
        }
        return result;
    }

    // ---------------------------------------------------------------------
    // UPDATE AGENCY
    // ---------------------------------------------------------------------
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int updateAgency(JSONObject obj) throws Exception {
        LcAgency agency = JSONObject.parseObject(obj.toJSONString(), LcAgency.class);
        int result = 0;
        try {

            agency.setModifyTime(new Date());
            result = lcAgencyMapper.updateByPrimaryKeySelective(agency);
        } catch (Exception e) {
            JshException.writeFail(logger, e);
        }
        return result;
    }

    // ---------------------------------------------------------------------
    // DELETE AGENCY
    // ---------------------------------------------------------------------
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int deleteAgency(Long id) throws Exception {
        int result = 0;
        try {
            result = lcAgencyMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteAgency(String ids) throws Exception {
        List<Long> idList = StringUtil.strToLongList(ids);
        int result = 0;
        try {
            for (Long id : idList) {
                lcAgencyMapper.deleteByPrimaryKey(id);
                result++;
            }
        } catch (Exception e) {
            JshException.writeFail(logger, e);
        }
        return result;
    }

    public List<Lcagent> getAgentsByCompany(Long companyId, Integer pageNo, Integer pageSize) {
        int offset = (pageNo - 1) * pageSize;
        return lcagencyMapper.selectAgentsByCompany(companyId, offset, pageSize);
    }

    public int countAgentsByCompany(Long companyId) {
        return lcagencyMapper.countAgentsByCompany(companyId);
    }

    public int updateLogo(Long id, byte[] fullLogo, byte[] thumb) {
        return lcAgencyMapper.updateLogo(id, fullLogo, thumb);
    }

    /**
     * Check file extension
     */
    public void checkFileExt(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new Exception("File is empty");
        }
        String fileName = file.getOriginalFilename();
        if (fileName == null || (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx"))) {
            throw new Exception("Only Excel files (.xls, .xlsx) are allowed");
        }
    }

    /**
     * Import agencies from Excel file
     * Expected columns: name*, email*, phone*, remarks, address, bill_address,
     * city, state, postal, enrollment, website, tier(A,B,C,D), shipping_acc, EIN,
     * country
     */
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void importAgency(MultipartFile file, HttpServletRequest request) throws Exception {
        User userInfo = userService.getCurrentUser();
        Workbook workbook = Workbook.getWorkbook(file.getInputStream());
        Sheet src = workbook.getSheet(0);

        // Expected columns: name*, email*, phone*, remarks, address, bill_address,
        // city, state, postal, enrollment, website, tier(A,B,C,D), shipping_acc, EIN,
        // country
        List<LcAgency> agencyList = new ArrayList<>();
        for (int i = 2; i < src.getRows(); i++) { // Start from row 2 (skip warning and header)
            String name = ExcelUtils.getContent(src, i, 0);

            if (StringUtil.isNotEmpty(name)) {
                LcAgency agency = new LcAgency();
                agency.setName(name); // Column 0: name*
                agency.setEmail(ExcelUtils.getContent(src, i, 1)); // Column 1: email*
                agency.setPhone(ExcelUtils.getContent(src, i, 2)); // Column 2: phone*
                agency.setRemarks(ExcelUtils.getContent(src, i, 3)); // Column 3: remarks
                agency.setAddress(ExcelUtils.getContent(src, i, 4)); // Column 4: address
                agency.setBillAddress(ExcelUtils.getContent(src, i, 5)); // Column 5: bill_address
                agency.setCity(ExcelUtils.getContent(src, i, 6)); // Column 6: city
                agency.setState(ExcelUtils.getContent(src, i, 7)); // Column 7: state
                agency.setPostal(ExcelUtils.getContent(src, i, 8)); // Column 8: postal
                agency.setEnrollment(ExcelUtils.getContent(src, i, 9)); // Column 9: enrollment
                agency.setWebsite(ExcelUtils.getContent(src, i, 10)); // Column 10: website
                agency.setTier(ExcelUtils.getContent(src, i, 11)); // Column 11: tier(A,B,C,D)
                agency.setShippingAcc(ExcelUtils.getContent(src, i, 12));// Column 12: shipping_acc
                agency.setEIN(ExcelUtils.getContent(src, i, 13)); // Column 13: EIN
                agency.setCountry(ExcelUtils.getContent(src, i, 14)); // Column 14: country
                agency.setMakeTime(new Date());
                agency.setModifyTime(new Date());
                agencyList.add(agency);
            }
        }
        importExcel(agencyList, request);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public BaseResponseInfo importExcel(List<LcAgency> agencyList, HttpServletRequest request) throws Exception {
        logService.insertLog("Agency",
                new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_IMPORT).append(agencyList.size())
                        .append(BusinessConstants.LOG_DATA_UNIT).toString(),
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        BaseResponseInfo info = new BaseResponseInfo();
        Map<String, Object> data = new HashMap<>();
        try {
            for (LcAgency agency : agencyList) {
                // Directly insert new agencies
                lcAgencyMapper.insertSelective(agency);
            }
            info.code = 200;
            data.put("message", "Successfully imported " + agencyList.size() + " agencies");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            info.code = 500;
            data.put("message", e.getMessage());
        }
        info.data = data;
        return info;
    }

}
