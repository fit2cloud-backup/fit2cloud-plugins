package com.fit2cloud.aliyunplugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.AllocateEipAddressRequest;
import com.aliyuncs.ecs.model.v20140526.AllocateEipAddressResponse;
import com.aliyuncs.ecs.model.v20140526.AllocatePublicIpAddressRequest;
import com.aliyuncs.ecs.model.v20140526.AllocatePublicIpAddressResponse;
import com.aliyuncs.ecs.model.v20140526.AssociateEipAddressRequest;
import com.aliyuncs.ecs.model.v20140526.CreateInstanceRequest;
import com.aliyuncs.ecs.model.v20140526.CreateInstanceResponse;
import com.aliyuncs.ecs.model.v20140526.DeleteInstanceRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeEipAddressesRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeEipAddressesResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeImagesRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeImagesResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeInstanceTypesRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeInstanceTypesResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeRegionsRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeRegionsResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeSecurityGroupsRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeSecurityGroupsResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeVSwitchesRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeVSwitchesResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeVpcsRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeVpcsResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeZonesRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeZonesResponse;
import com.aliyuncs.ecs.model.v20140526.ReleaseEipAddressRequest;
import com.aliyuncs.ecs.model.v20140526.StartInstanceResponse;
import com.aliyuncs.ecs.model.v20140526.UnassociateEipAddressRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.slb.model.v20140515.AddBackendServersRequest;
import com.aliyuncs.slb.model.v20140515.DescribeLoadBalancerAttributeRequest;
import com.aliyuncs.slb.model.v20140515.DescribeLoadBalancerAttributeResponse;
import com.aliyuncs.slb.model.v20140515.DescribeLoadBalancersRequest;
import com.aliyuncs.slb.model.v20140515.DescribeLoadBalancersResponse;
import com.fit2cloud.aliyunplugin.model.request.AliyunRequest;
import com.fit2cloud.aliyunplugin.model.request.GetInstanceRequest;
import com.fit2cloud.aliyunplugin.model.request.GetLoadBalancersRequest;
import com.fit2cloud.aliyunplugin.model.request.GetSecurityGroupsRequest;
import com.fit2cloud.aliyunplugin.model.request.GetVSwitchesRequest;
import com.fit2cloud.aliyunplugin.model.request.LaunchInstanceRequest;
import com.fit2cloud.aliyunplugin.model.request.LoadBalancerResponse;
import com.fit2cloud.aliyunplugin.utils.AliyunUtils;
import com.fit2cloud.sdk.AbstractCloudProvider;
import com.fit2cloud.sdk.F2CPlugin;
import com.fit2cloud.sdk.PluginException;
import com.fit2cloud.sdk.model.F2CInstance;
import com.fit2cloud.sdk.model.F2CLoadBalancer;
import com.fit2cloud.sdk.model.Request;
import com.fit2cloud.sdk.model.StartInstanceRequest;
import com.fit2cloud.sdk.model.StopInstanceRequest;
import com.fit2cloud.sdk.model.TerminateInstanceRequest;
import com.google.gson.Gson;

/**
 * Created by zhangbohan on 15/8/9.
 */
@F2CPlugin
public class AliyunCloudprovider extends AbstractCloudProvider{
    private static String name = "fit2cloud-aliyun-plugin";
    public String getName() {
        return name;
    }

    public boolean isSupportLaunchInstance() {
        return true;
    }

    public boolean isSupportUserData() {
        return false;
    }

    public boolean validateCredential(Request validateCredentialRequest) throws PluginException {
        AliyunRequest req = AliyunUtils.transRequest2AliyunRequest(validateCredentialRequest);
        IAcsClient aliyunClient = req.getAliyunClient();
        DescribeRegionsRequest describeRegionsRequest = new DescribeRegionsRequest();
        try {
            DescribeRegionsResponse describeRegionsResponse = aliyunClient.getAcsResponse(describeRegionsRequest);
            describeRegionsResponse.getRegions();
            return true;
        } catch (ClientException e) {
            e.printStackTrace();
            throw new PluginException(e.getMessage(),e);
        }
    }

