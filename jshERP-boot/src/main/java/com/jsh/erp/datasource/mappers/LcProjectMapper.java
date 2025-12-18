package com.jsh.erp.datasource.mappers;

import com.jsh.erp.datasource.entities.LcProject;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface LcProjectMapper {

        LcProject selectByPrimaryKey(Long id);

        int deleteByPrimaryKey(Long id);

        int insert(LcProject record);

        int insertSelective(LcProject record);

        int updateByPrimaryKeySelective(LcProject record);

        int updateByPrimaryKey(LcProject record);

        List<LcProject> selectByCondition(@Param("name") String name,
                        @Param("agency") String agency,
                        @Param("exactClient") String exactClient,
                        @Param("status") String status,
                        @Param("location") String location,
                        @Param("offset") Integer offset,
                        @Param("pageSize") Integer pageSize);

        int countByCondition(@Param("name") String name,
                        @Param("agency") String agency,
                        @Param("exactClient") String exactClient,
                        @Param("status") String status,
                        @Param("location") String location);
}
