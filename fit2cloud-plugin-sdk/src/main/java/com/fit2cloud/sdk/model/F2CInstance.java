package com.fit2cloud.sdk.model;

import java.util.Date;

//FIT2CLOUD定义的虚机模型
public class F2CInstance {
	private String name;
	private String imageId;
	private String os;
	private String instanceId;
	private String instanceStatus;
	private String instanceType;
	private String instanceTypeDescription;
	private String region;
	private String zone;
	private String remoteIP;
	private String localIP;
	private String description;
	private Date created;
	private String hostname;
	private String customData;
	
	private Long keypasswordId;
	private int sshPort;
	private String sshUser;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public String getInstanceStatus() {
		return instanceStatus;
	}
	public void setInstanceStatus(String instanceStatus) {
		this.instanceStatus = instanceStatus;
	}
	public String getInstanceType() {
		return instanceType;
	}
	public void setInstanceType(String instanceType) {
		this.instanceType = instanceType;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getRemoteIP() {
		return remoteIP;
	}
	public void setRemoteIP(String remoteIP) {
		this.remoteIP = remoteIP;
	}
	public String getLocalIP() {
		return localIP;
	}
	public void setLocalIP(String localIP) {
		this.localIP = localIP;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public String getCustomData() {
		return customData;
	}
	public void setCustomData(String customData) {
		this.customData = customData;
	}
	public Long getKeypasswordId() {
		return keypasswordId;
	}
	public void setKeypasswordId(Long keypasswordId) {
		this.keypasswordId = keypasswordId;
	}
	public int getSshPort() {
		return sshPort;
	}
	public void setSshPort(int sshPort) {
		this.sshPort = sshPort;
	}
	public String getSshUser() {
		return sshUser;
	}
	public void setSshUser(String sshUser) {
		this.sshUser = sshUser;
	}
	public String getInstanceTypeDescription() {
		return instanceTypeDescription;
	}
	public void setInstanceTypeDescription(String instanceTypeDescription) {
		this.instanceTypeDescription = instanceTypeDescription;
	}
}