    public F2CInstance launchInstance(String launchInstanceRequest) throws PluginException {
        LaunchInstanceRequest req;
        DescribeInstancesResponse.Instance instance = null;
        Gson gson = new Gson();
        req = gson.fromJson(launchInstanceRequest, LaunchInstanceRequest.class);
        CreateInstanceRequest createInstanceRequest = req.toCreateInstanceRequest();
        IAcsClient aliyunClient = req.getAliyunClient();
        try {
            CreateInstanceResponse createInstanceResponse = aliyunClient.getAcsResponse(createInstanceRequest);
            String instanceId =  createInstanceResponse.getInstanceId();

            DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
            describeInstancesRequest.setRegionId(req.getRegionId());
            Collection<String> instancesList = new ArrayList<String>();
            if(instanceId !=null){
                instancesList.add(instanceId);
            }
            describeInstancesRequest.setInstanceIds(new Gson().toJson(instancesList));
            int count = 0;
            boolean instanceCreated = false;
            while(true){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }
                System.out.println("check for instance["+instanceId+"] status for "+ ++count +" times!");
                DescribeInstancesResponse result = aliyunClient.getAcsResponse(describeInstancesRequest);
                List<DescribeInstancesResponse.Instance> instances = result.getInstances();
                if(instances != null && instances.size() > 0) {
                    instance = instances.get(0);
                    String aliyunStatus = "";
                    if(instance.getStatus()!=null){
                        aliyunStatus = instance.getStatus().getStringValue();
                    }
                    System.out.println("instance["+instanceId+"] current status :: "+aliyunStatus);
                    if (aliyunStatus.equals("Stopped")){
                        System.out.println("success to create instance!");
                        instanceCreated = true;
                        break;
                    }
                }
                if(count >= 10){
                    System.out.println("Create instance timeout!");
                    break;
                }
            }

            if(instanceCreated){
                if(req.getvSwitchId()!=null&&req.getvSwitchId().trim().length()>0){
                    if(req.isHasEip()){
                        //AllocateEipAddress
                        AllocateEipAddressRequest allocateEipAddressRequest = new AllocateEipAddressRequest();
                        allocateEipAddressRequest.setRegionId(req.getRegionId());
                        AllocateEipAddressResponse allocateEipAddressResponse = aliyunClient.getAcsResponse(allocateEipAddressRequest);
                        String eipId = allocateEipAddressResponse.getAllocationId();
                        System.out.println("success to allocate eip, eip ip is :: " + allocateEipAddressResponse.getEipAddress());
                        //AllocateEipAddress
                        AssociateEipAddressRequest associateEipAddressRequest = new AssociateEipAddressRequest();
                        associateEipAddressRequest.setAllocationId(eipId);
                        associateEipAddressRequest.setInstanceId(instanceId);
                        aliyunClient.getAcsResponse(associateEipAddressRequest);
                        System.out.println("success to bind eip:" + allocateEipAddressResponse.getEipAddress());
                    }
                }else {
                    if(req.isHasPublicIp()){
                        AllocatePublicIpAddressRequest allocatePublicIpAddressRequest = new AllocatePublicIpAddressRequest();
                        allocatePublicIpAddressRequest.setInstanceId(instanceId);
                        AllocatePublicIpAddressResponse allocatePublicIpAddressResponse = aliyunClient.getAcsResponse(allocatePublicIpAddressRequest);
                        String publicIp = allocatePublicIpAddressResponse.getIpAddress();
                        System.out.println("success to allocate public ip, public ip is :: " + publicIp);
                    }
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e){
                }
                com.aliyuncs.ecs.model.v20140526.StartInstanceRequest startInstanceRequest =
                        new com.aliyuncs.ecs.model.v20140526.StartInstanceRequest();
                startInstanceRequest.setInstanceId(instanceId);

                StartInstanceResponse startInstanceResponse = aliyunClient.getAcsResponse(startInstanceRequest);
                if(startInstanceResponse.getRequestId()!=null&&startInstanceResponse.getRequestId().length()>0){
                    System.out.println("start instance :: " + instanceId);
                }else{
                    throw new PluginException("启动虚机失败!");
                }

                count = 0;
                while(true){
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }
                    System.out.println("check for instance["+instanceId+"] status for "+ ++count +" times!");
                    DescribeInstancesResponse result1 = aliyunClient.getAcsResponse(describeInstancesRequest);
                    List<DescribeInstancesResponse.Instance> instances1 = result1.getInstances();
                    if(instances1 != null && instances1.size() > 0) {
                        instance = instances1.get(0);
                        String aliyunStatus = instance.getStatus().getStringValue();
                        System.out.println("instance["+instanceId+"] current status :: "+aliyunStatus);
                        if (aliyunStatus.equals("Running")){
                            if(req.getLoadBalancerId()!=null&&req.getLoadBalancerId().trim().length()>0){
                                AddBackendServersRequest addBackendServersRequest = new AddBackendServersRequest();
                                addBackendServersRequest.setLoadBalancerId(req.getLoadBalancerId());
                                DescribeLoadBalancerAttributeResponse.BackendServer bs = new DescribeLoadBalancerAttributeResponse.BackendServer();
                                bs.setServerId(instanceId);
                                List<DescribeLoadBalancerAttributeResponse.BackendServer> list = new ArrayList<DescribeLoadBalancerAttributeResponse.BackendServer>();
                                list.add(bs);
                                addBackendServersRequest.setBackendServers(new Gson().toJson(list));
                                aliyunClient.getAcsResponse(addBackendServersRequest);
                            }
                            break;
                        }
                    }
                    if(count >= 40){
                        break;
                    }
                }
            }

