package com.jsh.erp.datasource.mappers;

import com.jsh.erp.datasource.entities.Lcagent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LcagentMapperEx {

    List<Lcagent> selectByCondition(
            @Param("name") String name,
            @Param("category") String category,
            @Param("phone") String phone,
            @Param("address") String address,
            @Param("companyId") String companyId
    );
}
