package com.baidu.cimobi.mobileinfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.ObjectOutputStream;

public class MobileInstanceModel {
	private String ip;                          //mobile ip
	private String id;                          //mobile分配的唯一id
	private String alias;                       //别名，筛选需求
	private String infoReg;                     //信息的组合，包括（moible型号、系统版本、浏览器列表） 
	private String mobileType;                  //手机的品牌及型号
	private String androidVersion;              //系统的版本	
	private ArrayList<String> packages;         //系统中安装的包名
	private ArrayList<String> browsers;         //浏览器列表,浏览器别人/简称
	private HashMap<String,String>browserMap;   //系统中中浏览器列表<浏览器别名=>浏览器包名>
	private ObjectOutputStream oOut;            //sokect输出流
	
	
	public HashMap<String, String> getBrowserMap() {
		return browserMap;
	}
	public void setBrowserMap(HashMap<String, String> browserMap) {
		this.browserMap = browserMap;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getInfoReg() {
		return infoReg;
	}
	public void setInfoReg(String infoReg) {
		this.infoReg = infoReg;
	}
	public String getIp() {
		return ip;
	}
	public ObjectOutputStream getoOut() {
		return oOut;
	}
	public void setoOut(ObjectOutputStream oOut) {
		this.oOut = oOut;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
}
