package com.fit2cloud.aliyunplugin.model.request;

/**
 * Created by zhangbohan on 15/8/13.
 */
public class LoadBalancerResponse {
    private String loadBalancerId;
    private String loadBalancerName;

    public String getLoadBalancerId() {
        return loadBalancerId;
    }

    public void setLoadBalancerId(String loadBalancerId) {
        this.loadBalancerId = loadBalancerId;
    }

    public String getLoadBalancerName() {
        return loadBalancerName;
    }

    public void setLoadBalancerName(String loadBalancerName) {
        this.loadBalancerName = loadBalancerName;
    }
}
