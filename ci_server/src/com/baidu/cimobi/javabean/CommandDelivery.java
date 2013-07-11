package com.baidu.cimobi.javabean;

import java.util.HashMap;
import java.util.ArrayList;
/*
 * 命令bean
 * */
public class CommandDelivery implements java.io.Serializable{
	
    private String action;  //命令的动作
    private String id;      //
    private String androidVersion; //
    private String mobileType;     //
    private boolean isAll;          // 
    private ArrayList<String> browsers; //
    private String url;
    private String alias;
    private String ip;
    private String group;
    private String sequence;
        
    public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	private HashMap<String,String> attach;   //
    
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getAndroidVersion() {
		return androidVersion;
	}
	public void setAndroidVersion(String androidVersion) {
		this.androidVersion = androidVersion;
	}
	
	public String getMobileType() {
		return mobileType;
	}
	public void setMobileType(String mobileType) {
		this.mobileType = mobileType;
	}
	
	public boolean getIsAll() {
		return isAll;
	}
	public void setIsAll(boolean isAll) {
		this.isAll = isAll;
	}
	
	public ArrayList<String> getBrowsers() {
		return browsers;
	}
	public void setBrowsers(ArrayList<String> browsers) {
		this.browsers = browsers;
	}
	
	public HashMap<String, String> getAttach() {
		return attach;
	}
	public void setAttach(HashMap<String, String> attach) {
		this.attach = attach;
	}
}
