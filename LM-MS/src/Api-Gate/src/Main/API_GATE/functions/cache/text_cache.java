package Main.API_GATE.functions.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Map.Entry;

import Context.Main_Context;
import Main.API_GATE.model.Ask;
import Main.API_GATE.model.Service;
import Main.API_GATE.work.match_ask;

/**
 * �����ݻ��浽�ı��ļ���
 * @author mmy
 *
 */
public class text_cache{
	//��ʱ
	String cache_root_path="D:/CacheTextTest";
	//�������ʱ�仺�潫��ɾ��
	long cache_limit_time=60;	//s
	public text_cache(Main_Context mc) {
		cache_root_path=mc.config_obj.text_cache_dir_path;
		cache_limit_time=mc.config_obj.text_cache_limit_time;
		clear_cache_root_path(cache_root_path);
		
	}
	
	/**
	 * ��ȡ�ļ�����
	 * @param service
	 * @return
	 */
	public String get_cache(match_ask mAsk) {
		Service service=this.getMapFirstService(mAsk.service_map);
		String service_name=service.service_name;
		String version=service.version;
		Ask ask=this.getMapFirstAsk(mAsk.ask_map);
		String ask_name=ask.ask_name;
		long current_time=(long)System.currentTimeMillis()/1000;
		//�鿴��û����ͬsvhashid�ļ�
		File file = new File(cache_root_path);
		String[] filelist = file.list();
        for (int i = 0; i < filelist.length; i++) {
        	String file_name=filelist[i].replaceAll(".cache", "");
        	String file_service_name=file_name.split("_")[0];
        	String file_service_version=file_name.split("_")[1];
        	String file_service_ask=file_name.split("_")[2];
        	int file_service_last_cache_time=0;
        	try {
        		file_service_last_cache_time=Integer.parseInt(file_name.split("_")[3]);
        	}catch (Exception e) {
        		file_service_last_cache_time=0;
			}
        	
        	if(!file_service_name.equals(service_name) || file_service_version.equals(version) || !file_service_ask.equals(ask_name)) {
        		continue;
        	}
        	if((current_time-file_service_last_cache_time)>cache_limit_time) {
        		//ɾ���ļ�
        		File last_file = new File(cache_root_path + "/" + filelist[i]);
        		last_file.delete();
        		return "";
        	}else {
        		String cache_str=readToString(cache_root_path + "/" + filelist[i]);
        		return cache_str;
        	}
        }
        return "";        
		
	}
	
