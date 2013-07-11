package com.baidu.cimobi.mobileinfo;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * @auth:jiangshuguang
 * 已连接的mobile集合
 * */

public class MobileModel {
    private ArrayList<MobileInstanceModel> mobileList;
    public MobileModel(){
    	mobileList = new ArrayList<MobileInstanceModel>();
    }
    
    public void addInstance(MobileInstanceModel mobileInstanceModel){ 
    	String id = mobileInstanceModel.getId();
    	removeInstance(id);
    	mobileList.add(mobileInstanceModel);
    }
    
    public void removeInstance(String id){
    	for(int i=0; i<mobileList.size(); i++){
    		if(id == mobileList.get(i).getId()){
    			mobileList.remove(i);
    		}
    	}
    }
    
    /*
     * 通过id筛选mobile
     * */
    public MobileInstanceModel getMobileInstanceById(String id){
    	for(int i=0; i<mobileList.size(); i++){
    		if(id.equals(mobileList.get(i).getId())){
    			return mobileList.get(i);
    		}
    	}
    	return null;
    }
    
    /*
     * 通过alias筛选mobile
     * */
    public MobileInstanceModel getMobileInstanceByAlias(String alias){
    	for(int i=0; i<mobileList.size(); i++){
    		if(alias.equals(mobileList.get(i).getAlias())){
    			return mobileList.get(i);
    		}
    	}
    	return null;
    }
    
    /*
     * 通过ip筛选mobile
     * */
    public MobileInstanceModel getMobileInstanceByIp(String ip){
    	for(int i=0; i<mobileList.size(); i++){
    		if(ip.equals(mobileList.get(i).getIp())){
    			return mobileList.get(i);
    		}
    	}
    	return null;
    }
    
   /*
    * 通过正则筛选出一个满足条件的mobile
    * */
    public MobileInstanceModel getMobileInstanceByReg(String reg){
    	for(int i=0; i<mobileList.size(); i++){
    		if(""!=reg && match(mobileList.get(i).getInfoReg(),reg)){
    			return mobileList.get(i);
    		}
    	}
    	return null;
    }
    
    /*
     * 通过正则筛选出所有满足条件的mobile
     * */
    public ArrayList<MobileInstanceModel> getMobilesByReg(String reg){
    	ArrayList<MobileInstanceModel>Mobiles = new ArrayList<MobileInstanceModel>();
    	for(int i=0; i<mobileList.size(); i++){
    		if(""!=reg && match(mobileList.get(i).getInfoReg(),reg)){
    			Mobiles.add(mobileList.get(i));
    		}
    	}
    	if(Mobiles.size()>0){
    		return Mobiles;
    	}
    	return null;
    }
        
    public ArrayList<MobileInstanceModel> getMobileList(){
    	return mobileList;
    } 
    
	private  boolean match(String ora,String regex)
	{
		Pattern p=Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m=p.matcher(ora);
	
		if(m.find()){
			return true;
		}else{
			return false;
		}
	}
    
}
