package com.baidu.cimobi.mobileinfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileOutputStream; 
import java.io.OutputStreamWriter; 
import java.io.Writer; 
import java.util.ArrayList;
 
 
import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory; 
import javax.xml.transform.OutputKeys; 
import javax.xml.transform.Result; 
import javax.xml.transform.Source; 
import javax.xml.transform.Transformer; 
import javax.xml.transform.TransformerConfigurationException; 
import javax.xml.transform.TransformerException; 
import javax.xml.transform.TransformerFactory; 
import javax.xml.transform.dom.DOMSource; 
import javax.xml.transform.stream.StreamResult; 
 
import org.w3c.dom.Document; 
import org.w3c.dom.Element; 
import org.w3c.dom.Text; 
import org.w3c.dom.NodeList;   

 
/*
 * 将mobile的信息保存在xml文件中
 * 页面通过心跳ajax读取xml的信息
 * */
public class MobileInfoXml {
	private static String fileName = "./mobile_info.xml";
	private static File file = new File(fileName);
	
	public static void delMobileInfo(){
		File file = new File(fileName);
		if(file.exists()){
			file.delete();
		}
	}
	
	
	public static void saveMobileInfo(){
		File file = new File(fileName);
		if(file.exists()){
			file.delete();
		}
		
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
        DocumentBuilder builder = null; 
        
        try { 
        	builder = factory.newDocumentBuilder(); 
        }catch(Exception ex) { 
            ex.printStackTrace(); 
        } 
        Document doc = builder.newDocument(); 
        Element root = doc.createElement("mobile_info"); 
        doc.appendChild(root);
        
        for(int i=0; i<3; i++){
            Element moible = doc.createElement("mobile"); 
            moible.setAttribute("id","id");
            moible.setAttribute("name","name"); 
            moible.setAttribute("ip","ip");
            root.appendChild(moible); 
            
            for(int j=0; j<2; j++){
                Element packageItem = doc.createElement("package"); 
                moible.appendChild(packageItem); 
                Text packageName = doc.createTextNode("package_name"); 
                packageItem.appendChild(packageName); 
            }

            for(int k=0; k<2; k++){
                Element borowserItem = doc.createElement("borowser"); 
                moible.appendChild(borowserItem); 
                Text browserName = doc.createTextNode("borowser_name"); 
                borowserItem.appendChild(browserName); 
            }
        }

        try { 
            FileOutputStream fos = new FileOutputStream(fileName); 
            OutputStreamWriter osw = new OutputStreamWriter(fos); 
            callDomWriter(doc, osw, "UTF-8"); 
            osw.close(); 
            fos.close(); 
        }catch(Exception ex) { 
            ex.printStackTrace(); 
        } 
	}
	
	public static void deleteItem(String id){
		Document doc = null;
		if(file.exists()){
			   try{  
				 	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();   
				 	DocumentBuilder builder = factory.newDocumentBuilder();   
				 	doc = builder.parse(file);
			    	   
			    }catch (Exception e) {   
				  e.printStackTrace();   
				} 
			    Element root=(Element)doc.getElementsByTagName("mobile_info").item(0);
			    NodeList mobiles = doc.getElementsByTagName("mobile");
			    for(int i=0; i<mobiles.getLength(); i++){
					   if(((Element)mobiles.item(i)).getAttribute("id").equals(id)){
						   root.removeChild(mobiles.item(i));
						   break;
					   }
			    }
		        try { 
		            FileOutputStream fos = new FileOutputStream(fileName); 
		            OutputStreamWriter osw = new OutputStreamWriter(fos); 
		            callDomWriter(doc, osw, "UTF-8"); 
		            osw.close(); 
		            fos.close(); 
		        }catch(Exception ex) { 
		            ex.printStackTrace(); 
		        } 
			    
		}
	}
	
	/*
	 * 插入一个手机信息
	 * */
	public static void insertItem(MobileInstanceModel mobileInstance){
		deleteItem(mobileInstance.getId());
		DocumentBuilderFactory factory = null;
		DocumentBuilder builder = null;
		Document doc = null;
		Element root = null;
		
		try{
		    factory = DocumentBuilderFactory.newInstance();   
		 	builder = factory.newDocumentBuilder(); 
		}catch(Exception e){
			e.printStackTrace();
		}

		if(file.exists()){		  
		   try{   
			    doc = builder.parse(file);		    	   
		    }catch (Exception e) {   
			    e.printStackTrace();   
			} 
		    root=(Element)doc.getElementsByTagName("mobile_info").item(0);

		}else{			
	        doc = builder.newDocument(); 
	        root = doc.createElement("mobile_info"); 
	        doc.appendChild(root);	        	        
		}
		
		/*
		 * 增加一个手机信息 
		 * */
	    NodeList mobiles = doc.getElementsByTagName("mobile");
//	    for(int i=0; i<mobiles.getLength(); i++){
//			   if(((Element)mobiles.item(i)).getAttribute("id").equals(mobileInstance.getId())){
//				   return;
//			   }
//	    }
		
        Element mobile = doc.createElement("mobile"); 
        mobile.setAttribute("id",""+mobileInstance.getId());
        mobile.setAttribute("name",mobileInstance.getMobileType());
        mobile.setAttribute("sysVersion",mobileInstance.getAndroidVersion()); 
        mobile.setAttribute("ip",mobileInstance.getIp());
        mobile.setAttribute("alias", mobileInstance.getAlias());
        mobile.setAttribute("infoReg", mobileInstance.getInfoReg());
        
        root.appendChild(mobile); 
        
    	ArrayList<String> packageLables = mobileInstance.getPackages();
    	ArrayList<String> browsers = mobileInstance.getBrowsers();
        for(int i=0;i<browsers.size();i++){
            Element packageItem = doc.createElement("browser"); 
            mobile.appendChild(packageItem); 
            Text packageName = doc.createTextNode(browsers.get(i)); 
            packageItem.appendChild(packageName); 
        }
        /*
         * 其它安装包的信息，暂时不处理
         * */
//        for(int i=0;i<packageLables.size();i++){
//            Element packageItem = doc.createElement("package"); 
//            moible.appendChild(packageItem); 
//            Text packageName = doc.createTextNode(packageLables.get(i)); 
//            packageItem.appendChild(packageName); 
//        }
              
		
       try { 
            FileOutputStream fos = new FileOutputStream(fileName); 
            OutputStreamWriter osw = new OutputStreamWriter(fos); 
            callDomWriter(doc, osw, "UTF-8"); 
            osw.close(); 
            fos.close(); 
        }catch(Exception ex) { 
            ex.printStackTrace(); 
        } 
	}
	
    public static void callDomWriter(Document dom , Writer writer, String encoding) { 
        try { 
             
            Source source = new DOMSource(dom); 
            Result res = new StreamResult(writer); 
            Transformer xformer = TransformerFactory.newInstance().newTransformer(); 
            xformer.setOutputProperty(OutputKeys.ENCODING, encoding); 
            xformer.transform(source, res); 
        }catch (TransformerConfigurationException e) { 
               e.printStackTrace(); 
          } catch (TransformerException e) { 
           e.printStackTrace(); 
          }    
    } 
}
