package Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MMY_Json{
    public boolean isJson(String content){
        try {
            JSONObject jsonObject=new JSONObject(content);

            return  true;
        } catch (Exception e) {
            return false;
        }
    }

    public JSONObject get_jsonobj(String json_str){
        JSONObject jsonStr= null;
        try {
            jsonStr = new JSONObject(json_str);
            return jsonStr;
        } catch (JSONException e) {
            return null;
        }
    }

    public JSONArray get_jsonarray(String json_str){
        JSONArray jsonArray=null;
        try {
            jsonArray=new JSONArray(json_str);
            return jsonArray;
        } catch (JSONException e) {
            return null;
        }
    }
}