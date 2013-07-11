package com.baidu.cimobi.mobileinfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;

import com.baidu.cimobi.javabean.MobileInfo;
import com.baidu.cimobi.mobileinfo.MobileInstanceModel;
import java.net.Socket;
import java.io.ObjectOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * 
 * 浏览器信息
 *    别名 = [包名，入口activity]
 * 1. native=[com.android.browser, com.android.browser.BrowserActivity]
 * 2. uc=[com.UCMobile, com.UCMobile.main.UCMobile]
 * 3. QQ=[com.tencent.mtt, com.tencent.mtt.SplashActivity] 
 * 4. opera=[com.opera.mini.android, com.opera.mini.android.Browser]  
 * 5. chrome=[com.android.chrome, com.google.android.apps.chrome.Main] 
 * 6. firefox=[org.mozilla.firefox, org.mozilla.firefox.App]
 * 7. maxthon==[com.mx.browser, com.mx.browser.MxBrowserActivity]
 * 8. 360=[com.qihoo.browser, com.qihoo.browser.BrowserActivity]
 * 
 * 不在此统计范围的
 * 包名 = [包名，入口activity]
 * 
 * */

public class MobileInfoDao {
	private MobileInfo mobileInfo;
	private MobileInstanceModel mobileInstanceModel;
	private ObjectOutputStream oOut;
	private String ip;
	public MobileInfoDao(MobileInfo mobileInfo,ObjectOutputStream oOut,String ip){
	   this.mobileInfo = mobileInfo;
	   mobileInstanceModel = new MobileInstanceModel();
	   this.oOut = oOut;
	   this.ip = ip;
	   setMobileFullInfo();
	}
	
	public MobileInstanceModel getMobileInstance(){	                                  
	    return mobileInstanceModel;
    }
	

	private void setMobileFullInfo(){
		mobileInstanceModel.setIp(ip);
		mobileInstanceModel.setId(mobileInfo.getUid());
		mobileInstanceModel.setAlias(mobileInfo.getAlias());
		mobileInstanceModel.setMobileType(mobileInfo.getMobileType());
		mobileInstanceModel.setAndroidVersion(mobileInfo.getAndroidVersion());
		
		mobileInstanceModel.setInfoReg(getInfoReg());
		mobileInstanceModel.setoOut(oOut);
		mobileInstanceModel.setPackages(mobileInfo.getPackages());
		
		ArrayList<String>browsers = new ArrayList<String>();
		HashMap<String,String>browserMap = new HashMap<String,String>();
		String browser = "";
		for(int i=0;i<mobileInfo.getBrowsers().size();i++){
			browser = getBrowserAlias(mobileInfo.getBrowsers().get(i));
			browsers.add(browser);
			browserMap.put(browser,mobileInfo.getBrowsers().get(i));
		}					
		mobileInstanceModel.setBrowsers(browsers);
		mobileInstanceModel.setBrowserMap(browserMap);
	}
	
	private String getInfoReg(){
		String reg = "";
		String key;
		reg+=mobileInfo.getMobileType()+",";
		reg+=mobileInfo.getAndroidVersion()+",";
		for(int i=0;i<mobileInfo.getBrowsers().size();i++){
			reg+=getBrowserAlias(mobileInfo.getBrowsers().get(i)) + "&";
		}
		return reg;
	}

	private String getBrowserAlias(String packageName){
		String browserAlias = "";
        if(match("^.*opera.*$",packageName)){
        	browserAlias = "opera";
        }else if(match("^.*tencent.*$",packageName)){
        	browserAlias = "qq";
        }else if(match("^.*UC.*$",packageName)){
        	browserAlias = "uc";
        }else if(match("^.*chrome.*$",packageName)){
        	browserAlias = "chrome";
        }else if(match("^.*com\\.android\\.browser.*$",packageName)){
        	browserAlias = "native";
        }else if(match("^.*firefox.*$",packageName)){
        	browserAlias = "firefox";
        }else if(match("^.*mx.*$",packageName)){
        	browserAlias = "maxthon";
        }else if(match("^.*qihoo.*$",packageName)){
        	browserAlias = "360";
        }else{
        	browserAlias = packageName;
        } 
		return browserAlias;
	}
	
	
	private boolean match(String reg,String target){
		Pattern p = Pattern.compile(reg,Pattern.CASE_INSENSITIVE);
		Matcher m =  p.matcher(target);
		if(m.find()){
			return true;
		}
		return false;
	}
	
}
