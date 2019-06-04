package Main.AS.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class service_model{
	public String config_json_path;
	public String call_type;
	public String service_name;
	public String version;
	public String service_type;
	public String svhashid;
	public String service_host;
	//public String service_port;
	public String version_code;
	public String language;
	public String api_gate;	//|分割
	public String service_mes;
	public String service_url;
	public JSONArray fun_array=new JSONArray();
	public boolean model_if_ok=false;
	JSONObject service_json;
	public service_model(String config_json_path) {
		this.config_json_path=config_json_path;
		String json_str=readJsonFile(config_json_path);
		if(json_str!=null) {
			try {
				service_json=new JSONObject(json_str);
				call_type=service_json.getString("call_type");
				service_name=service_json.getString("name");
				version=service_json.getString("version");
				service_type=service_json.getString("service_type");
				svhashid=service_name+version;
				service_host=service_json.getString("service_host");
				//service_port=service_json.getString("service_port");
				language=service_json.getString("language");
				api_gate=service_json.getString("api-gate");
				service_mes=service_json.getString("service_mes");
				version_code=service_json.getString("version_code");
				
				try {
					service_url=service_json.getString("service_url");
				} catch (JSONException e) {
					service_url="";
				}
				try {
					if(call_type.equals("rpc")) {
						if(if_has_same_name_fun(service_json.getJSONArray("mods"))) {
							model_if_ok=false;
						}else {
							model_if_ok=true;
						}
					}else if(call_type.equals("http")){
						if(if_has_same_name_fun(service_json.getJSONArray("functions"))) {
							model_if_ok=false;
						}else {
							model_if_ok=true;
						}
					}
				} catch (JSONException e) {
					model_if_ok=true;
				}
				
				
				
			} catch (JSONException e) {
				model_if_ok=false;
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 查询是否在同一个服务下是否有相同函数名
	 * @return
	 */
	protected boolean if_has_same_name_fun(JSONArray mod_array) {
		if(call_type.equals("rpc")) {
			ArrayList<String> fun_name_list=new ArrayList<>();
			try {
				for(int i=0;i<mod_array.length();i++) {
					for(int j=0;j<mod_array.getJSONObject(i).getJSONArray("functions").length();j++) {
						fun_array.put(mod_array.getJSONObject(i).getJSONArray("functions").getJSONObject(j));
						String temp_name=mod_array.getJSONObject(i).getJSONArray("functions").getJSONObject(j).getString("name");
						if(fun_name_list.contains(temp_name)) {
							fun_name_list.add(temp_name);
							return false;
						}
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			return true;
		}else if(call_type.equals("http")) {
			ArrayList<String> fun_name_list=new ArrayList<>();
			try {
				for(int i=0;i<mod_array.length();i++) {
					fun_array.put(mod_array.getJSONObject(i));
					String temp_name=mod_array.getJSONObject(i).getString("name");
					if(fun_name_list.contains(temp_name)) {
						fun_name_list.add(temp_name);
						return false;
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			return true;
		}else {
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

	
}