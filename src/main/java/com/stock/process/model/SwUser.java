package com.stock.process.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;
import com.stock.process.enums.Status;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Nabeel Ahmed
 */
@Entity
@Table(name = "sw_user")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SwUser {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "sw_account", nullable = false, unique = true)
    private String swAccount;

    @OneToMany(mappedBy = "swUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SWTask> swTasks;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @PrePersist
    public void onCreate() {
        this.status = Status.Active;
        this.createdAt = new Timestamp((System.currentTimeMillis()));
    }

    public SwUser() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSwAccount() {
        return swAccount;
    }

    public void setSwAccount(String swAccount) {
        this.swAccount = swAccount;
    }

    public List<SWTask> getSwTasks() {
        return swTasks;
    }

    public void setSwTasks(List<SWTask> swTasks) {
        this.swTasks = swTasks;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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
