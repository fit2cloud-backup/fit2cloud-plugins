package com.fit2cloud.sdk.model;

/**
 * Created by zhangbohan on 15/8/3.
 */
public class Request {
    private String credential;
    private String regionId;

	public String getCredential() {
		return credential;
	}

	public void setCredential(String credential) {
		this.credential = credential;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
}