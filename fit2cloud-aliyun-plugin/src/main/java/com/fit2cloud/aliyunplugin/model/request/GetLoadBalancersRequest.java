package com.fit2cloud.aliyunplugin.model.request;

/**
 * Created by zhangbohan on 15/8/13.
 */
public class GetLoadBalancersRequest extends AliyunRequest{
    private String vpcId;

    public String getVpcId() {
        return vpcId;
    }

    public void setVpcId(String vpcId) {
        this.vpcId = vpcId;
    }
}
