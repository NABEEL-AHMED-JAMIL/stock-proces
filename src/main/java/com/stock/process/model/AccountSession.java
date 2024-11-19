package com.stock.process.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author Nabeel Ahmed
 */
@Entity
@Table(name = "account_session")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountSession {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sw_user_id", nullable = false)
    private SwUser swUser;

    @Column(name = "otp", nullable = false)
    private String otp;

    @Column(name = "otp_expired_at", nullable = false)
    private Timestamp otpExpiredAt;

    @Column(name = "device_id", nullable = false)
    private String deviceId;

    @Column(name = "expired_at", nullable = false)
    private Timestamp expiredAt;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = new Timestamp((System.currentTimeMillis()));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SwUser getSwUser() {
        return swUser;
    }

    public void setSwUser(SwUser swUser) {
        this.swUser = swUser;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Timestamp getOtpExpiredAt() {
        return otpExpiredAt;
    }

    public void setOtpExpiredAt(Timestamp otpExpiredAt) {
        this.otpExpiredAt = otpExpiredAt;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Timestamp getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Timestamp expiredAt) {
        this.expiredAt = expiredAt;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
