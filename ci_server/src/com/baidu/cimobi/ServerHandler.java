package com.baidu.cimobi;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;


import com.baidu.cimobi.command.CommandHandler;
import com.baidu.cimobi.javabean.CommandDelivery;
import com.baidu.cimobi.javabean.InfoType;
import com.baidu.cimobi.javabean.MobileFullInfo;
import com.baidu.cimobi.javabean.MobileInfo;
import com.baidu.cimobi.mobileinfo.MobileInfoDao;
import com.baidu.cimobi.mobileinfo.MobileInfoXml;
import com.baidu.cimobi.mobileinfo.MobileModel;
import com.baidu.cimobi.util.Log;
import com.baidu.cimobi.util.SystemTime;

public class ServerHandler{	
	private JLabel serverInfoLable;
	private JTextArea serverInfo;
	private JLabel sendInfoLable;
	private JTextField sendInfo;
	private JButton sendBtn;
	private JButton closeBtn;
	private int i;
	PrintWriter out_out;
	private Socket curSocket;
	private ArrayList<MobileFullInfo> mobilelist;
	private HashMap<Integer,Socket> socketList;
	private MobileModel mobiles;
	private boolean isGraphical; //是否显示图形化界面
	int port;
	
	public ServerHandler(boolean isGraphical,int port){
		this.i = 0;
		this.isGraphical = isGraphical;
		this.port = port;
		if(isGraphical){
			MainFrame mainFrame = new MainFrame();
			mainFrame.makeFrame();
		}
		mobiles = new MobileModel();
	}	
	
	
	/*
	 * 服务守护进程
	 * 开启端口，等待客户端的连接
	 * */
	public void start(){
		MobileInfoXml.delMobileInfo(); //重置mobile信息 
		try{
			ServerSocket server = new ServerSocket(port);
//			mobileTest();
			printLog("service start success");
			for(;;){
				Socket incoming = server.accept();
				curSocket = incoming;
				new SocketHandler(incoming).start();	
				i++;
			}
	    }catch(BindException e){
	    	printLog("The port has binded by other program");
	    }catch(SocketException e){
	    	printLog("mobile connect failed");
	    }catch(IOException e){
	    	printLog("mobile connect failed");
	    }catch(Exception e){
	    	printLog("mobile connect failed");
	    }
	}
		   
	
	/*
	 * socket监听线程
	 * */
	private class SocketHandler extends Thread{
		private Socket incoming;
		private int clientId;
		public SocketHandler(Socket incoming){
			this.incoming = incoming;
		}
		public void run(){			
			BufferedReader in = null;
			PrintWriter out = null;
			ObjectInputStream oIn = null;
			ObjectOutputStream oOut = null;
			Object obj = null;
			MobileInfo mobileInfo = null;
			InfoType infoType = null;
			CommandDelivery commandDelivery = null;
			String mobileIp = incoming.getInetAddress().toString();
			String ip;
			String id = ""; //为删除断开连接的mobile做准备
			String type="";
		    String mobileType = "";
			
			
			try{
				oIn = new ObjectInputStream(new BufferedInputStream(incoming.getInputStream()));
			    oOut = new ObjectOutputStream(incoming.getOutputStream());  
			}catch(Exception e){
				printLog("connect exception");
			}
			
			try{
				while(true){					
					if((obj = oIn.readObject())!=null){
						infoType = (InfoType)obj;
						type = infoType.getType();
						type.replaceAll("\n", "");
						if(type.startsWith("mobileInfo")){   // mobile建立连接处理逻辑
							try{
								if((obj = oIn.readObject())!=null){
									mobileInfo = (MobileInfo)obj;	
									ip = incoming.getInetAddress().toString();
									ip = ip.replace("/", "");
									MobileInfoDao mobileInfoDao = 
										new MobileInfoDao(mobileInfo,oOut,ip);
									id = mobileInfoDao.getMobileInstance().getId();
									mobiles.addInstance(mobileInfoDao.getMobileInstance());
									MobileInfoXml.insertItem(mobileInfoDao.getMobileInstance());
									mobileType = mobileInfoDao.getMobileInstance().getMobileType();
									printLog(mobileType+" Connected");
								}
							}catch(Exception e){
								closeConnect(oIn,oOut,this.incoming);
								return;
							}

						}else if(type.startsWith("command")){   
							try{
								if((obj = oIn.readObject())!=null){
									commandDelivery = (CommandDelivery)obj;
									printLog("execute "+commandDelivery.getAction()+" command");
									InfoType oStatus = new InfoType();
									if(mobiles.getMobileList().size()<1){
										oStatus.setType("can't find mobiel that meet the filter conditions!");
										oOut.writeObject(oStatus);
										printLog("can't find mobiel that meet the filter conditions!");
										return;
									}
									CommandHandler commandHandler = new CommandHandler(mobiles,commandDelivery);
									try{
										String status = commandHandler.doHandler();
										oStatus.setType(status);
										oOut.writeObject(oStatus);
										printLog(status);
										return;
									}catch(Exception e){
										closeConnect(oIn,oOut,this.incoming);
										printLog("Execution failed(may be disconnned)");
										return;
									}
								}
							}catch(Exception e){
								closeConnect(oIn,oOut,this.incoming);
								return;
							} 
						}
					}
				} 							
			}catch(Exception e){
				if(type.startsWith("mobileInfo")){
					mobiles.removeInstance(id);    
					MobileInfoXml.deleteItem(id);  
			    }
				
				closeConnect(oIn,oOut,this.incoming);
				
				if(type.startsWith("mobileInfo")){
					printLog(mobileType + " disconnect or connect exception");
				}
				return;
			}
		}
	}
	
	
	//关闭socket
	private void closeConnect(ObjectInputStream oIn,ObjectOutputStream oOut,Socket socket){
		try{
			if(null != oIn){
			oIn.close();
			}
			if(null != oOut){
				oOut.close();
			}
			if(null != socket){
				socket.close();
			}
		}catch(Exception ee){

		}
	}
	
	
	public void mobileTest(){
		MobileInfo mobileInfo = new MobileInfo();
		mobileInfo.setAlias("mobile1");
		mobileInfo.setAndroidVersion("4.0.3");
		mobileInfo.setMobileType("htc TT52");
		ArrayList<String> browserList = new ArrayList<String>();
		browserList.add("com.baidu.uc.test");
		browserList.add("com.baidu.chrome.test");
		browserList.add("com.baidu.tencent.test");
		browserList.add("com.baidu.opera.test");
		browserList.add("com.android.browser");
		mobileInfo.setBrowsers(browserList);
		mobileInfo.setUid("aa11bb22cc33dd44");
		ObjectOutputStream oOut = null;
		String ip = "127.0.0.1";
		MobileInfoDao mobileInfoDao = 
			new MobileInfoDao(mobileInfo,oOut,ip);
		mobiles.addInstance(mobileInfoDao.getMobileInstance());
		MobileInfoXml.insertItem(mobileInfoDao.getMobileInstance());
		
		mobileInfo.setAlias("mobile2");
		mobileInfo.setUid("bb11bb22cc33dd55");
		mobileInfo.setAndroidVersion("4.0.4");
		mobileInfo.setMobileType("htc rrr");
		browserList.remove("com.baidu.opera.test");
		browserList.remove("com.baidu.tencent.test");
		
		mobileInfoDao = 
			new MobileInfoDao(mobileInfo,oOut,ip);
		mobiles.addInstance(mobileInfoDao.getMobileInstance());
		MobileInfoXml.insertItem(mobileInfoDao.getMobileInstance());
	}
	
	
	/*
	 *日志处理
	 *1. 写入log.txt文件中
	 *2. 在面板中显示 
	 * */
	private void printLog(String log){
		String logItem = SystemTime.get()+"   "+log+"\n";
		Log.insert(logItem);
		if(isGraphical){
			serverInfo.append(logItem);
		}
	}


