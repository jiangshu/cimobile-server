package com.baidu.cimobi.javabean;

import java.util.HashMap;

public class CommandExecute implements java.io.Serializable{
    private String action;
    private HashMap<String,String> argument;
    
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public HashMap<String,String> getArgument() {
		return argument;
	}
	public void setArgument(HashMap<String,String> argument) {
		this.argument = argument;
	}    	 
}
