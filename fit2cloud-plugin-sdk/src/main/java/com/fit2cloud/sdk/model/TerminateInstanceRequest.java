package com.fit2cloud.sdk.model;

public class TerminateInstanceRequest extends Request {
	private String instanceId;
	private boolean keepDiskWhenTerminate;

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public boolean isKeepDiskWhenTerminate() {
		return keepDiskWhenTerminate;
	}

	public void setKeepDiskWhenTerminate(boolean keepDiskWhenTerminate) {
		this.keepDiskWhenTerminate = keepDiskWhenTerminate;
	}
}
