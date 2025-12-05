package com.jsh.erp.datasource.mappers;

import com.jsh.erp.datasource.entities.LcAgency;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LcAgencyMapperEx {

    /**
     * Search agencies by name, city, and state
     */
    List<LcAgency> selectByCondition(
            @Param("name") String name,
            @Param("tier") String tier,
            @Param("phone") String phone,
            @Param("city") String city,
            @Param("state") String state,
            @Param("postal") String postal
    );

    /**
     * Update agency logo (BLOB)
     */
    int updateLogo(
            @Param("id") Long id,
            @Param("logo") byte[] logo
    );
}
