package com.jsh.erp.service;

import com.jsh.erp.datasource.entities.Lcagent;
import com.jsh.erp.datasource.mappers.LcagentMapper;
import com.jsh.erp.datasource.mappers.LcagentMapperEx;
import com.jsh.erp.exception.JshException;
import com.jsh.erp.utils.PageUtils;

import jxl.write.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import com.jsh.erp.constants.BusinessConstants;
import com.jsh.erp.datasource.entities.User;
import com.jsh.erp.utils.BaseResponseInfo;
import com.jsh.erp.utils.ExcelUtils;
import com.jsh.erp.utils.StringUtil;
import jxl.Sheet;
import jxl.Workbook;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class LcagentService {

    private Logger logger = LoggerFactory.getLogger(LcagentService.class);

    @Resource
    private LcagentMapper lcagentMapper;

    @Resource
    private LcagentMapperEx lcagentMapperEx;

    @Resource
    private UserService userService;

    @Resource
    private LogService logService;

    // ---------------------------------------------------------------------
    // GET BY ID
    // ---------------------------------------------------------------------
    public Lcagent getAgent(Long id) throws Exception {
        try {
            return lcagentMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            JshException.readFail(logger, e);
            return null;
        }
    }

    // ---------------------------------------------------------------------
    // LIST WITH FILTERS
    // ---------------------------------------------------------------------
    public List<Lcagent> select(String name, String category,
            String phone, String address, String companyId) throws Exception {
        try {
            PageUtils.startPage();
            List<Lcagent> result = lcagentMapperEx.selectByCondition(name, category, phone, address, companyId);
            return result != null ? result : new java.util.ArrayList<>();
        } catch (Exception e) {
            JshException.readFail(logger, e);
            return new java.util.ArrayList<>();
        }
    }

    // ---------------------------------------------------------------------
    // INSERT ENTITY
    // ---------------------------------------------------------------------
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int insertAgent(Lcagent agent) throws Exception {
        int result = 0;
        try {
            agent.setEmployId(UUID.randomUUID().toString());
            agent.setMakeTime(new Date());
            agent.setModifyTime(new Date());
            result = lcagentMapper.insertSelective(agent);
        } catch (Exception e) {
            JshException.writeFail(logger, e);
        }
        return result;
    }

    // ---------------------------------------------------------------------
    // UPDATE ENTITY
    // ---------------------------------------------------------------------
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int updateAgent(Lcagent agent) throws Exception {
        int result = 0;
        try {
            result = lcagentMapper.updateByPrimaryKeySelective(agent);
        } catch (Exception e) {
            JshException.writeFail(logger, e);
        }
        return result;
    }

    // ---------------------------------------------------------------------
    // DELETE
    // ---------------------------------------------------------------------
    // ---------------------------------------------------------------------
    // DELETE
    // ---------------------------------------------------------------------
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int deleteAgent(Long id) throws Exception {
        int result = 0;
        try {
            result = lcagentMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteAgent(String ids) throws Exception {
        String[] idArray = ids.split(",");
        int result = 0;
        for (String id : idArray) {
            result += deleteAgent(Long.parseLong(id));
        }
        return result;
    }

    // ---------------------------------------------------------------------
    // UPDATE LOGO
    // ---------------------------------------------------------------------
    /*
     * public int updateLogo(Long id, byte[] logo) {
     * // try {
     * // return lcagentMapper.updateLogo(id, logo);
     * // } catch (Exception e) {
     * // JshException.writeFail(logger, e);
     * // return -1;
     * // }
     * return 0;
     * }
     */

    // ---------------------------------------------------------------------
    // IMPORT
    // ---------------------------------------------------------------------
    public void checkFileExt(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new Exception("File is empty");
        }
        String fileName = file.getOriginalFilename();
        if (fileName == null || (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx"))) {
            throw new Exception("Only Excel files (.xls, .xlsx) are allowed");
        }
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void importAgent(MultipartFile file, HttpServletRequest request) throws Exception {
        User userInfo = userService.getCurrentUser();
        Workbook workbook = Workbook.getWorkbook(file.getInputStream());
        Sheet src = workbook.getSheet(0);

        // Expected columns: Name*, Category, Phone, Address, Title, Email, OfficeNum,
        // PhoneExt, Remark
        List<Lcagent> agentList = new ArrayList<>();
        for (int i = 1; i < src.getRows(); i++) {
            String name = ExcelUtils.getContent(src, i, 0);
            if (StringUtil.isNotEmpty(name)) {
                Lcagent agent = new Lcagent();
                agent.setName(name);
                agent.setCategory(ExcelUtils.getContent(src, i, 1));
                agent.setPhone(ExcelUtils.getContent(src, i, 2));
                agent.setAddress(ExcelUtils.getContent(src, i, 3));
                agent.setTitle(ExcelUtils.getContent(src, i, 4));
                agent.setEmail(ExcelUtils.getContent(src, i, 5));
                agent.setOfficeNum(ExcelUtils.getContent(src, i, 6));
                agent.setPhoneExt(ExcelUtils.getContent(src, i, 7));
                agent.setRemark(ExcelUtils.getContent(src, i, 8));

                agent.setEmployId(UUID.randomUUID().toString());
                agent.setMakeTime(new Date());
                agent.setModifyTime(new Date());
                if (userInfo != null) {
                    agent.setTenantId(
                            userInfo.getTenantId() != null ? Integer.parseInt(userInfo.getTenantId().toString())
                                    : null);
                }

                agentList.add(agent);
            }
        }
        importExcel(agentList, request);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public BaseResponseInfo importExcel(List<Lcagent> agentList, HttpServletRequest request) throws Exception {
        logService.insertLog("Lcagent",
                new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_IMPORT).append(agentList.size())
                        .append(BusinessConstants.LOG_DATA_UNIT).toString(),
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        BaseResponseInfo info = new BaseResponseInfo();
        Map<String, Object> data = new HashMap<>();
        try {
            for (Lcagent agent : agentList) {
                lcagentMapper.insertSelective(agent);
            }
            info.code = 200;
            data.put("message", "Successfully imported " + agentList.size() + " agents");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            info.code = 500;
            data.put("message", e.getMessage());
        }
        info.data = data;
        return info;
    }
}
