package com.jsh.erp.datasource.entities;

import java.util.Date;

public class LcAgent {

    private Long id;

    private String employId;
    private Long companyId;
    private String title;

    private byte[] logo;

    private String address;

    private String currency;
    private String remark;

    private String phone;
    private String officeNum;
    private String phoneExt;

    private String EXT1;
    private String EXT2;
    private String EXT3;
    private String EXT4;
    private String EXT5;

    private Date makeTime;
    private Date modifyTime;

    // --------------------------------------------------
    // Getter / Setter Methods
    // --------------------------------------------------

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getEmployId() { return employId; }

    public void setEmployId(String employId) { this.employId = employId == null ? null : employId.trim(); }

    public Long getCompanyId() { return companyId; }

    public void setCompanyId(Long companyId) { this.companyId = companyId; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title == null ? null : title.trim(); }

    public byte[] getLogo() { return logo; }

    public void setLogo(byte[] logo) { this.logo = logo; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address == null ? null : address.trim(); }

    public String getCurrency() { return currency; }

    public void setCurrency(String currency) { this.currency = currency == null ? null : currency.trim(); }

    public String getRemark() { return remark; }

    public void setRemark(String remark) { this.remark = remark == null ? null : remark.trim(); }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone == null ? null : phone.trim(); }

    public String getOfficeNum() { return officeNum; }

    public void setOfficeNum(String officeNum) { this.officeNum = officeNum == null ? null : officeNum.trim(); }

    public String getPhoneExt() { return phoneExt; }

    public void setPhoneExt(String phoneExt) { this.phoneExt = phoneExt == null ? null : phoneExt.trim(); }

    public String getEXT1() { return EXT1; }

    public void setEXT1(String EXT1) { this.EXT1 = EXT1 == null ? null : EXT1.trim(); }

    public String getEXT2() { return EXT2; }

    public void setEXT2(String EXT2) { this.EXT2 = EXT2 == null ? null : EXT2.trim(); }

    public String getEXT3() { return EXT3; }

    public void setEXT3(String EXT3) { this.EXT3 = EXT3 == null ? null : EXT3.trim(); }

    public String getEXT4() { return EXT4; }

    public void setEXT4(String EXT4) { this.EXT4 = EXT4 == null ? null : EXT4.trim(); }

    public String getEXT5() { return EXT5; }

    public void setEXT5(String EXT5) { this.EXT5 = EXT5 == null ? null : EXT5.trim(); }

    public Date getMakeTime() { return makeTime; }

    public void setMakeTime(Date makeTime) { this.makeTime = makeTime; }

    public Date getModifyTime() { return modifyTime; }

    public void setModifyTime(Date modifyTime) { this.modifyTime = modifyTime; }
}
