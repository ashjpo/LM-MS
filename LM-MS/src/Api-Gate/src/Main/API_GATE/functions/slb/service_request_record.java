package Main.API_GATE.functions.slb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Main.API_GATE.model.Service;

/**
 * ���ڼ�¼service+version������ ����slb
 * @author mmy
 *
 */
public class service_request_record{
	public Map<String,Service> service_map=new HashMap<>();	//serviceid service
	public Map<String,Integer> call_num_map=new HashMap<>();	//serviceid num
	public String last_call_serviceid="";
}