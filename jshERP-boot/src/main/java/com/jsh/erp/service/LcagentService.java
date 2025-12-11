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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class LcagentService {

    private Logger logger = LoggerFactory.getLogger(LcagentService.class);

    @Resource
    private LcagentMapper lcagentMapper;

    @Resource
    private LcagentMapperEx lcagentMapperEx;

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

    // ---------------------------------------------------------------------
    // UPDATE LOGO
    // ---------------------------------------------------------------------
    public int updateLogo(Long id, byte[] logo) {
        try {
            return lcagentMapper.updateLogo(id, logo);
        } catch (Exception e) {
            JshException.writeFail(logger, e);
            return -1;
        }
    }
}
