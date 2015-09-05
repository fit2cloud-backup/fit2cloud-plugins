package com.fit2cloud.aliyunplugin.model.request;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.fit2cloud.sdk.model.Request;
import com.google.gson.Gson;
import com.fit2cloud.aliyunplugin.utils.AliyunCredential;

/**
 * Created by zhangbohan on 15/8/10.
 */
public class AliyunRequest extends Request{
    private AliyunCredential aliyunCredential;


    public AliyunCredential getAliyunCredential() {
        if(aliyunCredential==null){
            aliyunCredential = new Gson().fromJson(getCredential(),AliyunCredential.class);
        }
        return aliyunCredential;
    }

    public void setAliyunCredential(AliyunCredential aliyunCredential) {
        this.aliyunCredential = aliyunCredential;
    }

    public String getSecretKey() {
        aliyunCredential = getAliyunCredential();
        if(aliyunCredential != null) {
            return aliyunCredential.getSecretKey();
        }
        return null;
    }

    public String getAccessKey() {
        aliyunCredential = getAliyunCredential();
        if(aliyunCredential != null) {
            return aliyunCredential.getAccessKey();
        }
        return null;
    }

    public IAcsClient getAliyunClient(){
        if(getAccessKey() != null && getAccessKey().trim().length() > 0 && getSecretKey() != null && getSecretKey().trim().length() > 0) {
            String defaultRegionId = "cn-hangzhou";
            if(getRegionId() != null && getRegionId().trim().length() > 0) {
                defaultRegionId = getRegionId();
            }
            IClientProfile profile = DefaultProfile.getProfile(defaultRegionId, getAccessKey(), getSecretKey());
            IAcsClient client = new DefaultAcsClient(profile);
            return client;
        }
        return null;
    }
}
