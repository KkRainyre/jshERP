package com.jsh.erp.datasource.mappers;

import com.jsh.erp.datasource.entities.LcQuote;

public interface LcQuoteMapper {
    int deleteByPrimaryKey(Long id);

    int insert(LcQuote record);

    int insertSelective(LcQuote record);

    LcQuote selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(LcQuote record);

    int updateByPrimaryKey(LcQuote record);
}
