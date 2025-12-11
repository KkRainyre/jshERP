package com.jsh.erp.datasource.mappers;

import com.jsh.erp.datasource.entities.Lcagent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LcagentMapper {

    Lcagent selectByPrimaryKey(Long id);

    int deleteByPrimaryKey(Long id);

    int insert(Lcagent record);

    int insertSelective(Lcagent record);

    int updateByPrimaryKeySelective(Lcagent record);

    int updateByPrimaryKey(Lcagent record);

    int updateLogo(@Param("id") Long id, @Param("logo") byte[] logo);

    List<Lcagent> selectAgentsByCompany(@Param("companyId") Long companyId,
                                        @Param("offset") Integer offset,
                                        @Param("pageSize") Integer pageSize);

    int countAgentsByCompany(@Param("companyId") Long companyId);


}
