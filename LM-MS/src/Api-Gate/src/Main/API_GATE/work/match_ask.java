package Main.API_GATE.work;

import java.util.HashMap;
import java.util.Map;

import Main.API_GATE.model.Ask;
import Main.API_GATE.model.Service;

public class match_ask{
	public String url;
	public Map<String,Service> service_map=new HashMap<>();	//<serviceid,Service>
	public Map<String,Ask> ask_map=new HashMap<>();	//<serviceid,Service>
	//public Map<String,Integer> ask_slb=new HashMap<>();	//<serviceid,int>
	//public String lask_ask_service_id="";	//…œ¥Œ«Î«Ûservice_id
}