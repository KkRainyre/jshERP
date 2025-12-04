package com.jsh.erp.service;

import com.alibaba.fastjson.JSONObject;
import com.jsh.erp.datasource.entities.LcAgency;
import com.jsh.erp.datasource.entities.LcAgencyExample;
import com.jsh.erp.datasource.mappers.LcAgencyMapper;
import com.jsh.erp.datasource.mappers.LcAgencyMapperEx;
import com.jsh.erp.exception.JshException;
import com.jsh.erp.utils.PageUtils;
import com.jsh.erp.utils.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class AgencyService {

    private Logger logger = LoggerFactory.getLogger(AgencyService.class);

    @Resource
    private LcAgencyMapper lcAgencyMapper;

    @Resource
    private LcAgencyMapperEx lcAgencyMapperEx;

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
    public List<LcAgency> select(String name, String tier, String phone, String city,String state, String postal) throws Exception {
        try {
            PageUtils.startPage();
            return lcAgencyMapperEx.selectByCondition(name,tier,phone,city, state,postal);
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



    public int updateLogo(Long id, byte[] logo) {
        return lcAgencyMapper.updateLogo(id, logo);
    }
}
