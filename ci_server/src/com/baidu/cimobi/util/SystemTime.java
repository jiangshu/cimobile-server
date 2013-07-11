package com.baidu.cimobi.util;

import java.util.Date;
import java.text.SimpleDateFormat;

public class SystemTime {
   public static String get(){
   	Date now = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return sdf.format(now);
   }
}
