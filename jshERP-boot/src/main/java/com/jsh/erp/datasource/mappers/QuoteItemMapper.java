package com.jsh.erp.datasource.mappers;

import com.jsh.erp.datasource.entities.QuoteItem;

public interface QuoteItemMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuoteItem record);

    int insertSelective(QuoteItem record);

    QuoteItem selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(QuoteItem record);

    int updateByPrimaryKey(QuoteItem record);
}
