package com.fit2cloud.sdk.model;

public class UserData {
	
	public static final String SSH = "ssh";
	public static final String MANUAL = "manual";
	public static final String USERDATA = "userdata";
	
	private long account_id;
	private long cloud_id;
	private long cluster_id;
	private long serverrole_id;
	private long cluster_serverrole_id;
	private long server_id;
	private String platform = "";
	private String region = "";
	private String consumer_key;
	private String secret_key;
	private String rabbitmq_host = "";
	private int rabbitmq_port = 5672;
	private boolean rabbitmq_ssl = false;
	private String rabbitmq_down_exchange = "";
	private String rabbitmq_down_queue = "fit2cloud";
	private String rabbitmq_up_exchange = "fit2cloud";
	private String rabbitmq_vhost = "";
	private String restapi_endpoint = "";
	private boolean hasDisk = false;
	private String diskType = "";
	private String diskRaidType = "";
	private String diskFileSystem = "";
	private String diskMountPoint = "";
	private String agentInstallMethod;
	
	private int hostname_type = 0;
	private String hostname = "";
	private String cluster_name = "";
	private String cluster_role_name = "";
	private String server_name = "";
	
	private String cluster_type = "default";
	
	private String enterprise_internal_ip = "";
	private String enterprise_external_ip = "";
	
	private String master_repo = "";
	private String backup_repo = "";
	
	public long getAccount_id() {
		return account_id;
	}

	public void setAccount_id(long account_id) {
		this.account_id = account_id;
	}

	public long getCloud_id() {
		return cloud_id;
	}

	public void setCloud_id(long cloud_id) {
		this.cloud_id = cloud_id;
	}

	public long getCluster_id() {
		return cluster_id;
	}

	public void setCluster_id(long cluster_id) {
		this.cluster_id = cluster_id;
	}

	public long getServerrole_id() {
		return serverrole_id;
	}

	public void setServerrole_id(long serverrole_id) {
		this.serverrole_id = serverrole_id;
	}

	public long getCluster_serverrole_id() {
		return cluster_serverrole_id;
	}

	public void setCluster_serverrole_id(long cluster_serverrole_id) {
		this.cluster_serverrole_id = cluster_serverrole_id;
	}

	public long getServer_id() {
		return server_id;
	}

	public void setServer_id(long server_id) {
		this.server_id = server_id;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	
	public String getConsumer_key() {
		return consumer_key;
	}

	public void setConsumer_key(String consumer_key) {
		this.consumer_key = consumer_key;
	}

	public String getSecret_key() {
		return secret_key;
	}

	public void setSecret_key(String secret_key) {
		this.secret_key = secret_key;
	}

	public String getRabbitmq_host() {
		return rabbitmq_host;
	}

	public void setRabbitmq_host(String rabbitmq_host) {
		this.rabbitmq_host = rabbitmq_host;
	}

	public int getRabbitmq_port() {
		return rabbitmq_port;
	}

	public void setRabbitmq_port(int rabbitmq_port) {
		this.rabbitmq_port = rabbitmq_port;
	}

	public String getRabbitmq_down_queue() {
		return rabbitmq_down_queue;
	}

	public void setRabbitmq_down_queue(String rabbitmq_down_queue) {
		this.rabbitmq_down_queue = rabbitmq_down_queue;
	}

	public String getRabbitmq_down_exchange() {
		return rabbitmq_down_exchange;
	}

	public void setRabbitmq_down_exchange(String rabbitmq_down_exchange) {
		this.rabbitmq_down_exchange = rabbitmq_down_exchange;
	}

	public String getRabbitmq_up_exchange() {
		return rabbitmq_up_exchange;
	}

	public void setRabbitmq_up_exchange(String rabbitmq_up_exchange) {
		this.rabbitmq_up_exchange = rabbitmq_up_exchange;
	}

	public String getRestapi_endpoint() {
		return restapi_endpoint;
	}

	public void setRestapi_endpoint(String restapi_endpoint) {
		this.restapi_endpoint = restapi_endpoint;
	}
	
	public boolean isRabbitmq_ssl() {
		return rabbitmq_ssl;
	}

	public void setRabbitmq_ssl(boolean rabbitmq_ssl) {
		this.rabbitmq_ssl = rabbitmq_ssl;
	}

	public String getRabbitmq_vhost() {
		return rabbitmq_vhost;
	}

	public void setRabbitmq_vhost(String rabbitmq_vhost) {
		this.rabbitmq_vhost = rabbitmq_vhost;
	}
	
	public String getDiskType() {
		return diskType;
	}

	public void setDiskType(String diskType) {
		this.diskType = diskType;
	}

	public String getDiskRaidType() {
		return diskRaidType;
	}

	public void setDiskRaidType(String diskRaidType) {
		this.diskRaidType = diskRaidType;
	}

	public String getDiskFileSystem() {
		return diskFileSystem;
	}

	public void setDiskFileSystem(String diskFileSystem) {
		this.diskFileSystem = diskFileSystem;
	}

	public String getDiskMountPoint() {
		return diskMountPoint;
	}

	public void setDiskMountPoint(String diskMountPoint) {
		this.diskMountPoint = diskMountPoint;
	}

	public boolean isHasDisk() {
		return hasDisk;
	}

	public void setHasDisk(boolean hasDisk) {
		this.hasDisk = hasDisk;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getCluster_name() {
		return cluster_name;
	}

	public void setCluster_name(String cluster_name) {
		this.cluster_name = cluster_name;
	}

	public String getCluster_role_name() {
		return cluster_role_name;
	}

	public void setCluster_role_name(String cluster_role_name) {
		this.cluster_role_name = cluster_role_name;
	}

	public String getServer_name() {
		return server_name;
	}

	public void setServer_name(String server_name) {
		this.server_name = server_name;
	}

	public int getHostname_type() {
		return hostname_type;
	}

	public void setHostname_type(int hostname_type) {
		this.hostname_type = hostname_type;
	}

	public String getCluster_type() {
		return cluster_type;
	}

	public void setCluster_type(String cluster_type) {
		this.cluster_type = cluster_type;
	}

	public String getEnterprise_internal_ip() {
		return enterprise_internal_ip;
	}

	public void setEnterprise_internal_ip(String enterprise_internal_ip) {
		this.enterprise_internal_ip = enterprise_internal_ip;
	}

	public String getEnterprise_external_ip() {
		return enterprise_external_ip;
	}

	public void setEnterprise_external_ip(String enterprise_external_ip) {
		this.enterprise_external_ip = enterprise_external_ip;
	}

	public String getMaster_repo() {
		return master_repo;
	}

	public void setMaster_repo(String master_repo) {
		this.master_repo = master_repo;
	}

	public String getBackup_repo() {
		return backup_repo;
	}

	public void setBackup_repo(String backup_repo) {
		this.backup_repo = backup_repo;
	}

	public String getAgentInstallMethod() {
		return agentInstallMethod;
	}

	public void setAgentInstallMethod(String agentInstallMethod) {
		this.agentInstallMethod = agentInstallMethod;
	}
}
