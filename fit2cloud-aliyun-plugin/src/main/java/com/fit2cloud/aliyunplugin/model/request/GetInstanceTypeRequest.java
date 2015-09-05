package com.fit2cloud.aliyunplugin.model.request;

/**
 * Created by zhangbohan on 15/8/10.
 */
public class GetInstanceTypeRequest extends AliyunRequest{
    private String instanceTypeId;

    public String getInstanceTypeId() {
        return instanceTypeId;
    }

    public void setInstanceTypeId(String instanceTypeId) {
        this.instanceTypeId = instanceTypeId;
    }
}
