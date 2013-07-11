package com.baidu.cimobi;

import com.baidu.cimobi.util.Log;
import com.baidu.cimobi.util.SystemTime;
/*
 * 主service入口
 * */
public class MainServer{	
   public static void main(String args[]) {
	   /*
	    * 图形化界面服务端 
	    * */
	   
	    boolean isGraph = false;
	    int port = 3204;
		String argumentItem = "";
		String argumentArr[];
		
		for(int i=0;i<args.length;i++){
			argumentItem = args[i];
			argumentArr = argumentItem.split("=");
			if(argumentArr[0].equals("port")){
				port =  Integer.parseInt(argumentArr[1]);
			}else if(argumentArr[0].equals("graph")){
				isGraph = true;
			}
		}
	   
		ServerHandler serverHandler = new ServerHandler(isGraph,port);
		serverHandler.start(); 
    }
}














//class SocketHandler1 extends Thread{
//	   
//	private Socket incoming;
//	private int clientId;
//	public SocketHandler1(Socket incoming){
//		this.incoming = incoming;
//	}
//	public void run(){
//		
//		BufferedReader in;
//		PrintWriter out;
//		ObjectInputStream oIn;
//		ObjectOutputStream oOut;
//		Object obj = null;
//		MobileInfo mobileInstance;
//		InfoType infoType;			
//		String mobileIp = incoming.getInetAddress().toString();
//
//		try{
////			in = new BufferedReader
////					(new InputStreamReader(incoming.getInputStream()));
//			oIn = new ObjectInputStream(new BufferedInputStream(incoming.getInputStream()));
//			
////			out = new PrintWriter
////					(incoming.getOutputStream(),true);
//		    oOut = new ObjectOutputStream(incoming.getOutputStream());
//			
//		    oout_oout = oOut;	
////			out_out = out;
//			
////			Pattern p = Pattern.compile(".*off.*"); 
////			Matcher m = null;
//			while(true){					
//				if((obj = oIn.readObject())!=null){
//					infoType = (InfoType)obj;
//					String type = infoType.getType();
//					type.replaceAll("\n", "");
//					if(type.startsWith("mobileInfo")){
//						if((obj = oIn.readObject())!=null){
//							mobileInstance = (MobileInfo)obj;	
//							
//							serverInfo.append(mobileInstance.getMobileType()+"\n");
//						}
//					}
//				}
//				
////			   String str = in.readLine();
////               m = p.matcher(str);
////               if(m.find()){
////            	   System.out.println("连接中断");
////       			   ObjectTest objectTest = new ObjectTest("object_object",2);
////    		       oout_oout.writeObject(objectTest);     
////            	   break;
////               }
//				
//			}	
//			
//			/*
//			 * 关闭
//			 * 1.关闭输入输出流
//			 * 2.关闭套接字
//			 * */
////			try{
////				this.incoming.close();	
////			}catch(Exception e){
////				System.out.println(e);
////			}
//			
////			this.interrupt();				
//			
//		}catch(Exception e){
//			try{
//				if(null != this.incoming){
//					this.incoming.close();
//				}
//				System.out.println("连接已关闭");
//				serverInfo.append("连接已关闭\n");
//			}catch(Exception ee){
//				System.out.println("连接关闭异常");
//			}
//			
////			System.out.println(e);
//		}
//	}
//}