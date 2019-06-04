package Main.AS.model;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import hprose.server.HproseTcpServer;

public class init_java_service{
	String root_path="../../";
	String this_service_url;
	String service_port;
	String service_path;
	HproseTcpServer server;
	public init_java_service(String this_service_url,String service_port,String service_path) {
		this.this_service_url=this_service_url;
		this.service_port=service_port;
		this.service_path=service_path;
	}
	
	public boolean start_service() {
		String json_str=this.readJsonFile(this.service_path);
		try {
			JSONObject service_config=new JSONObject(json_str);
			if(service_config.getString("call_type").equals("rpc")) {
				String service_url="tcp://"+service_config.getString("service_host")+":"+this.service_port+"/"+this.this_service_url+service_config.getString("service_url");
				try {
					server = new HproseTcpServer(service_url);
					ArrayList<String> fun_name_array=new ArrayList<>();
					ArrayList<Class> fun_mod_name_array=new ArrayList<>();
					for(int i=0;i<service_config.getJSONArray("mods").length();i++) {
						String mod_file_path=service_config.getJSONArray("mods").getJSONObject(i).getString("file_path");
						String mod_fun_name=service_config.getJSONArray("mods").getJSONObject(i).getString("name");
						File file = new File(mod_file_path);
			            URLClassLoader loader;
						try {
							loader = new URLClassLoader(new URL[]{ file.toURI().toURL() });
							String class_name=file.getName().replace(".jar", "");
							Class clazz = loader.loadClass(class_name);
							for(int j=0;j<service_config.getJSONArray("mods").getJSONObject(i).getJSONArray("functions").length();j++) {
								String fun_name=service_config.getJSONArray("mods").getJSONObject(i).getJSONArray("functions").getJSONObject(j).getString("name");
								
								if(!fun_name_array.contains(fun_name)) {
									
									fun_name_array.add(fun_name);
									fun_mod_name_array.add(clazz);
								}else {
									System.out.println("same function name error");
								}
							}
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							return false;
						}catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							return false;
						}
					}
					
					for(int i=0;i<fun_name_array.size();i++) {
						try {
							
							server.add(fun_name_array.get(i), fun_mod_name_array.get(i).newInstance());
						} catch (InstantiationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							return false;
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							return false;
						}
					}
					
					try {
						server.start();
						System.in.read();
				        server.stop();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return false;
					}
					
					return true;
					
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
				
			
			}else {
				return false;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);

            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
	
	public static void main(String[] args) {
		if(args.length==3) {
			String this_service_url=args[0];			//服务独立添加url
			String service_port=args[1];
			String service_path=args[2];
			init_java_service init_obj=new init_java_service(this_service_url,service_port,service_path);
			init_obj.start_service();
		}else {
			System.out.println("param error");
			System.exit(0);
		}
	}
}