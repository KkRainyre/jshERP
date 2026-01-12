package com.jsh.erp.datasource.entities;

import java.util.Date;

public class LcAgency {

    private Long id;

    private String name;
    private String email;
    private String phone;

    private String remarks;

    private String address;
    private String billAddress;
    private String city;
    private String state;
    private String postal;

    private String enrollment;
    private String website;

    private String logo; // Stores OSS URL
    private byte[] logoThumb; // LONGBLOB

    private String tier;
    private String shippingAcc;
    private String EIN;

    private String EXT1;
    private String EXT2;
    private String EXT3;
    private String EXT4;
    private String EXT5;

    private Date makeTime;
    private Date modifyTime;

    private long tenantId;
    private String country;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public long getTenantId() {
        return tenantId;
    }

    public void setTenantId(long tenantId) {
        this.tenantId = tenantId;
    }
    // -------------------------------------------------
    // Getter / Setter Methods
    // -------------------------------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getBillAddress() {
        return billAddress;
    }

    public void setBillAddress(String billAddress) {
        this.billAddress = billAddress == null ? null : billAddress.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal == null ? null : postal.trim();
    }

    public String getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(String enrollment) {
        this.enrollment = enrollment == null ? null : enrollment.trim();
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website == null ? null : website.trim();
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public byte[] getLogoThumb() {
        return logoThumb;
    }

    public void setLogoThumb(byte[] logoThumb) {
        this.logoThumb = logoThumb;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier == null ? null : tier.trim();
    }

    public String getShippingAcc() {
        return shippingAcc;
    }

    public void setShippingAcc(String shippingAcc) {
        this.shippingAcc = shippingAcc == null ? null : shippingAcc.trim();
    }

    public String getEIN() {
        return EIN;
    }

    public void setEIN(String EIN) {
        this.EIN = EIN == null ? null : EIN.trim();
    }

    public String getEXT1() {
        return EXT1;
    }

    public void setEXT1(String EXT1) {
        this.EXT1 = EXT1 == null ? null : EXT1.trim();
    }

    public String getEXT2() {
        return EXT2;
    }

    public void setEXT2(String EXT2) {
        this.EXT2 = EXT2 == null ? null : EXT2.trim();
    }

    public String getEXT3() {
        return EXT3;
    }

    public void setEXT3(String EXT3) {
        this.EXT3 = EXT3 == null ? null : EXT3.trim();
    }

    public String getEXT4() {
        return EXT4;
    }

    public void setEXT4(String EXT4) {
        this.EXT4 = EXT4 == null ? null : EXT4.trim();
    }

    public String getEXT5() {
        return EXT5;
    }

    public void setEXT5(String EXT5) {
        this.EXT5 = EXT5 == null ? null : EXT5.trim();
    }

    public Date getMakeTime() {
        return makeTime;
    }

    public void setMakeTime(Date makeTime) {
        this.makeTime = makeTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

}
