package com.acubeapps.service.pojo;

import java.util.List;

public class GsmRegisterResponse {
	private String status;
    private List<Error> errorList;

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
}
