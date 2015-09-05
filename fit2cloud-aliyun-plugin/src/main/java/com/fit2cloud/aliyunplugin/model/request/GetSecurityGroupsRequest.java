package com.fit2cloud.aliyunplugin.model.request;

/**
 * Created by zhangbohan on 15/8/11.
 */
public class GetSecurityGroupsRequest extends AliyunRequest{
    private String vpcId;

    public String getVpcId() {
        return vpcId;
    }

    public void setVpcId(String vpcId) {
        this.vpcId = vpcId;
    }
}
