package com.fit2cloud.sdk.model;

public class F2CLoadBalancer {
	private String id;
	private String name;
	private Long createTime;
	private String status;
	private String address;
	private String addressType;
	private Integer bandwidth;
	private String region;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddressType() {
		return addressType;
	}
	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}
	public Integer getBandwidth() {
		return bandwidth;
	}
	public void setBandwidth(Integer bandwidth) {
		this.bandwidth = bandwidth;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
}
