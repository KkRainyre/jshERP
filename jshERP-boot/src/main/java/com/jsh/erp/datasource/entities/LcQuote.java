package com.jsh.erp.datasource.entities;

import java.math.BigDecimal;
import java.util.Date;

public class LcQuote {
    private Long id;

    private String quoteNo;

    private Long customerId;

    private String customerName;

    private String status;

    private String currency;

    private BigDecimal subtotal;

    private BigDecimal taxAmount;

    private BigDecimal totalAmount;

    private String ext1;

    private String ext2;

    private String ext3;

    private String ext4;

    private String ext5;

    private BigDecimal ext6;

    private BigDecimal ext7;

    private BigDecimal ext8;

    private Date ext9;

    private Date ext10;

    private Date createdTime;

    private Date updatedTime;

    private Long tenantId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuoteNo() {
        return quoteNo;
    }

    public void setQuoteNo(String quoteNo) {
        this.quoteNo = quoteNo == null ? null : quoteNo.trim();
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName == null ? null : customerName.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency == null ? null : currency.trim();
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1 == null ? null : ext1.trim();
    }

    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2 == null ? null : ext2.trim();
    }

    public String getExt3() {
        return ext3;
    }

    public void setExt3(String ext3) {
        this.ext3 = ext3 == null ? null : ext3.trim();
    }

    public String getExt4() {
        return ext4;
    }

    public void setExt4(String ext4) {
        this.ext4 = ext4 == null ? null : ext4.trim();
    }

    public String getExt5() {
        return ext5;
    }

    public void setExt5(String ext5) {
        this.ext5 = ext5 == null ? null : ext5.trim();
    }

    public BigDecimal getExt6() {
        return ext6;
    }

    public void setExt6(BigDecimal ext6) {
        this.ext6 = ext6;
    }

    public BigDecimal getExt7() {
        return ext7;
    }

    public void setExt7(BigDecimal ext7) {
        this.ext7 = ext7;
    }

    public BigDecimal getExt8() {
        return ext8;
    }

    public void setExt8(BigDecimal ext8) {
        this.ext8 = ext8;
    }

    public Date getExt9() {
        return ext9;
    }

    public void setExt9(Date ext9) {
        this.ext9 = ext9;
    }

    public Date getExt10() {
        return ext10;
    }

    public void setExt10(Date ext10) {
        this.ext10 = ext10;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}
