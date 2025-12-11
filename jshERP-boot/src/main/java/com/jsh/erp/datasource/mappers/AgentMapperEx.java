package com.jsh.erp.datasource.mappers;

import com.jsh.erp.datasource.entities.Lcagent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentMapperEx {

    List<Lcagent> selectByCondition(@Param("name") String name,
                                    @Param("tier") String tier,
                                    @Param("phone") String phone,
                                    @Param("city") String city,
                                    @Param("state") String state,
                                    @Param("postal") String postal);
}
