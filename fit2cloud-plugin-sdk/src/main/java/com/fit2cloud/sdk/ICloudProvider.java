package com.fit2cloud.sdk;

import java.util.List;

import com.fit2cloud.sdk.model.F2CInstance;
import com.fit2cloud.sdk.model.F2CLoadBalancer;
import com.fit2cloud.sdk.model.Request;
import com.fit2cloud.sdk.model.StartInstanceRequest;
import com.fit2cloud.sdk.model.StopInstanceRequest;
import com.fit2cloud.sdk.model.TerminateInstanceRequest;

public interface ICloudProvider {
	//返回插件名称
	String getName();
    //是否支持创建虚机
    boolean isSupportLaunchInstance();
    //是否支持userdata
    boolean isSupportUserData();
    //验证云帐号是否有效
	boolean validateCredential(Request cloudCredential) throws PluginException;
    //创建并启动虚机实例
    F2CInstance launchInstance(String launchInstanceRequest) throws PluginException;
    //验证启动虚机配置,如果不能通过验证抛出异常
    void validateLaunchInstanceConfiguration(String launchInstanceConfiguration) throws PluginException;
    //删除虚机实例
    boolean terminateInstance(TerminateInstanceRequest terminateInstanceRequest) throws PluginException;
    //启动虚机实例
    F2CInstance startInstance(StartInstanceRequest startInstanceRequest) throws PluginException;
    //停止虚机实例
    boolean stopInstance(StopInstanceRequest stopInstanceRequest) throws PluginException;
    //获取全部实例列表
    List<F2CInstance> getF2CInstances(String getInstancesRequest) throws PluginException;
    //获取单个实例的信息
    F2CInstance getF2CInstance(String getInstanceRequest) throws PluginException;
    //获取全部负载均衡器列表
    List<F2CLoadBalancer> getF2CLoadBalancers(String getLoadBalancersRequest) throws PluginException;
}
