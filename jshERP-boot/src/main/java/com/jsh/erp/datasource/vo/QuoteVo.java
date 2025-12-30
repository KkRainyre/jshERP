package com.jsh.erp.datasource.vo;

import com.jsh.erp.datasource.entities.LcQuote;
import com.jsh.erp.datasource.entities.QuoteItem;

import java.util.List;

public class QuoteVo extends LcQuote {

    private List<QuoteItem> items;

    public List<QuoteItem> getItems() {
        return items;
    }

    public void setItems(List<QuoteItem> items) {
        this.items = items;
    }
}