	/**
	 * ���û��� �����Ƿ����óɹ� force=false���֮ǰ����û�е�ʱ�Ͳ����� force=true����
	 * @param service
	 * @param message
	 */
	public boolean set_cache(match_ask mAsk,String message,Boolean force) {
		Service service=this.getMapFirstService(mAsk.service_map);
		String service_name=service.service_name;
		String version=service.version;
		Ask ask=this.getMapFirstAsk(mAsk.ask_map);
		String ask_name=ask.ask_name;
		long current_time=(long)System.currentTimeMillis()/1000;
		//�鿴��û����ͬsvhashid�ļ�
		File file = new File(cache_root_path);
		String[] filelist = file.list();
		boolean if_hash=false;
        for (int i = 0; i < filelist.length; i++) {
        	String file_name=filelist[i].replaceAll(".cache", "");
        	String file_service_name=file_name.split("_")[0];
        	String file_service_version=file_name.split("_")[1];
        	String file_service_ask=file_name.split("_")[2];
        	int file_service_last_cache_time=0;
        	try {
        		file_service_last_cache_time=Integer.parseInt(file_name.split("_")[3]);
        	}catch (Exception e) {
        		file_service_last_cache_time=0;
			}
        	
        	if(!file_service_name.equals(service_name) || !file_service_version.equals(version) || !file_service_ask.equals(ask_name)) {
        		continue;
        	}
        	
        	if(force) {	//ȫ������
        		if_hash=true;
        		//ɾ���ļ�
        		File last_file = new File(cache_root_path + "/" + filelist[i]);
        		last_file.delete();
        		String new_file_name=service_name+"_"+version+"_"+ask_name+"_"+current_time+".cache";
        		File create_file =new File(cache_root_path + "/" + new_file_name);
        		Writer out;
				try {
					out = new FileWriter(create_file);
					out.write(message);
            		out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		return true;
        	}else {	//ʱ���ж�
        		if_hash=true;
        		if((current_time-file_service_last_cache_time)>cache_limit_time) {
            		//ɾ���ļ�
            		File last_file = new File(cache_root_path + "/" + filelist[i]);
            		last_file.delete();
            		String new_file_name=service_name+"_"+version+"_"+ask_name+"_"+current_time+".cache";
            		File create_file =new File(cache_root_path + "/" + new_file_name);
            		Writer out;
					try {
						out = new FileWriter(create_file);
						out.write(message);
	            		out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            		return true;
            	}else {
            		return false;
            	}
        	}
        }
        
        //֮ǰû�л�����ļ�
        if(if_hash==false) {
        	String new_file_name=service_name+"_"+version+"_"+ask_name+"_"+current_time+".cache";
			File create_file =new File(cache_root_path + "/" + new_file_name);
			Writer out;
			try {
				out = new FileWriter(create_file);
				out.write(message);
	    		out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
        }
        return true;
        
	}
	
	public String readToString(String fileName) {  
        String encoding = "UTF-8";  
        File file = new File(fileName);  
        Long filelength = file.length();  
        byte[] filecontent = new byte[filelength.intValue()];  
        try {  
            FileInputStream in = new FileInputStream(file);  
            in.read(filecontent);  
            return new String(filecontent, encoding);
        } catch (Exception e) {  
            e.printStackTrace();  
        }
        
        return "";
    }  
	
	/**
	 * ��ջ����Ŀ¼
	 */
	public boolean clear_cache_root_path(String cache_root_path) {
		if(!new File(cache_root_path).exists()) {
			File file = new File(cache_root_path.replace("/","\\\\"));
			file.mkdir();
			return true;
		}else {
			clearFolder(cache_root_path);
			return true;
		}
	}
	
	public static void clearFolder(String folderPath) {
	     try {
	        delAllFile(folderPath); //ɾ����������������
	        
	     } catch (Exception e) {
	       e.printStackTrace(); 
	     }
	}
	
	public static boolean delAllFile(String path) {
	       boolean flag = false;
	       File file = new File(path);
	       if (!file.exists()) {
	         return flag;
	       }
	       if (!file.isDirectory()) {
	         return flag;
	       }
	       String[] tempList = file.list();
	       File temp = null;
	       for (int i = 0; i < tempList.length; i++) {
	          if (path.endsWith(File.separator)) {
	             temp = new File(path + tempList[i]);
	          } else {
	              temp = new File(path + File.separator + tempList[i]);
	          }
	          if (temp.isFile()) {
	             temp.delete();
	          }
	          if (temp.isDirectory()) {
	             delAllFile(path + "/" + tempList[i]);//��ɾ���ļ���������ļ�
	             //delFolder(path + "/" + tempList[i]);//��ɾ�����ļ���
	             flag = true;
	          }
	       }
	       return flag;
	     
	}
	
	private Service getMapFirstService(Map<String, Service> ask_map) {    	
		Service obj = null;        
		for (Entry<String, Service> entry : ask_map.entrySet()) {            
			obj = entry.getValue();            
			if (obj != null) {                
				break;            
			}        
		}        
		return  obj;    
	}
	
	private Ask getMapFirstAsk(Map<String, Ask> ask_map) {    	
		Ask obj = null;        
		for (Entry<String, Ask> entry : ask_map.entrySet()) {            
			obj = entry.getValue();            
			if (obj != null) {                
				break;            
			}        
		}        
		return  obj;    
	}
}