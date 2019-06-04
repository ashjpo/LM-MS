package Tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

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