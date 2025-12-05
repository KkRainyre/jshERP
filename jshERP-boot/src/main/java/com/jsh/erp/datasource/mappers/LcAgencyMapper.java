package com.jsh.erp.datasource.mappers;
import com.jsh.erp.datasource.entities.LcAgency;
import com.jsh.erp.datasource.entities.LcAgencyExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LcAgencyMapper {
    long countByExample(LcAgencyExample example);
    int deleteByExample(LcAgencyExample example);
    int deleteByPrimaryKey(Long id);
    int insert(LcAgency record);
    int insertSelective(LcAgency record);
    List<LcAgency> selectByExample(LcAgencyExample example); LcAgency selectByPrimaryKey(Long id);
    int updateByExampleSelective(@Param("record") LcAgency record,
                                 @Param("example") LcAgencyExample example);
    int updateByExample(@Param("record") LcAgency record, @Param("example") LcAgencyExample example);
    int updateByPrimaryKeySelective(LcAgency record); int updateByPrimaryKey(LcAgency record);
    /** Update logo blob only */ int updateLogo(@Param("id") Long id, @Param("logo") byte[] logo);


 }