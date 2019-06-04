package Main.MCS.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author mmy
 *
 *	service_id							service_id
 *	api-gate-name						��©����api����
 *	��������								service_type(common,error,nodirect,fusing)
 *	hashid��������+�汾�ţ�				hashid
 *	������ 								service_name 
 *	�汾�� 								version
 *	ע��ʱ�� 							register_time
 *	�����б�									ask_list										
 *	as��Ϣ								as_mes
 *	������Ϣ								service_mes
 *		����								
 *
 */
public class Service{
	public String service_id;
	public String call_type;
	public String api_gate_name;
	public String service_type;
	public String svhashid;
	public String service_name;
	public String version;
	public long register_time;
	public ArrayList<Ask> ask_list;
	public AS as_mes;
	
	public boolean if_start_on_node=false;
	public boolean if_regist_on_api_gate=false;
	//������ʱ��
	public long last_hart_time;
	
	public Service(JSONObject service_mes) {
		last_hart_time=System.currentTimeMillis();
		try {
			this.service_id=service_mes.getString("service_id");
			this.call_type=service_mes.getString("call_type");
			this.api_gate_name=service_mes.getString("api-gate");
			this.service_type=service_mes.getString("service_type");
			this.service_name=service_mes.getString("service_name");
			this.version=service_mes.getString("version");
			this.svhashid=this.service_name+this.version;
			register_time=System.currentTimeMillis();
			/*JSONArray ask_list_json=service_mes.getJSONArray("ask_list");
			for(int i=0;i<ask_list_json.length();i++) {
				//ask_list.add((Ask)ask_list_json.getClass());
				
			}*/
			if_start_on_node=true;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * �����api-gate�ϵ�ע��
	 */
	public void regist_service_on_api_gate() {
		if_regist_on_api_gate=true;
	}
	
	/**
	 * ����as�Ľڵ���Ϣ
	 * @param as_mes
	 */
	public void link_to_as(AS as_mes) {
		if_start_on_node=true;
		this.as_mes=as_mes;
	}
	
	
	
}








