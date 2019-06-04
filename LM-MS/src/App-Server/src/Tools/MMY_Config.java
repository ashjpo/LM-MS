package Tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class MMY_Config{
	Properties pro=null;
	public MMY_Config(String config_file_path) {
		File file = new File(config_file_path);
		if (file.exists()) {
			try {
				pro = new Properties();
				FileInputStream in = new FileInputStream(config_file_path);
				try {
					pro.load(in);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else {
			pro=null;
		}
		

	}
	
	/**
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String get_string(String key,String defaultValue) {
		if(pro==null) {
			return defaultValue;
		}
		try {
			String value=(String) pro.getProperty(key, defaultValue);
			return value;
		}catch(Exception exception){
			return defaultValue;
		}
	}
	
	/**
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public int get_int(String key,int defaultValue) {
		if(pro==null) {
			return defaultValue;
		}
		try {
			int value=Integer.getInteger(pro.getProperty(key, defaultValue+""));
			return value;
		}catch(Exception exception){
			return defaultValue;
		}
		
	}
	
	/**
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public float get_float(String key,float defaultValue) {
		if(pro==null) {
			return defaultValue;
		}
		try {
			float value=Float.parseFloat(pro.getProperty(key, defaultValue+""));
			return value;
		}catch(Exception exception){
			return defaultValue;
		}
	}
	
	/**
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public boolean get_boolean(String key,boolean defaultValue) {
		if(pro==null) {
			return defaultValue;
		}
		boolean value=defaultValue;
		try {
			if(defaultValue) {
				value=Boolean.getBoolean(pro.getProperty(key, "true"));
			}else {
				value=Boolean.getBoolean(pro.getProperty(key, "false"));
			}
		}catch(Exception exception){
			return defaultValue;
		}
		return value;
	}
	
	public static void main(String[] args) {
		MMY_Config mmy_config_obj=new MMY_Config("D:/a.properties");
		String value=mmy_config_obj.get_string("name","haha");
		System.out.println(value);
		System.out.println(mmy_config_obj.get_float("pass",5));
	}
}