            if(instance != null) {
                System.out.println(new Gson().toJson(instance));
                DescribeInstanceTypesResponse.InstanceType instanceType = getInstanceType(instance.getInstanceType(),aliyunClient);
                F2CInstance f2cInstance = AliyunUtils.toF2CInstance(instance,instanceType);
                return f2cInstance;
            }else {
                System.out.println("Failed to create instance!!");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if(instance != null) {
                System.out.println(new Gson().toJson(instance));
                DescribeInstanceTypesResponse.InstanceType instanceType = getInstanceType(instance.getInstanceType(),aliyunClient);
                F2CInstance f2cInstance = AliyunUtils.toF2CInstance(instance,instanceType);
                return f2cInstance;
            }else {
                throw new PluginException(e.getMessage(), e);
            }
        }
    }

    public void validateLaunchInstanceConfiguration(String launchInstanceConfiguration) throws PluginException {
        LaunchInstanceRequest req = new Gson().fromJson(launchInstanceConfiguration,LaunchInstanceRequest.class);
        IAcsClient aliyunClient = req.getAliyunClient();
        Integer diskSize = req.getDiskSize();
        String dataDiskCategory = req.getDiskType();
        if(diskSize!=null&&diskSize>0) {
            if("cloud".equals(dataDiskCategory)){
                if(diskSize < 5||diskSize>2000) {
                    throw new PluginException("普通云盘大小为5~2000GB!");
                }
            }else if("cloud_ssd".equals(dataDiskCategory)){
                if(diskSize < 20||diskSize>1024) {
                    throw new PluginException("SSD云盘大小为20~1024GB!");
                }
            }
        }

        try {
            String instanceTypeId = req.getInstanceType();
            DescribeInstanceTypesResponse.InstanceType instanceType = getInstanceType(instanceTypeId, aliyunClient);
            if(instanceType == null) {
                throw new PluginException("验证instanceType失败!");
            }
        } catch (Exception e) {
            throw new PluginException("验证instanceType失败!");
        }

        String password = req.getPassword();
        if(password!=null&&password.trim().length()>0&&password.trim().length()<30){
            if(!password.matches("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,30})")){
                throw new PluginException("无效的密码!");
            }
        }else{
            throw new PluginException("无效的密码!");
        }

        try {
            String regionId = req.getRegionId();
            if(regionId !=null&&regionId.trim().length()>0){
                DescribeRegionsRequest describeRegionsRequest = new DescribeRegionsRequest();
                DescribeRegionsResponse describeRegionsResponse = aliyunClient.getAcsResponse(describeRegionsRequest);
                boolean invalidRegion = true;
                for(DescribeRegionsResponse.Region region:describeRegionsResponse.getRegions()){
                    if(region.getRegionId().equals(regionId)){
                        invalidRegion = false;
                        break;
                    }
                }
                if(invalidRegion){
                    throw new PluginException("无效的region!");
                }
            }else {
                throw new PluginException("无效的region!");
            }
        } catch (ClientException e) {
            e.printStackTrace();
            throw new PluginException("无效的region!");
        }

        try {
            String zoneid = req.getZoneId();
            if(zoneid !=null&&zoneid.trim().length()>0){
                DescribeZonesRequest describeZonesRequest = new DescribeZonesRequest();
                describeZonesRequest.setRegionId(req.getRegionId());
                DescribeZonesResponse describeZonesResponse = aliyunClient.getAcsResponse(describeZonesRequest);
                boolean invalidZone = true;
                for(DescribeZonesResponse.Zone zone:describeZonesResponse.getZones()){
                    if(zone.getZoneId().equals(zoneid)){
                        invalidZone = false;
                        break;
                    }
                }
                if(invalidZone){
                    throw new PluginException("无效的zone!");
                }
            }
        } catch (Exception e) {
            throw new PluginException("无效的zone!");
        }

        try {
            String imageId = req.getImageId();
            DescribeImagesResponse.Image image = getImage(imageId,req.getRegionId(), aliyunClient);
            if(image == null) {
                throw new PluginException("验证imageId失败!");
            }
        } catch (Exception e) {
            throw new PluginException("验证imageId失败!");
        }

        String internetChargeType = req.getInternetChargeType();
        if(internetChargeType !=null&&internetChargeType.trim().length()>0){
            if(!AliyunUtils.getgetInternetChargeTypes().contains(internetChargeType)){
                throw new PluginException("无效的网络类型!");
            }
        }
        int internetMaxBandwidthOut = req.getInternetMaxBandwidthOut();
        if(AliyunUtils.PAY_BY_BAND_WIDTH.equals(internetChargeType)){
            if(internetMaxBandwidthOut<0||internetMaxBandwidthOut>100){
                throw new PluginException("无效的公网流出带宽值!");
            }
        }
        if(AliyunUtils.PAY_BY_TRAFFIC.equals(internetChargeType)){
            if(internetMaxBandwidthOut<1||internetMaxBandwidthOut>100){
                throw new PluginException("无效的公网流出带宽值!");
            }
        }
        String vpcId = req.getVpcId();
        if(vpcId!=null&&vpcId.trim().length()>0){
            if(req.getvSwitchId()!=null&&req.getvSwitchId().trim().length()>0){

            }else {
                throw new PluginException("无效的虚拟交换机!");
            }
        }
        try {
            String securityGroupId = req.getSecurityGroupId();
            if(securityGroupId !=null&&securityGroupId.trim().length()>0){
                DescribeSecurityGroupsRequest describeSecurityGroupsRequest = new DescribeSecurityGroupsRequest();
                describeSecurityGroupsRequest.setRegionId(req.getRegionId());
                if(vpcId!=null&&vpcId.trim().length()>0){
                    describeSecurityGroupsRequest.setVpcId(vpcId);
                }
                DescribeSecurityGroupsResponse describeSecurityGroupsResponse = aliyunClient.getAcsResponse(describeSecurityGroupsRequest);
                boolean invalidSecurityGroup = true;
                for(DescribeSecurityGroupsResponse.SecurityGroup securityGroup:describeSecurityGroupsResponse.getSecurityGroups()){
                    if(securityGroup.getSecurityGroupId().equals(securityGroupId)){
                        invalidSecurityGroup = false;
                        break;
                    }
                }
                if(invalidSecurityGroup){
                    throw new PluginException("无效的安全组!");
                }
            }else {
                throw new PluginException("无效的安全组!");
            }
        } catch (Exception e) {
            throw new PluginException("无效的安全组!");
        }
        try {
            String loadBalancerId = req.getLoadBalancerId();
            if(loadBalancerId !=null&&loadBalancerId.trim().length()>0){
                DescribeLoadBalancerAttributeRequest describeLoadBalancersRequest = new DescribeLoadBalancerAttributeRequest();
                describeLoadBalancersRequest.setRegionId(req.getRegionId());
                describeLoadBalancersRequest.setLoadBalancerId(loadBalancerId);
                DescribeLoadBalancerAttributeResponse dr = aliyunClient.getAcsResponse(describeLoadBalancersRequest);

                boolean invalidLoadBalancers = true;
                if(req.getvSwitchId()!=null&&req.getvSwitchId().trim().length()>0){
                    if("intranet".equals(dr.getAddressType())){
                        invalidLoadBalancers = false;
                    }
                }else{
                    if("internet".equals(dr.getAddressType())){
                        invalidLoadBalancers = false;
                    }
                }

                if(invalidLoadBalancers){
                    throw new PluginException("无效的负载均衡器!");
                }
            }
        } catch (Exception e) {
            throw new PluginException("无效的负载均衡器!");
        }
    }

    public boolean terminateInstance(TerminateInstanceRequest terminateInstanceRequest) throws PluginException {
        AliyunRequest req = AliyunUtils.transRequest2AliyunRequest(terminateInstanceRequest);
        IAcsClient aliyunClient = req.getAliyunClient();
        try {
            com.aliyuncs.ecs.model.v20140526.StopInstanceRequest stopInstanceRequest =
                    new com.aliyuncs.ecs.model.v20140526.StopInstanceRequest();

            String instanceId = terminateInstanceRequest.getInstanceId();
            if(instanceId!=null){
            }else{
                throw new PluginException("无效的instanceId");
            }
            boolean instanceStopped = false;
            DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
            describeInstancesRequest.setRegionId(req.getRegionId());
            Collection<String> instancesList = new ArrayList<String>();
            instancesList.add(instanceId);
            describeInstancesRequest.setInstanceIds(new Gson().toJson(instancesList));
            DescribeInstancesResponse instancesResult = aliyunClient.getAcsResponse(describeInstancesRequest);
            List<DescribeInstancesResponse.Instance> instances = instancesResult.getInstances();
            DescribeInstancesResponse.Instance instance =null;
            if(instances!=null&&instances.size()>0){
                instance = instances.get(0);
                if(instance.getStatus()== DescribeInstancesResponse.Instance.Status.STOPPED){
                    instanceStopped = true;
                }else if (instance.getStatus()== DescribeInstancesResponse.Instance.Status.RUNNING){
                    stopInstanceRequest.setInstanceId(instanceId);
                    System.out.println("start to stop the instance!!!!!!");
                    aliyunClient.getAcsResponse(stopInstanceRequest);

                    int count = 0;
                    while(true){
                        System.out.println("check for instance["+instanceId+"] status for "+ ++count +" times!");
                        DescribeInstancesResponse stopResult = aliyunClient.getAcsResponse(describeInstancesRequest);
                        List<DescribeInstancesResponse.Instance> stopInstances = stopResult.getInstances();
                        if(stopInstances!=null&&stopInstances.size()>0) {
                            instance = stopInstances.get(0);
                            String aliyunStatus = instance.getStatus().getStringValue();
                            System.out.println("instance["+instanceId+"] current status :: "+aliyunStatus);
                            if (aliyunStatus.equals("Stopped")){
                                System.out.println("success to stop instance!");
                                instanceStopped = true;
                                break;
                            }
                        }
                        if(count < 20){
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                            }
                        }else{
                            throw new PluginException("Stop instance timeout!!!");
                        }
                    }
                }
            }

            if(instanceStopped){
                if(instance.getEipAddress()!=null){
                    String allocationId = instance.getEipAddress().getAllocationId();
                    if(allocationId!=null&&allocationId.trim().length()>0){
                        System.out.println("start unbind eip:" + allocationId);
                        UnassociateEipAddressRequest unassociateEipAddressRequest = new UnassociateEipAddressRequest();
                        unassociateEipAddressRequest.setAllocationId(allocationId);
                        unassociateEipAddressRequest.setInstanceId(instanceId);
                        aliyunClient.getAcsResponse(unassociateEipAddressRequest);
                        DescribeEipAddressesRequest describeEipAddressesRequest = new DescribeEipAddressesRequest();
                        describeEipAddressesRequest.setRegionId(req.getRegionId());
                        describeEipAddressesRequest.setAllocationId(allocationId);
                        int eipcount = 0;
                        boolean unBindEip = false;
                        while(true){
                            System.out.println("check for eip["+allocationId+"] status for "+ ++eipcount +" times!");
                            DescribeEipAddressesResponse eipResult = aliyunClient.getAcsResponse(describeEipAddressesRequest);
                            List<DescribeEipAddressesResponse.EipAddress> eips= eipResult.getEipAddresses();
                            if(eips!=null&&eips.size()>0) {
                                DescribeEipAddressesResponse.EipAddress eip = eips.get(0);
                                String eipStatus = eip.getStatus();
                                System.out.println("instance["+allocationId+"] current status :: "+eipStatus);
                                if (eipStatus.equals("Available")){
                                    System.out.println("success to unbind eip!");
                                    unBindEip = true;
                                    break;
                                }
                            }
                            if(eipcount < 10){
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                }
                            }else{
                                throw new PluginException("Unbind eip timeout!");
                            }
                        }
                        if(unBindEip){
                            System.out.println("release unbind eip:" + allocationId);
                            ReleaseEipAddressRequest releaseEipAddressRequest = new ReleaseEipAddressRequest();
                            releaseEipAddressRequest.setAllocationId(allocationId);
                            aliyunClient.getAcsResponse(releaseEipAddressRequest);
                        }
                    }
                }
                DeleteInstanceRequest deleteInstanceRequest = new DeleteInstanceRequest();
                deleteInstanceRequest.setInstanceId(instanceId);
                System.out.println("start to delete the instance!!!!!!");
                aliyunClient.getAcsResponse(deleteInstanceRequest);
                int count = 0;
                while(true){
                    System.out.println("check for instance["+instanceId+"] status for "+ ++count +" times!");
                    DescribeInstancesResponse result = aliyunClient.getAcsResponse(describeInstancesRequest);
                    List<DescribeInstancesResponse.Instance> deleteInstances = result.getInstances();
                    if(deleteInstances.size()==0) {
                        System.out.println("success to delete instance!");
                        return true;
                    }
                    if(count < 10){
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                        }
                    }else{
                        break;
                    }
                }
            }
            return false;
        } catch (ClientException e) {
            e.printStackTrace();
            throw new PluginException(e.getMessage(),e);
        }
    }

    public F2CInstance startInstance(StartInstanceRequest startInstanceRequest) throws PluginException {
        AliyunRequest req = AliyunUtils.transRequest2AliyunRequest(startInstanceRequest);
        IAcsClient aliyunClient = req.getAliyunClient();

        com.aliyuncs.ecs.model.v20140526.StartInstanceRequest aliyunStartInstanceRequest =
                new com.aliyuncs.ecs.model.v20140526.StartInstanceRequest();
        try {
            String instanceId = startInstanceRequest.getInstanceId();
            if(instanceId!=null){
                aliyunStartInstanceRequest.setInstanceId(instanceId);
                aliyunClient.getAcsResponse(aliyunStartInstanceRequest);
            }else{
                throw new PluginException("无效的instanceId");
            }
            DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
            describeInstancesRequest.setRegionId(req.getRegionId());
            Collection<String> instancesList = new ArrayList<String>();
            instancesList.add(instanceId);
            describeInstancesRequest.setInstanceIds(new Gson().toJson(instancesList));
            DescribeInstancesResponse.Instance instance =null;
            int count = 0;
            while(true){
                System.out.println("check for instance["+instanceId+"] status for "+ ++count +" times!");
                DescribeInstancesResponse result = aliyunClient.getAcsResponse(describeInstancesRequest);
                List<DescribeInstancesResponse.Instance> instances = result.getInstances();
                if(instances != null && instances.size() > 0) {
                    instance = instances.get(0);
                    String aliyunStatus = instance.getStatus().getStringValue();
                    System.out.println("instance["+instanceId+"] current status :: "+aliyunStatus);
                    if (aliyunStatus.equals("Running")){
                        System.out.println("success to start instance!");
                        break;
                    }
                }
                if(count < 40){
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }
                }else{
                    break;
                }
            }
            if(instance != null) {
                DescribeInstanceTypesResponse.InstanceType instanceType = getInstanceType(instance.getInstanceType(), aliyunClient);
                F2CInstance f2cInstance = AliyunUtils.toF2CInstance(instance,instanceType);
                return f2cInstance;
            }else {
                System.out.println("Failed to start instance!!");
                return null;
            }
        } catch (ClientException e) {
            e.printStackTrace();
            throw new PluginException(e.getMessage(),e);
        }
    }

    public boolean stopInstance(StopInstanceRequest stopInstanceRequest) throws PluginException {
        AliyunRequest req = AliyunUtils.transRequest2AliyunRequest(stopInstanceRequest);
        IAcsClient aliyunClient = req.getAliyunClient();

        com.aliyuncs.ecs.model.v20140526.StopInstanceRequest aliyunStopInstanceRequest =
                new com.aliyuncs.ecs.model.v20140526.StopInstanceRequest();
        try {
            String instanceId = stopInstanceRequest.getInstanceId();
            if(instanceId!=null){
                aliyunStopInstanceRequest.setInstanceId(instanceId);
                aliyunClient.getAcsResponse(aliyunStopInstanceRequest);
            }else{
                throw new PluginException("无效的instanceId");
            }
            DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
            describeInstancesRequest.setRegionId(req.getRegionId());
            Collection<String> instancesList = new ArrayList<String>();
            instancesList.add(instanceId);
            describeInstancesRequest.setInstanceIds(new Gson().toJson(instancesList));
            DescribeInstancesResponse.Instance instance =null;
            int count = 0;
            while(true){
                System.out.println("check for instance["+instanceId+"] status for "+ ++count +" times!");
                DescribeInstancesResponse result = aliyunClient.getAcsResponse(describeInstancesRequest);
                List<DescribeInstancesResponse.Instance> instances = result.getInstances();
                if(instances != null && instances.size() > 0) {
                    instance = instances.get(0);
                    String aliyunStatus = instance.getStatus().getStringValue();
                    System.out.println("instance["+instanceId+"] current status :: "+aliyunStatus);
                    if (aliyunStatus.equals("Stopped")){
                        System.out.println("success to stop instance!");
                        return true;
                    }
                }
                if(count < 40){
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }
                }else{
                    break;
                }
            }
            return false;
        } catch (ClientException e) {
            e.printStackTrace();
            throw new PluginException(e.getMessage(),e);
        }
    }

    private DescribeInstanceTypesResponse.InstanceType getInstanceType(String intanceId,IAcsClient client)
                                throws PluginException{
        DescribeInstanceTypesRequest describeInstanceTypesRequest = new DescribeInstanceTypesRequest();
        DescribeInstanceTypesResponse.InstanceType instanceType = null;
        try {
            DescribeInstanceTypesResponse describeInstanceTypesResponse = client.getAcsResponse(describeInstanceTypesRequest);
            List<DescribeInstanceTypesResponse.InstanceType> types = describeInstanceTypesResponse.getInstanceTypes();
            for(DescribeInstanceTypesResponse.InstanceType it:types){
                if(it.getInstanceTypeId().equals(intanceId)){
                    instanceType = it;
                }
            }
            return instanceType;
        } catch (ClientException e) {
            e.printStackTrace();
            throw new PluginException(e.getMessage(), e);
        }
    }

    private DescribeImagesResponse.Image getImage(String imageId,String regionId,IAcsClient client)
            throws PluginException{
        DescribeImagesRequest describeImagesRequest = new DescribeImagesRequest();
        describeImagesRequest.setImageId(imageId);
        describeImagesRequest.setRegionId(regionId);
        DescribeImagesResponse.Image image = null;
        try {
            DescribeImagesResponse describeImagesResponse = client.getAcsResponse(describeImagesRequest);
            List<DescribeImagesResponse.Image> images = describeImagesResponse.getImages();
            if(images.size()>0){
                image = images.get(0);
            }
            return image;
        } catch (ClientException e) {
            e.printStackTrace();
            throw new PluginException(e.getMessage(), e);
        }
    }

    public List<F2CInstance> getF2CInstances(String getF2CInstancesRequest) throws PluginException {
        AliyunRequest req = new Gson().fromJson(getF2CInstancesRequest, AliyunRequest.class);
        IAcsClient aliyunClient = req.getAliyunClient();

        List<F2CInstance> results = new ArrayList<F2CInstance>();
        try {
            DescribeRegionsRequest describeRegionsRequest = new DescribeRegionsRequest();
            DescribeRegionsResponse describeRegionsResponse = aliyunClient.getAcsResponse(describeRegionsRequest);
            List<DescribeRegionsResponse.Region> regions = describeRegionsResponse.getRegions();
            if(regions.size()>0){
                for(DescribeRegionsResponse.Region region:regions){
                    DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
                    describeInstancesRequest.setRegionId(region.getRegionId());

                    DescribeInstanceTypesRequest describeInstanceTypesRequest = new DescribeInstanceTypesRequest();
                    describeInstanceTypesRequest.setRegionId(region.getRegionId());

                    DescribeInstanceTypesResponse describeInstanceTypesResponse = aliyunClient.getAcsResponse(describeInstanceTypesRequest);
                    List<DescribeInstanceTypesResponse.InstanceType> instanceTypes = describeInstanceTypesResponse.getInstanceTypes();



                    DescribeInstancesResponse describeInstancesResponse = aliyunClient.getAcsResponse(describeInstancesRequest);
                    List<DescribeInstancesResponse.Instance> instances = describeInstancesResponse.getInstances();
                    String imageIds = "";
                    for(int i =0;i<instances.size();i++){
                        if(i!=0){
                            imageIds = imageIds + "," + instances.get(i).getImageId();
                        }else{
                            imageIds += instances.get(i).getImageId();
                        }

                    }
                    DescribeImagesRequest describeImagesRequest = new DescribeImagesRequest();
                    describeImagesRequest.setRegionId(region.getRegionId());
                    describeImagesRequest.setImageId(imageIds);
                    DescribeImagesResponse describeImagesResponse = aliyunClient.getAcsResponse(describeImagesRequest);
                    List<DescribeImagesResponse.Image> images = describeImagesResponse.getImages();
                    describeImagesRequest.setImageOwnerAlias("marketplace");
                    DescribeImagesResponse describeImagesResponse1 = aliyunClient.getAcsResponse(describeImagesRequest);
                    List<DescribeImagesResponse.Image> images1 = describeImagesResponse1.getImages();
                    images.addAll(images1);
                    for(DescribeInstancesResponse.Instance i:instances){
                        DescribeInstanceTypesResponse.InstanceType instanceType = null;

                        for(DescribeInstanceTypesResponse.InstanceType t:instanceTypes){
                            if(t.getInstanceTypeId().equals(i.getInstanceType())){
                                instanceType = t;
                            }
                        }
                        String osName = "";

                        for(DescribeImagesResponse.Image image :images){
                            if(image.getImageId().equals(i.getImageId())){
                                osName = image.getOSName();
                            }
                        }
                        F2CInstance f2CInstance = AliyunUtils.toF2CInstance(i,instanceType);
                        f2CInstance.setOs(osName);
                        results.add(f2CInstance);
                    }
                }
            }
            return results;
        } catch (ClientException e) {
            e.printStackTrace();
            throw new PluginException(e.getMessage(),e);
        }
    }

    public F2CInstance getF2CInstance(String getF2CInstanceRequest) throws PluginException {
        GetInstanceRequest req = new Gson().fromJson(getF2CInstanceRequest,GetInstanceRequest.class);
        String instanceId = req.getInstanceId();
        IAcsClient aliyunClient = req.getAliyunClient();
        DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
        describeInstancesRequest.setRegionId(req.getRegionId());
        Collection<String> instancesList = new ArrayList<String>();
        if(instanceId !=null){
            instancesList.add(instanceId);
        }
        describeInstancesRequest.setInstanceIds(new Gson().toJson(instancesList));
        F2CInstance f2CInstance = null;
        try {
            DescribeInstancesResponse.Instance instance =null;
            DescribeInstancesResponse result = aliyunClient.getAcsResponse(describeInstancesRequest);
            List<DescribeInstancesResponse.Instance> instances = result.getInstances();

            if(instances != null && instances.size() > 0) {
                instance = instances.get(0);
                DescribeInstanceTypesResponse.InstanceType instanceType = getInstanceType(instance.getInstanceId(),aliyunClient);;

                f2CInstance = AliyunUtils.toF2CInstance(instance, instanceType);
                DescribeImagesRequest describeImagesRequest = new DescribeImagesRequest();
                describeImagesRequest.setRegionId(instance.getRegionId());
                describeImagesRequest.setImageId(instance.getImageId());
                DescribeImagesResponse describeImagesResponse = aliyunClient.getAcsResponse(describeImagesRequest);
                List<DescribeImagesResponse.Image> images = describeImagesResponse.getImages();
                describeImagesRequest.setImageOwnerAlias("marketplace");
                DescribeImagesResponse describeImagesResponse1 = aliyunClient.getAcsResponse(describeImagesRequest);
                List<DescribeImagesResponse.Image> images1 = describeImagesResponse1.getImages();
                images.addAll(images1);
                for(DescribeImagesResponse.Image image :images){
                    if(image.getImageId().equals(instance.getImageId())){
                        f2CInstance.setOs(image.getOSName());
                    }
                }
            }
            return f2CInstance;
        } catch (ClientException e) {
            e.printStackTrace();
            throw new PluginException(e.getMessage(),e);
        }
    }

    public List<DescribeZonesResponse.Zone> getZones(String getZonesRequest) throws PluginException{
        AliyunRequest req = new Gson().fromJson(getZonesRequest, AliyunRequest.class);
        IAcsClient aliyunClient = req.getAliyunClient();
        DescribeZonesRequest describeZonesRequest = new DescribeZonesRequest();
        describeZonesRequest.setRegionId(req.getRegionId());
        try {
            DescribeZonesResponse describeRegionsResponse = aliyunClient.getAcsResponse(describeZonesRequest);
            List<DescribeZonesResponse.Zone> zones = describeRegionsResponse.getZones();
            return zones;
        } catch (ClientException e) {
            e.printStackTrace();
            throw new PluginException(e.getMessage(),e);
        }
    }
    public List<DescribeRegionsResponse.Region> getRegions(String getRegionRequest) throws PluginException{
        AliyunRequest req = new Gson().fromJson(getRegionRequest,AliyunRequest.class);
        IAcsClient aliyunClient = req.getAliyunClient();
        DescribeRegionsRequest describeRegionsRequest = new DescribeRegionsRequest();
        try {
            DescribeRegionsResponse describeRegionsResponse = aliyunClient.getAcsResponse(describeRegionsRequest);
            List<DescribeRegionsResponse.Region> regions = describeRegionsResponse.getRegions();
            return regions;
        } catch (ClientException e) {
            e.printStackTrace();
            throw new PluginException(e.getMessage(),e);
        }
    }

    public List<DescribeImagesResponse.Image> getImages(String getImagesRequest) throws PluginException{
        AliyunRequest req = new Gson().fromJson(getImagesRequest, AliyunRequest.class);
        IAcsClient aliyunClient = req.getAliyunClient();
        DescribeImagesRequest describeImagesRequest = new DescribeImagesRequest();
        describeImagesRequest.setRegionId(req.getRegionId());
        int pageSize = 10;
        int pageNum = 1;
        int totalCount = 0;
        describeImagesRequest.setPageSize(pageSize);

        try {
            List<DescribeImagesResponse.Image> images = new ArrayList<DescribeImagesResponse.Image>();
            do{
                describeImagesRequest.setPageNumber(pageNum);
                DescribeImagesResponse describeRegionsResponse = aliyunClient.getAcsResponse(describeImagesRequest);
                totalCount = describeRegionsResponse.getTotalCount();
                for(DescribeImagesResponse.Image image:describeRegionsResponse.getImages()){
                    if(image.getOSType().equals("linux")&&image.getArchitecture().equals(DescribeImagesResponse.Image.Architecture.X86_64)){
                        if(image.getPlatform().toLowerCase().equals("suse")||
                                image.getPlatform().toLowerCase().equals("aliyun")||
                                image.getPlatform().toLowerCase().equals("ubuntu")||
                                image.getPlatform().toLowerCase().equals("debian")||
                                image.getPlatform().toLowerCase().equals("centos6")||
                                image.getPlatform().toLowerCase().equals("centos5")||
                                image.getPlatform().toLowerCase().equals("centos")||
                                image.getPlatform().toLowerCase().equals("redhat5")){
                            StringBuffer imageName = new StringBuffer();
                            if(image.getImageOwnerAlias().equals("system")){
                                imageName.append("[系统]");
                            }else if(image.getImageOwnerAlias().equals("self")){
                                imageName.append("[自定义]");
                            }else if(image.getImageOwnerAlias().equals("others")){
                                imageName.append("[共享]");
                            }
                            imageName.append(image.getImageName());
                            image.setImageName(imageName.toString());
                            images.add(image);
                        }
                    }
                }
                pageNum++;
            }while (totalCount > pageNum * pageSize);
            return images;
        } catch (ClientException e) {
            e.printStackTrace();
            throw new PluginException(e.getMessage(),e);
        }
    }

    public List<DescribeVpcsResponse.Vpc> getVpcs(String getVpcsRequest) throws PluginException{
        AliyunRequest req = new Gson().fromJson(getVpcsRequest, AliyunRequest.class);
        IAcsClient aliyunClient = req.getAliyunClient();
        DescribeVpcsRequest describeVpcsRequest = new DescribeVpcsRequest();
        describeVpcsRequest.setRegionId(req.getRegionId());
        try {
            DescribeVpcsResponse describeVpcsResponse = aliyunClient.getAcsResponse(describeVpcsRequest);
            List<DescribeVpcsResponse.Vpc> vpcs = describeVpcsResponse.getVpcs();
            List<DescribeVpcsResponse.Vpc> results = new ArrayList<DescribeVpcsResponse.Vpc>();
            for(DescribeVpcsResponse.Vpc vpc:vpcs){
                String vpcname = vpc.getVpcName()+"(VPC)";
                vpc.setVpcName(vpcname);
                results.add(vpc);
            }
            DescribeVpcsResponse.Vpc nullVpc = new DescribeVpcsResponse.Vpc();
            nullVpc.setVpcId("");
            nullVpc.setVpcName("经典网络");
            results.add(0,nullVpc);
            return results;
        } catch (ClientException e) {
            e.printStackTrace();
            throw new PluginException(e.getMessage(),e);
        }
    }

    public List<DescribeVSwitchesResponse.VSwitch> getVSwitches(String getVSwitchesRequest) throws PluginException{
        GetVSwitchesRequest req = new Gson().fromJson(getVSwitchesRequest, GetVSwitchesRequest.class);
        IAcsClient aliyunClient = req.getAliyunClient();
        DescribeVSwitchesRequest describeVSwitchesRequest = new DescribeVSwitchesRequest();

        try {List<DescribeVSwitchesResponse.VSwitch> vSwitches = new ArrayList<DescribeVSwitchesResponse.VSwitch>();
            if(req.getVpcId()!=null&&req.getVpcId().trim().length()>0){
                describeVSwitchesRequest.setVpcId(req.getVpcId());
                DescribeVSwitchesResponse describeVSwitchesResponse = aliyunClient.getAcsResponse(describeVSwitchesRequest);
                vSwitches = describeVSwitchesResponse.getVSwitches();
            }
            return vSwitches;
        } catch (ClientException e) {
            e.printStackTrace();
            throw new PluginException(e.getMessage(),e);
        }
    }

    public List<DescribeSecurityGroupsResponse.SecurityGroup> getSecurityGroups(String getSecurityGroupsRequest) throws PluginException{
        GetSecurityGroupsRequest req = new Gson().fromJson(getSecurityGroupsRequest, GetSecurityGroupsRequest.class);
        IAcsClient aliyunClient = req.getAliyunClient();
        DescribeSecurityGroupsRequest describeSecurityGroupsRequest = new DescribeSecurityGroupsRequest();
        describeSecurityGroupsRequest.setRegionId(req.getRegionId());
        String vpcId = req.getVpcId();
        if(vpcId!=null&&vpcId.trim().length()>0){
            describeSecurityGroupsRequest.setVpcId(vpcId);
        }
        try {
            DescribeSecurityGroupsResponse describeSecurityGroupsResponse = aliyunClient.getAcsResponse(describeSecurityGroupsRequest);
            List<DescribeSecurityGroupsResponse.SecurityGroup> securityGroups = describeSecurityGroupsResponse.getSecurityGroups();
            List<DescribeSecurityGroupsResponse.SecurityGroup> resultSecurityGroups = new ArrayList<DescribeSecurityGroupsResponse.SecurityGroup>();
            for(DescribeSecurityGroupsResponse.SecurityGroup sg:securityGroups){
                if (sg.getDescription().contains("System created security group") || sg.getDescription().startsWith("G")){
                    String name = sg.getSecurityGroupName();
                    name = name + "(系统默认)";
                    sg.setSecurityGroupName(name);
                }
            }

            if(vpcId!=null&&vpcId.trim().length()>0){
                resultSecurityGroups = securityGroups;
            }else{
                for(DescribeSecurityGroupsResponse.SecurityGroup sg:securityGroups){
                    if(sg.getVpcId().trim().length()==0){
                        resultSecurityGroups.add(sg);
                    }
                }
            }
            return resultSecurityGroups;
        } catch (ClientException e) {
            e.printStackTrace();
            throw new PluginException(e.getMessage(),e);
        }
    }

    public List<HashMap<String, String>> getInternetChargeTypes(String getInternetChargeTypesRequest) {

        List<HashMap<String, String>> instanceTypeList = new ArrayList<HashMap<String, String>>();
        for(String s : AliyunUtils.getgetInternetChargeTypes()) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("value", s);
            if(s.equals("PayByBandwidth")){
                map.put("key", "按固定带宽付费");
            }else {
                map.put("key", "按流量付费");
            }
            instanceTypeList.add(map);
        }
        return instanceTypeList;
    }
    public List<Map<String, Object>> getDiskTypes(String getDiskTypesRequest) {
        List<Map<String, Object>> getDiskTypeList = new ArrayList<Map<String,Object>>();
        Map<String, Object> cloudMap = new HashMap<String, Object>();
        cloudMap.put("key", "普通云盘");
        cloudMap.put("value", "cloud");
        getDiskTypeList.add(cloudMap);
        Map<String, Object> ssdMap = new HashMap<String, Object>();
        ssdMap.put("key", "SSD云盘");
        ssdMap.put("value", "cloud_ssd");
        getDiskTypeList.add(ssdMap);
        return getDiskTypeList;
    }

    public List<Map<String, String>> getDiskFileSystems(String getDiskFileSystemsRequest) {
        List<Map<String, String>> diskFileSystemList = new ArrayList<Map<String,String>>();
        Map<String, String> ext4Map = new HashMap<String, String>();
        ext4Map.put("key", "ext4");
        ext4Map.put("value", "ext4");
        diskFileSystemList.add(ext4Map);
        Map<String, String> ext3Map = new HashMap<String, String>();
        ext3Map.put("key", "ext3");
        ext3Map.put("value", "ext3");
        diskFileSystemList.add(ext3Map);
        return diskFileSystemList;
    }

    public List<Map<String, String>> getInstanceTypes(String getInstanceTypesRequest) throws PluginException{
        AliyunRequest req = new Gson().fromJson(getInstanceTypesRequest,AliyunRequest.class);
        IAcsClient aliyunClient = req.getAliyunClient();
        DescribeInstanceTypesRequest describeInstanceTypesRequest = new DescribeInstanceTypesRequest();
        describeInstanceTypesRequest.setRegionId(req.getRegionId());
        List<Map<String, String>> resultList = new ArrayList<Map<String,String>>();
        try {
            DescribeInstanceTypesResponse instanceTypesResponse = aliyunClient.getAcsResponse(describeInstanceTypesRequest);
            List<DescribeInstanceTypesResponse.InstanceType> instanceTypeList = instanceTypesResponse.getInstanceTypes();
            for(DescribeInstanceTypesResponse.InstanceType type:instanceTypeList){
                if(type.getMemorySize()== 0.5f){
                    continue;
                }
                String typeName = type.getInstanceTypeId() + "("+ type.getCpuCoreCount() + "核 " + type.getMemorySize().intValue() + "G)";
                HashMap map = new HashMap();
                map.put("instanceTypeId",type.getInstanceTypeId());
                map.put("instanceTypeName",typeName);
                resultList.add(map);
            }
            return resultList;
        } catch (ClientException e) {
            e.printStackTrace();
            throw new PluginException(e.getMessage(),e);
        }

    }
    //todo refactor when aliyun supply new sdk
    public List<LoadBalancerResponse> getLoadBalancers(String getLoadBalancersRequest) throws  PluginException{
        GetLoadBalancersRequest req = new Gson().fromJson(getLoadBalancersRequest, GetLoadBalancersRequest.class);
        IAcsClient aliyunClient = req.getAliyunClient();
        DescribeLoadBalancersRequest describeLoadBalancersRequest = new DescribeLoadBalancersRequest();
        describeLoadBalancersRequest.setRegionId(req.getRegionId());
        List<LoadBalancerResponse> result = new ArrayList<LoadBalancerResponse>();
        LoadBalancerResponse nulllb = new LoadBalancerResponse();
        nulllb.setLoadBalancerId("");
        nulllb.setLoadBalancerName("请选择");
        result.add(nulllb);
        try {
            DescribeLoadBalancersResponse describeLoadBalancersResponse = aliyunClient.getAcsResponse(describeLoadBalancersRequest);
            List<DescribeLoadBalancersResponse.LoadBalancer> loadBalancers = describeLoadBalancersResponse.getLoadBalancers();
            if(loadBalancers.size()>0){
                for(DescribeLoadBalancersResponse.LoadBalancer lb :loadBalancers){
                    DescribeLoadBalancerAttributeRequest describeLoadBalancerAttribute = new DescribeLoadBalancerAttributeRequest();
                    describeLoadBalancerAttribute.setLoadBalancerId(lb.getLoadBalancerId());
                    DescribeLoadBalancerAttributeResponse dlbar = aliyunClient.getAcsResponse(describeLoadBalancerAttribute);

                    LoadBalancerResponse lbr = new LoadBalancerResponse();
                    lbr.setLoadBalancerId(dlbar.getLoadBalancerId());
                    if(dlbar.getLoadBalancerName()!=null&&dlbar.getLoadBalancerName().trim().length()>0){
                        lbr.setLoadBalancerName(dlbar.getLoadBalancerName());
                    }else{
                        lbr.setLoadBalancerName(dlbar.getLoadBalancerId());
                    }
                    if(req.getVpcId()!=null&&req.getVpcId().trim().length()>0){
                        if(dlbar.getAddressType().equals("intranet")){
                            result.add(lbr);
                        }
                    }else{
                        if(dlbar.getAddressType().equals("internet")){
                            result.add(lbr);
                        }
                    }
                }
            }
            return result;
        } catch (ClientException e) {
            e.printStackTrace();
            throw new PluginException(e.getMessage(),e);
        }
    }

	public List<F2CLoadBalancer> getF2CLoadBalancers(
			String getLoadBalancersRequest) throws PluginException {
		// TODO Auto-generated method stub
		return null;
	}
}
