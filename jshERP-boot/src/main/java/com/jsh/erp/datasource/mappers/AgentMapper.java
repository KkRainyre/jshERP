package com.jsh.erp.datasource.mappers;

import com.jsh.erp.datasource.entities.Lcagent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Lcagent record);

    int insertSelective(Lcagent record);

    Lcagent selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Lcagent record);

    int updateByPrimaryKey(Lcagent record);

    // Optional: if you use examples
    List<Lcagent> selectByExample(Object example);

    // Custom logo update
    int updateLogo(@Param("id") Long id, @Param("logo") byte[] logo);
}
