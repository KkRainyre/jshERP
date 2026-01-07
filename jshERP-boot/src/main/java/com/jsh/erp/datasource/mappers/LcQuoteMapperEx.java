package com.jsh.erp.datasource.mappers;

import com.jsh.erp.datasource.entities.LcQuote;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LcQuoteMapperEx {
        List<LcQuote> selectByCondition(
                        @Param("quoteNo") String quoteNo,
                        @Param("customerName") String customerName,
                        @Param("status") String status,
                        @Param("minPrice") String minPrice,
                        @Param("maxPrice") String maxPrice,
                        @Param("projectName") String projectName);

        Long countsByCondition(
                        @Param("quoteNo") String quoteNo,
                        @Param("customerName") String customerName,
                        @Param("status") String status,
                        @Param("minPrice") String minPrice,
                        @Param("maxPrice") String maxPrice,
                        @Param("projectName") String projectName);
}
