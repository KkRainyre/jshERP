package com.jsh.erp.datasource.mappers;

import com.jsh.erp.datasource.entities.QuoteItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuoteItemMapperEx {
    List<QuoteItem> selectByQuoteId(@Param("quoteId") Long quoteId);
}
