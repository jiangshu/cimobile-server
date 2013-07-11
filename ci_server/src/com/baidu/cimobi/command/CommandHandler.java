package com.baidu.cimobi.command;
import com.baidu.cimobi.mobileinfo.MobileModel;
import com.baidu.cimobi.javabean.CommandDelivery;
import com.baidu.cimobi.javabean.CommandExecute;
import com.baidu.cimobi.mobileinfo.MobileInstanceModel;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.net.ConnectException;

/*
 * 1.对命令进行处理
 * 2.找到合适的mobile
 * */
public class CommandHandler {
   private MobileModel mobiles;
   private CommandDelivery commandDelivery;
   String filterReg;
   
   public CommandHandler(MobileModel mobiles,CommandDelivery commandDelivery){
	   this.mobiles = mobiles;
	   this.commandDelivery = commandDelivery;
	   this.filterReg = getFilterReg();
   }
   
   /*
    * 刷选以及发送命令
    * */
   public String doHandler() throws Exception{
	   MobileInstanceModel mobileInstanceModel = null;
	   ArrayList<MobileInstanceModel> mobileList = new ArrayList<MobileInstanceModel>();
	   ObjectOutputStream oOut = null;
	   mobileInstanceModel = getMobileInstance();
	   if(null != mobileInstanceModel){
		   oOut = mobileInstanceModel.getoOut();
		   if(null == oOut){
			   return "Execution failed";
		   }
		   try{
			   oOut.writeObject(getCommand(mobileInstanceModel)); //输出命令
			   return "execution success!";
		   }catch(Exception e){
			   throw new Exception(e);
		   }
	   }else{
		   mobileList = mobiles.getMobilesByReg(filterReg);
		   if(mobileList.size()>0){
			   for(int i=0;i<mobileList.size();i++){
				   mobileInstanceModel = mobileList.get(i);
				   oOut = mobileInstanceModel.getoOut();
				   if(null != oOut){
					   try{
						   oOut.writeObject(getCommand(mobileInstanceModel)); //输出命令
					   }catch(Exception e){
						   throw new Exception(e);
					   }
				   }
			   }
			   return "execution success!";
		   }else{
			   return "can't find mobiel that meet the filter conditions!";
		   }
	   }
   }
   
  
   
   /*
    * 构造发送的命令
    * */
   private CommandExecute getCommand(MobileInstanceModel mobileInstanceModel){
	   CommandExecute commandExecute = new CommandExecute(); 
	   String url= commandDelivery.getUrl();
	   ArrayList<String> browserList = commandDelivery.getBrowsers();
	   String sequence = commandDelivery.getSequence();
	   String browsers = "";
	   for(int i=0;i<browserList.size();i++){
		   if(i==browserList.size()-1){
			   if(mobileInstanceModel.getBrowserMap().containsKey(browserList.get(i))){
				   browsers+=mobileInstanceModel.getBrowserMap().get(browserList.get(i));
			   }
		   }else{
			   if(mobileInstanceModel.getBrowserMap().containsKey(browserList.get(i))){
				   browsers+=mobileInstanceModel.getBrowserMap().get(browserList.get(i))+"&";
			   }
		   }
	   }
	   commandExecute.setAction("openBrowser");
	   HashMap<String,String>argument = new HashMap<String,String>();
	   if("".equals(browsers)){
		   browsers = "com.android.browser";
	   }
	   argument.put("browsers", browsers);
	   if("".equals(url) || null == url){
		   url = "http://www.baidu.com";
	   }
	   argument.put("url", url);
	   argument.put("sequence",sequence);
	   commandExecute.setArgument(argument);
	   return commandExecute;
   }
   
   
   /*
    * 筛选满足条件的mobile
    * */
   private MobileInstanceModel getMobileInstance(){
	   String id = commandDelivery.getId();       
	   boolean isAll = commandDelivery.getIsAll();          
	   String alias = commandDelivery.getAlias();
	   String ip = commandDelivery.getIp();
	   
	   if(!("".equals(id)) && null != id){
		   return  mobiles.getMobileInstanceById(id);
	   }else if(!("".equals(alias)) && null != alias){
		   return  mobiles.getMobileInstanceByAlias(alias);
	   }else if(!("".equals(ip)) && null != ip){
		   return  mobiles.getMobileInstanceById(ip);
	   }else{
		   if(!isAll){
			   return  mobiles.getMobileInstanceByReg(filterReg);
		   }
	   }	      
	   return null;
   }
   
   
   /*
    * 将筛选条件拼成正则
    * */
   private String getFilterReg(){
	   String reg = "";
	   String androidVersion = commandDelivery.getAndroidVersion(); 
	   String mobileType = commandDelivery.getMobileType(); 
	   ArrayList<String> browsers = commandDelivery.getBrowsers(); 
	   
	   if(null != mobileType){
		   reg+= mobileType + "[^,]*,";
	   }
	   
	   if(null != androidVersion){
		   reg+= androidVersion + ","; 
	   }else{
		   reg+="[^,]*,";
	   }
	   
	   if(null!=browsers && browsers.size()>0){
		   reg+="[^,]*(";
		   for(int i=0; i<browsers.size(); i++){
			   reg+=browsers.get(i)+"[^,]*";
			   if(i!=browsers.size()-1){
				   reg+="|";
			   }
		   }
		   reg+="){"+browsers.size()+"}";
	   }else{
		   reg+="";
	   }	   
	   return reg;	   
   }    
}
