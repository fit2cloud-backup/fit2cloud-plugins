package com.fit2cloud.aliyunplugin.model.request;


import com.aliyuncs.ecs.model.v20140526.CreateInstanceRequest;
import com.fit2cloud.sdk.model.UserData;
import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;

/**
 * Created by zhangbohan on 15/8/10.
 */
public class LaunchInstanceRequest extends AliyunRequest{
    private String zoneId;
    private String imageId;
    private String instanceType;
    private String securityGroupId;
    private boolean hasPublicIp = true;
    private String internetChargeType;
    private Integer internetMaxBandwidthOut;
    private String ioOptimized;
    private String password;
    private String vpcId;
    private String vSwitchId;
    private boolean hasEip = false;
    private String loadBalancerId;
    private Integer diskSize;
    private String diskType;

    private boolean keepDiskWhenTerminate = true;
    private String diskFileSystem;
    private String diskMount;
    private String userdata = null;

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }

    public String getSecurityGroupId() {
        return securityGroupId;
    }

    public void setSecurityGroupId(String securityGroupId) {
        this.securityGroupId = securityGroupId;
    }

    public boolean isHasPublicIp() {
        return hasPublicIp;
    }

    public void setHasPublicIp(boolean hasPublicIp) {
        this.hasPublicIp = hasPublicIp;
    }

    public String getInternetChargeType() {
        return internetChargeType;
    }

    public void setInternetChargeType(String internetChargeType) {
        this.internetChargeType = internetChargeType;
    }

    public Integer getInternetMaxBandwidthOut() {
        return internetMaxBandwidthOut;
    }

    public void setInternetMaxBandwidthOut(Integer internetMaxBandwidthOut) {
        this.internetMaxBandwidthOut = internetMaxBandwidthOut;
    }

    public Integer getDiskSize() {
        return diskSize;
    }

    public void setDiskSize(Integer diskSize) {
        this.diskSize = diskSize;
    }

    public String getDiskType() {
        return diskType;
    }

    public void setDiskType(String diskType) {
        this.diskType = diskType;
    }

    public String getDiskFileSystem() {
        return diskFileSystem;
    }

    public void setDiskFileSystem(String diskFileSystem) {
        this.diskFileSystem = diskFileSystem;
    }

    public String getDiskMount() {
        return diskMount;
    }

    public void setDiskMount(String diskMount) {
        this.diskMount = diskMount;
    }

    public String getIoOptimized() {
        return ioOptimized;
    }

    public void setIoOptimized(String ioOptimized) {
        this.ioOptimized = ioOptimized;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVpcId() {
        return vpcId;
    }

    public void setVpcId(String vpcId) {
        this.vpcId = vpcId;
    }

    public String getvSwitchId() {
        return vSwitchId;
    }

    public void setvSwitchId(String vSwitchId) {
        this.vSwitchId = vSwitchId;
    }

    public boolean isHasEip() {
        return hasEip;
    }

    public void setHasEip(boolean hasEip) {
        this.hasEip = hasEip;
    }

    public String getLoadBalancerId() {
        return loadBalancerId;
    }

    public void setLoadBalancerId(String loadBalancerId) {
        this.loadBalancerId = loadBalancerId;
    }

    public String getUserdata() {
		return userdata;
	}

	public void setUserdata(String userdata) {
		this.userdata = userdata;
	}

    public boolean isKeepDiskWhenTerminate() {
        return keepDiskWhenTerminate;
    }

    public void setKeepDiskWhenTerminate(boolean keepDiskWhenTerminate) {
        this.keepDiskWhenTerminate = keepDiskWhenTerminate;
    }

    public CreateInstanceRequest toCreateInstanceRequest(){
        CreateInstanceRequest createInstanceRequest = new CreateInstanceRequest();
        createInstanceRequest.setRegionId(getRegionId());
        if(userdata != null && userdata.trim().length() > 0) {
			try {
				String udStr = new String(Base64.decodeBase64(userdata.getBytes("UTF-8")), "UTF-8");
				UserData ud = new Gson().fromJson(udStr, UserData.class);
				String clusterName = ud.getCluster_name();
				String clusterRoleName = ud.getCluster_role_name();
				long serverId = ud.getServer_id();
				String serverName = clusterName + "--" + clusterRoleName + "--" + serverId;
				createInstanceRequest.setInstanceName(serverName);
			} catch (Exception e) {
				System.out.println("处理虚机名称时出错!"+e.getMessage());
			}
		}

        createInstanceRequest.setInstanceType(instanceType);
        createInstanceRequest.setImageId(imageId);
        createInstanceRequest.setPassword(password);
        createInstanceRequest.setSecurityGroupId(securityGroupId);
        if(zoneId!=null&&zoneId.trim().length()>0){
            createInstanceRequest.setZoneId(zoneId);
        }
        if (vpcId!=null&&vpcId.trim().length()>0&&vSwitchId!=null&&vSwitchId.trim().length()>0){

        }else{
            if(internetMaxBandwidthOut!=null&&internetMaxBandwidthOut>0){
                createInstanceRequest.setInternetMaxBandwidthOut(internetMaxBandwidthOut);
            }
        }

        if(diskSize!=null&&diskSize>0){
            createInstanceRequest.setDataDisk1Size(diskSize);
            createInstanceRequest.setDataDisk1DeleteWithInstance(!keepDiskWhenTerminate);
        }
        if(diskType!=null&&diskType.trim().length()>0){
            createInstanceRequest.setDataDisk1Category(diskType);
        }
        if(ioOptimized!=null&&ioOptimized.trim().length()>0){
            createInstanceRequest.setIoOptimized(ioOptimized);
        }
        if(vSwitchId!=null&&vSwitchId.trim().length()>0){
            createInstanceRequest.setVSwitchId(vSwitchId);
        }
        return createInstanceRequest;
    }
}
