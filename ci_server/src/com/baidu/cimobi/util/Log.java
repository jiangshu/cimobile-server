package com.baidu.cimobi.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
/*
 * auth:jiangshuguang
 * 日志信息处理类
 * */
public class Log {
	private static String logFile = "log";
	public static void insert(String log){
		try{
			PrintStream ps = new PrintStream(new FileOutputStream(logFile,true));
			ps.print(log);
			ps.close();
		}catch(Exception e){
			
		}	
	}
		
	public static ArrayList<HashMap<String,String>>getLog(){
    	ArrayList<HashMap<String,String>> logs = new ArrayList<HashMap<String,String>>();
    	try{
    		BufferedReader brd = new BufferedReader(
    				new InputStreamReader(new FileInputStream(logFile)));
    		String logItem = "";
    		String[] logTep;   		
    		while((logItem = brd.readLine())!=null){
    			HashMap<String,String>log = new HashMap<String,String>();
    			logTep  = logItem.split("   ");
    			String time = "";
    			String content = "";
    			if(logTep.length == 2){
    				time = logTep[0];
    				content = logTep[1];
    				log.put("time",time);
    				log.put("content", content);
    				logs.add(log);
    			}
    		}
    		brd.close();
    	}catch(Exception e){} 
    	return logs;
	}
	
	
}