   /*
    * 主图形化界面
    * */
   private class MainFrame extends WindowAdapter implements ActionListener{	
		public void makeFrame(){
			JFrame frame = new JFrame("服务器端");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    	frame.setLayout(null);
	    	frame.setSize(500,600);
	    	frame.setResizable(false);//设置窗口大小固定
	    	frame.addWindowListener(this);
	    	
	    	serverInfoLable = new JLabel("日志信息");
	    	frame.add(serverInfoLable);
	    	serverInfoLable.setBounds(50, 20, 400, 30);
	    	
	    	serverInfo = new JTextArea();
	    	serverInfo.setEditable(false);
	    	frame.add(serverInfo);
	    	serverInfo.setBounds(50, 50, 400, 470);
	    	
//	    	closeBtn = new JButton("关闭连接");
//	    	frame.add(closeBtn);
//	    	closeBtn.setBounds(200, 450, 120, 30);
//	    	closeBtn.addActionListener(this);
//	    	
//	    	
//	    	sendInfoLable = new JLabel("命令");
//	    	frame.add(sendInfoLable);
//	    	sendInfoLable.setBounds(20, 490, 400, 30);
//	    	
//	    	sendInfo = new JTextField("Start browser");
//	    	frame.add(sendInfo);
//	    	sendInfo.setBounds(20, 520, 360, 30);
//	    	
//	    	sendBtn = new JButton("执行");
//	    	frame.add(sendBtn);
//	    	sendBtn.setBounds(400, 520, 80, 30);
//	    	sendBtn.addActionListener(this);
	    		    	
	    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	        Dimension frameSize = frame.getSize();
	        frame.setLocation( (screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		    frame.setVisible(true);
		}
		
		public void actionPerformed(ActionEvent e){
//
//			if(e.getSource() == closeBtn){
//				try{
//					curSocket.close();
//				}catch(Exception ee){
//					ee.printStackTrace();
//				}
//			}else if(e.getSource() == sendBtn){
//				CommandDelivery command = new CommandDelivery();
//				command.setAction("openBrowser");
//				try{
//					oout_oout.writeObject(command);					
////					String str = sendInfo.getText();
////					sendInfo.setText("");
////					out_out.println(str);
//					
//				}catch(Exception ee){
//					ee.printStackTrace();
//				}
//			}
		}
		
	    public void windowClosing(WindowEvent e){
	    	System.exit(0);
	    }
   }
   
   
}
