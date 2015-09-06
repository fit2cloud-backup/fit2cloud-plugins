# FIT2CLOUD 插件示例工程

### 插件机制

FIT2CLOUD 提供基于Spring注入机制的插件体系，开发的云插件只需要实现 FIT2CLOUD Plugin SDK 中的接口方法，并且编写云插件虚机创建模版，FIT2CLOUD就能动态的调用云插件中方法，在页面中渲染生成虚机创建模版页面。

### 插件开发指南

1. 编写云插件的虚机创建模版（launchconfiguration.json)，具体可参考仓库中提供的官方阿里云插件。
2. 实现 SDK 相应接口 ｀fit2cloud-plugin-sdk/src/main/java/com/fit2cloud/sdk/ICloudProvider.java`bao

**接口列表**

```
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

```

**阿里云插件目录结构**

```
fit2cloud-aliyun-plugin
├── pom.xml
└── src
    └── main
        ├── java
        │   └── com
        │       └── fit2cloud
        │           └── aliyunplugin
        │               ├── AliyunCloudprovider.java  <-----云插件实现 SDK 接口，调用公/私有云API具体业务实现
        │               ├── model
        │               │   └── request
        │               │       ├── AliyunRequest.java
        │               │       ├── GetInstanceRequest.java
        │               │       ├── GetInstanceTypeRequest.java
        │               │       ├── GetLoadBalancersRequest.java
        │               │       ├── GetSecurityGroupsRequest.java
        │               │       ├── GetVSwitchesRequest.java
        │               │       ├── LaunchInstanceRequest.java
        │               │       └── LoadBalancerResponse.java
        │               └── utils
        │                   ├── AliyunCredential.java
        │                   └── AliyunUtils.java
        └── resources
            ├── credential.json
            ├── launchConfigure.json        <----- 虚机创建模版
            └── plugin.properties
```

