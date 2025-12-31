package com.jsh.erp.datasource.entities;

import java.math.BigDecimal;
import java.util.Date;

public class QuoteItem {
    private Long id;

    private Long quoteId;

    private String itemType;

    private Long productId;

    private String productSeries;

    private String productModel;

    private String skuCode;

    private String itemName;

    private String description;

    private String lightingType;

    private String mounting;

    private String profileType;

    private String shape;

    private String dimensions;

    private String lengthSpec;

    private String profileFinish;

    private String outputSpec;

    private String cct;

    private String cri;

    private String beamAngle;

    private String voltage;

    private String dimming;

    private String driverType;

    private String ipRating;

    private BigDecimal quantity;

    private BigDecimal unitPrice;

    private BigDecimal lineTotal;

    private Integer sortOrder;

    private Byte printFlag;

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

    public Long getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(Long quoteId) {
        this.quoteId = quoteId;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType == null ? null : itemType.trim();
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductSeries() {
        return productSeries;
    }

    public void setProductSeries(String productSeries) {
        this.productSeries = productSeries == null ? null : productSeries.trim();
    }

    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel == null ? null : productModel.trim();
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode == null ? null : skuCode.trim();
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName == null ? null : itemName.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getLightingType() {
        return lightingType;
    }

    public void setLightingType(String lightingType) {
        this.lightingType = lightingType == null ? null : lightingType.trim();
    }

    public String getMounting() {
        return mounting;
    }

    public void setMounting(String mounting) {
        this.mounting = mounting == null ? null : mounting.trim();
    }

    public String getProfileType() {
        return profileType;
    }

    public void setProfileType(String profileType) {
        this.profileType = profileType == null ? null : profileType.trim();
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape == null ? null : shape.trim();
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions == null ? null : dimensions.trim();
    }

    public String getLengthSpec() {
        return lengthSpec;
    }

    public void setLengthSpec(String lengthSpec) {
        this.lengthSpec = lengthSpec == null ? null : lengthSpec.trim();
    }

    public String getProfileFinish() {
        return profileFinish;
    }

    public void setProfileFinish(String profileFinish) {
        this.profileFinish = profileFinish == null ? null : profileFinish.trim();
    }

    public String getOutputSpec() {
        return outputSpec;
    }

    public void setOutputSpec(String outputSpec) {
        this.outputSpec = outputSpec == null ? null : outputSpec.trim();
    }

    public String getCct() {
        return cct;
    }

    public void setCct(String cct) {
        this.cct = cct == null ? null : cct.trim();
    }

    public String getCri() {
        return cri;
    }

    public void setCri(String cri) {
        this.cri = cri == null ? null : cri.trim();
    }

    public String getBeamAngle() {
        return beamAngle;
    }

    public void setBeamAngle(String beamAngle) {
        this.beamAngle = beamAngle == null ? null : beamAngle.trim();
    }

    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage == null ? null : voltage.trim();
    }

    public String getDimming() {
        return dimming;
    }

    public void setDimming(String dimming) {
        this.dimming = dimming == null ? null : dimming.trim();
    }

    public String getDriverType() {
        return driverType;
    }

    public void setDriverType(String driverType) {
        this.driverType = driverType == null ? null : driverType.trim();
    }

    public String getIpRating() {
        return ipRating;
    }

    public void setIpRating(String ipRating) {
        this.ipRating = ipRating == null ? null : ipRating.trim();
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(BigDecimal lineTotal) {
        this.lineTotal = lineTotal;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Byte getPrintFlag() {
        return printFlag;
    }

    public void setPrintFlag(Byte printFlag) {
        this.printFlag = printFlag;
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
}
