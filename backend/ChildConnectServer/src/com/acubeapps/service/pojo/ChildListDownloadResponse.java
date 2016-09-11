package com.acubeapps.service.pojo;

import java.util.List;

import com.acubeapps.storage.dynamodb.pojo.ChildLoginDetails;

public class ChildListDownloadResponse {
    private List<ChildLoginDetails> childDetailList;
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

	public List<ChildLoginDetails> getChildDetailList() {
		return childDetailList;
	}

	public void setChildDetailList(List<ChildLoginDetails> childDetailList) {
		this.childDetailList = childDetailList;
	}
}
