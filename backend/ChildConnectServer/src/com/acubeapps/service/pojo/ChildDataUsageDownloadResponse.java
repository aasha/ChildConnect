package com.acubeapps.service.pojo;

import java.util.List;

public class ChildDataUsageDownloadResponse {
    private String status;
    private List<Error> errorList;
    private List<AppUsage> appUsage;
    private List<String> browserHistory;

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

    public List<AppUsage> getAppUsage() {
        return appUsage;
    }

    public void setAppUsage(List<AppUsage> appUsage) {
        this.appUsage = appUsage;
    }

	public List<String> getBrowserHistory() {
		return browserHistory;
	}

	public void setBrowserHistory(List<String> browserHistory) {
		this.browserHistory = browserHistory;
	}
}
