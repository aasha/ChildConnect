package com.acubeapps.service.pojo;

import java.util.List;

public class ChildPolicyDownloadResponse {
    private String status;
    private List<Error> errorList;
    private Policy policy;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Error> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<Error> errorList) {
        this.errorList = errorList;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }
}
