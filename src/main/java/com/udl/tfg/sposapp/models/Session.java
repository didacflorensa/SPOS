package com.udl.tfg.sposapp.models;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.annotation.Nullable;
import javax.persistence.*;

@Entity
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;

    @NotBlank(message = "You must provide a valid email address")
    @Email
    private String email;

    @NotBlank(message = "You must choose a solution type")
    private SolutionType type;

    @ManyToOne
    private VirtualMachine vmConfig;

    @ManyToOne
    private Parameters info;

    @OneToOne
    private Result sessionResults;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public SolutionType getType() {
        return type;
    }

    public void setType(SolutionType type) {
        this.type = type;
    }

    public VirtualMachine getVmConfig() {
        return vmConfig;
    }

    public void setVmConfig(VirtualMachine vmConfig) {
        this.vmConfig = vmConfig;
    }

    public Parameters getInfo() {
        return info;
    }

    public void setInfo(Parameters info) {
        this.info = info;
    }

    public Result getSessionResults() {
        return sessionResults;
    }

    public void setSessionResults(Result sessionResults) {
        this.sessionResults = sessionResults;
    }
}
