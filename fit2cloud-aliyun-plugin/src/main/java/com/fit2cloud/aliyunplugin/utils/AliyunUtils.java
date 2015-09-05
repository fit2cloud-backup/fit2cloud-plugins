package com.fit2cloud.aliyunplugin.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.aliyuncs.ecs.model.v20140526.DescribeInstanceTypesResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.fit2cloud.aliyunplugin.model.request.AliyunRequest;
import com.fit2cloud.sdk.model.F2CInstance;
import com.fit2cloud.sdk.model.Request;
import com.google.gson.Gson;

/**
 * Created by zhangbohan on 15/8/10.
 */
public class AliyunUtils {
    public static String PAY_BY_BAND_WIDTH = "PayByBandwidth";
    public static String PAY_BY_TRAFFIC = "PayByTraffic";

    private static List<String> internetChargeTypes;
    static {
        internetChargeTypes = new ArrayList<String>();
        internetChargeTypes.add("PayByBandwidth");
        internetChargeTypes.add("PayByTraffic");
    }
    public static List<String> getgetInternetChargeTypes(){
        return internetChargeTypes;
    }
    public static AliyunRequest transRequest2AliyunRequest(Request request) {
        AliyunRequest aliyunRequest = new AliyunRequest();
        aliyunRequest.setCredential(request.getCredential());
        aliyunRequest.setRegionId(request.getRegionId());
        return aliyunRequest;
    }
    public static F2CInstance toF2CInstance(DescribeInstancesResponse.Instance instance,DescribeInstanceTypesResponse.InstanceType instanceTypeObj) {
        F2CInstance f2cInstance = new F2CInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        try {
            f2cInstance.setCreated(sdf.parse(instance.getCreationTime()));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        f2cInstance.setRegion(instance.getRegionId());
        f2cInstance.setHostname(instance.getHostName());
        f2cInstance.setImageId(instance.getImageId());
        f2cInstance.setInstanceId(instance.getInstanceId());
        f2cInstance.setInstanceStatus(toF2CInstanceStatus(instance.getStatus()));
        String instanceType = instance.getInstanceType();
        f2cInstance.setInstanceType(instanceType);
        if(instanceTypeObj!=null){
            f2cInstance.setInstanceTypeDescription(instanceTypeObj.getCpuCoreCount() + "核 " + Math.round(instanceTypeObj.getMemorySize()) + "G");
        }
        if(instance.getPublicIpAddress().size()>0){
            f2cInstance.setRemoteIP(instance.getPublicIpAddress().get(0));
        }
        if(instance.getEipAddress()!=null){
            if(instance.getEipAddress().getIpAddress()!=null&&instance.getEipAddress().getIpAddress().trim().length()>0){
                f2cInstance.setRemoteIP(instance.getEipAddress().getIpAddress());
            }
        }
        if(instance.getInnerIpAddress().size()>0){
            f2cInstance.setLocalIP(instance.getInnerIpAddress().get(0));
        }

        f2cInstance.setName(instance.getInstanceName());
        String name = f2cInstance.getName();
        if(name == null || name.trim().length() == 0) {
            f2cInstance.setName(instance.getInstanceId());
        }else {
        	if(name.length() > 64) {
        		name = name.substring(0, 61) + "...";
        	}
        	f2cInstance.setName(name);
        }

//    	f2cInstance.setOs(os);
        f2cInstance.setZone(instance.getZoneId());

        instance.setCreationTime(null);
        instance.setDescription(null);
        instance.setInstanceId(null);
        instance.setInstanceName(null);
        instance.setHostName(null);
        instance.setStatus(null);
        instance.setInnerIpAddress(null);
        instance.setImageId(null);

        f2cInstance.setCustomData(new Gson().toJson(instance));
        return f2cInstance;
    }

    public static String toF2CInstanceStatus(DescribeInstancesResponse.Instance.Status aliyunStatus) {
        if (aliyunStatus == DescribeInstancesResponse.Instance.Status.RUNNING){
            return "Running";
        }else if (aliyunStatus == DescribeInstancesResponse.Instance.Status.STOPPING){
            return "Stopping";
        }else if (aliyunStatus == DescribeInstancesResponse.Instance.Status.STOPPED){
            return "Stopped";
        }else if (aliyunStatus == DescribeInstancesResponse.Instance.Status.DELETED){
            return "Deleted";
        }else if (aliyunStatus == DescribeInstancesResponse.Instance.Status.STARTING){
            return "Starting";
        }
        return "Unknown";
    }

}
