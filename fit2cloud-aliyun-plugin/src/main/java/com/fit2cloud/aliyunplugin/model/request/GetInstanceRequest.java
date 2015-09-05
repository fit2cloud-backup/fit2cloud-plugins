package com.fit2cloud.aliyunplugin.model.request;

/**
 * Created by zhangbohan on 15/8/11.
 */
public class GetInstanceRequest extends AliyunRequest {
    private String instanceId;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}
