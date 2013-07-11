package com.baidu.cimobi.javabean;

import java.util.ArrayList;

/*
 * mobile基本信息bean
 * */

public class MobileInfo implements java.io.Serializable{
    private String Uid;                 //手机的唯一标识  
	private String mobileType;          //手机的品牌及型号
	private String androidVersion;      //系统的版本
	private String alias;               //手机别名
	private ArrayList<String> browsers; //浏览器列表（包名）
	private ArrayList<String> packages; //mobile所有安装程序(包名)
	private String group;               //所属的分组
	
	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public void setAndroidVersion(String androidVersion) {
		this.androidVersion = androidVersion;
	}

	public ArrayList<String> getPackages() {
		return packages;
	}

	public void setPackages(ArrayList<String> packages) {
		this.packages = packages;
	}

	public ArrayList<String> getBrowsers() {
		return browsers;
	}

	public void setBrowsers(ArrayList<String> browsers) {
		this.browsers = browsers;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	public String getMobileType() {
		return mobileType;
	}
	
	public void setMobileType(String mobileType) {
		this.mobileType = mobileType;
	}
	
	public String getAndroidVersion() {
		return androidVersion;
	}
	
	public String getUid() {
		return Uid;
	}

	public void setUid(String uid) {
		Uid = uid;
	}

}
