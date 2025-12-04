package com.jsh.erp.datasource.mappers;

import com.jsh.erp.datasource.entities.LcAgent;
//import com.jsh.erp.datasource.entities.LcAgentExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LcAgentMapper {

//    long countByExample(LcAgentExample example);
//
//    int deleteByExample(LcAgentExample example);

    int deleteByPrimaryKey(Long id);

    int insert(LcAgent record);

    int insertSelective(LcAgent record);

//    List<LcAgent> selectByExample(LcAgentExample example);

    LcAgent selectByPrimaryKey(Long id);

//    int updateByExampleSelective(@Param("record") LcAgent record,
//                                 @Param("example") LcAgentExample example);
//
//    int updateByExample(@Param("record") LcAgent record,
//                        @Param("example") LcAgentExample example);

    int updateByPrimaryKeySelective(LcAgent record);

    int updateByPrimaryKey(LcAgent record);

    /** Update logo blob only */
    int updateLogo(@Param("id") Long id,
                   @Param("logo") byte[] logo);
}
