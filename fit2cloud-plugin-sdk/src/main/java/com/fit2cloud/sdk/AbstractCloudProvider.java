package com.fit2cloud.sdk;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.jar.JarFile;

/**
 * Created by zhangbohan on 15/7/29.
 */
public abstract class AbstractCloudProvider implements ICloudProvider  {
	public String getPageTemplate(){
    	return readConfigFile("launchConfigure.json");
    }
	
	public String getCredentialPageTemplate(){
		return readConfigFile("credential.json");
	}

	@SuppressWarnings("resource")
	private String readConfigFile(String fileName) {
		try {
        	URL url = this.getClass().getProtectionDomain().getCodeSource().getLocation();
        	JarFile jarFile = new JarFile(url.getPath());
        	InputStream is = jarFile.getInputStream(jarFile.getEntry(fileName));
        	BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        	StringBuffer sb = new StringBuffer();
        	String line = null;
        	while((line = br.readLine()) != null) {
        		sb.append(line.trim());
        	}
        	return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    	return null;
	}
}
