package com.neu.webapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("inbound_package")
public class InboundPackage {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long packageId;
    private String pickupCode;
    private String cabinetType;
    private String proxyPhone;
    private String status;
    private Integer isAutoCheckout;
    private Long enteredBy;
    private LocalDateTime enterTime;
    private LocalDateTime outTime;
    private LocalDateTime autoCheckoutTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPackageId() { return packageId; }
    public void setPackageId(Long packageId) { this.packageId = packageId; }
    public String getPickupCode() { return pickupCode; }
    public void setPickupCode(String pickupCode) { this.pickupCode = pickupCode; }
    public String getCabinetType() { return cabinetType; }
    public void setCabinetType(String cabinetType) { this.cabinetType = cabinetType; }
    public String getProxyPhone() { return proxyPhone; }
    public void setProxyPhone(String proxyPhone) { this.proxyPhone = proxyPhone; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getIsAutoCheckout() { return isAutoCheckout; }
    public void setIsAutoCheckout(Integer isAutoCheckout) { this.isAutoCheckout = isAutoCheckout; }
    public Long getEnteredBy() { return enteredBy; }
    public void setEnteredBy(Long enteredBy) { this.enteredBy = enteredBy; }
    public LocalDateTime getEnterTime() { return enterTime; }
    public void setEnterTime(LocalDateTime enterTime) { this.enterTime = enterTime; }
    public LocalDateTime getOutTime() { return outTime; }
    public void setOutTime(LocalDateTime outTime) { this.outTime = outTime; }
    public LocalDateTime getAutoCheckoutTime() { return autoCheckoutTime; }
    public void setAutoCheckoutTime(LocalDateTime autoCheckoutTime) { this.autoCheckoutTime = autoCheckoutTime; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
