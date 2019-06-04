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

public class service_model_rpc extends service_model{
	JSONObject service_json;
	public service_model_rpc(String config_json_path) {
		super(config_json_path);
		this.config_json_path=config_json_path;
		String json_str=super.readJsonFile(config_json_path);
		if(json_str!=null) {
			try {
				service_json=new JSONObject(json_str);
				try {
					service_url=service_json.getString("service_url");
				} catch (JSONException e) {
					service_url="";
				}
				try {
					if(if_has_same_name_fun(service_json.getJSONArray("mods"))) {
						model_if_ok=false;
					}else {
						model_if_ok=true;
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
	}
	

}