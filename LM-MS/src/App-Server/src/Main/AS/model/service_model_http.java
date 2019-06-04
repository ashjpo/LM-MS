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

public class service_model_http extends service_model{
	public JSONArray fun_array=new JSONArray();
	JSONObject service_json;
	public service_model_http(String config_json_path) {
		super(config_json_path);
		this.config_json_path=config_json_path;
		String json_str=super.readJsonFile(config_json_path);
		if(json_str!=null) {
			try {
				
				if(if_has_same_name_fun(service_json.getJSONArray("functions"))) {
					model_if_ok=false;
				}else {
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
	protected boolean if_has_same_name_fun(JSONArray fun_array) {
		ArrayList<String> fun_name_list=new ArrayList<>();
		try {
			for(int i=0;i<fun_array.length();i++) {
				fun_array.put(fun_array.getJSONObject(i));
				String temp_name=fun_array.getJSONObject(i).getString("name");
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
	}
	

}