package com.udl.tfg.sposapp.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class MethodInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;

    @NotNull(message = "You must specify method type")
    @Enumerated(EnumType.STRING)
    private MethodCodes method;

    private String name;

    @NotNull(message = "This field cannot be empty")
    private boolean mpsSupport;

    @NotNull(message = "This field cannot be empty")
    private boolean lpSupport;

    @NotNull(message = "This field cannot be empty")
    private boolean pyomoSupport;

    @NotNull(message = "This field cannot be empty")
    private boolean decompositionSupport;

    @NotNull(message = "This field cannot be empty")
    private boolean parallelizationSupport;

    @NotNull(message = "This field cannot be empty")
    private boolean clusterSupport;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public MethodCodes getMethod() {
        return method;
    }

    public void setMethod(MethodCodes method) {
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMpsSupport() {
        return mpsSupport;
    }

    public void setMpsSupport(boolean mpsSupport) {
        this.mpsSupport = mpsSupport;
    }

    public boolean isLpSupport() {
        return lpSupport;
    }

    public void setLpSupport(boolean lpSupport) {
        this.lpSupport = lpSupport;
    }

    public boolean isDecompositionSupport() {
        return decompositionSupport;
    }

    public void setDecompositionSupport(boolean decompositionSupport) {
        this.decompositionSupport = decompositionSupport;
    }

    public boolean isPyomoSupport() {
        return pyomoSupport;
    }

    public void setPyomotSupport(boolean pyomoSupport) {
        this.pyomoSupport = pyomoSupport;
    }

    public boolean isParallelizationSupport() {
        return parallelizationSupport;
    }

    public void setParallelizationSupport(boolean parallelizationSupport) {
        this.parallelizationSupport = parallelizationSupport;
    }

    public boolean isClusterSupport() {
        return clusterSupport;
    }

    public void setClusterSupport(boolean clusterSupport) {
        this.clusterSupport = clusterSupport;
    }
}
