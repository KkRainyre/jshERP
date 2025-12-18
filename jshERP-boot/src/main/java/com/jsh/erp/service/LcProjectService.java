package com.jsh.erp.service;

import com.jsh.erp.datasource.entities.LcProject;
import com.jsh.erp.datasource.mappers.LcProjectMapper;
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
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LcProjectService {

    @Resource
    private LcProjectMapper lcProjectMapper;

    @Resource
    private UserService userService;

    @Resource
    private LogService logService;

    public LcProject getProject(Long id) {
        return lcProjectMapper.selectByPrimaryKey(id);
    }

    public List<LcProject> select(String name, String agency, String exactClient, String status, String location,
            Integer offset,
            Integer pageSize) {
        return lcProjectMapper.selectByCondition(name, agency, exactClient, status, location, offset, pageSize);
    }

    public int count(String name, String agency, String exactClient, String status, String location) {
        return lcProjectMapper.countByCondition(name, agency, exactClient, status, location);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int insertProject(LcProject project) {
        return lcProjectMapper.insertSelective(project);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int updateProject(LcProject project) {
        return lcProjectMapper.updateByPrimaryKeySelective(project);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int deleteProject(Long id) {
        return lcProjectMapper.deleteByPrimaryKey(id);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteProject(String ids) {
        String[] idArray = ids.split(",");
        int result = 0;
        for (String id : idArray) {
            result += deleteProject(Long.parseLong(id));
        }
        return result;
    }

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
    public BaseResponseInfo importProject(MultipartFile file, HttpServletRequest request) throws Exception {
        User userInfo = userService.getCurrentUser();
        Workbook workbook = Workbook.getWorkbook(file.getInputStream());
        Sheet src = workbook.getSheet(0);

        // Expected columns: Name*, Client, Status, Location, Start Date, End Date,
        // Budget, Manager, Contact Person, Contact Phone, Remarks
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<LcProject> projectList = new ArrayList<>();
        for (int i = 1; i < src.getRows(); i++) {
            String name = ExcelUtils.getContent(src, i, 0);
            if (StringUtil.isNotEmpty(name)) {
                LcProject project = new LcProject();
                project.setName(name);
                project.setAgency(ExcelUtils.getContent(src, i, 1));
                project.setStatus(ExcelUtils.getContent(src, i, 2));
                project.setLocation(ExcelUtils.getContent(src, i, 3));

                String startDateStr = ExcelUtils.getContent(src, i, 4);
                if (StringUtil.isNotEmpty(startDateStr)) {
                    try {
                        project.setStartDate(sdf.parse(startDateStr));
                    } catch (Exception e) {
                        /* ignore */}
                }

                String endDateStr = ExcelUtils.getContent(src, i, 5);
                if (StringUtil.isNotEmpty(endDateStr)) {
                    try {
                        project.setEndDate(sdf.parse(endDateStr));
                    } catch (Exception e) {
                        /* ignore */}
                }

                String budgetStr = ExcelUtils.getContent(src, i, 6);
                if (StringUtil.isNotEmpty(budgetStr)) {
                    try {
                        project.setBudget(new BigDecimal(budgetStr));
                    } catch (Exception e) {
                        /* ignore */}
                }

                project.setManager(ExcelUtils.getContent(src, i, 7));
                project.setContactPerson(ExcelUtils.getContent(src, i, 8));
                project.setContactPhone(ExcelUtils.getContent(src, i, 9));
                project.setRemarks(ExcelUtils.getContent(src, i, 10));

                project.setMakeTime(new Date());
                project.setModifyTime(new Date());
                if (userInfo != null) {
                    project.setTenantId(userInfo.getTenantId() != null ? userInfo.getTenantId() : null);
                }

                projectList.add(project);
            }
        }
        return importExcel(projectList, request);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public BaseResponseInfo importExcel(List<LcProject> projectList, HttpServletRequest request) throws Exception {
        logService.insertLog("LcProject",
                new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_IMPORT).append(projectList.size())
                        .append(BusinessConstants.LOG_DATA_UNIT).toString(),
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        BaseResponseInfo info = new BaseResponseInfo();
        Map<String, Object> data = new HashMap<>();
        try {
            for (LcProject project : projectList) {
                lcProjectMapper.insertSelective(project);
            }
            info.code = 200;
            data.put("message", "Successfully imported " + projectList.size() + " projects");
        } catch (Exception e) {
            info.code = 500;
            data.put("message", e.getMessage());
        }
        info.data = data;
        return info;
    }
}
