package Main.API_GATE.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Context.Main_Context;
import Tools.MMY_Json;

/**
 * 用于控制AS和Service
 * @author mmy
 *
 */

public class Service_AS_controller{
	private MMY_Json mmy_Json=new MMY_Json();
	public Main_Context mc;
	ArrayList<service_as_update_interface> service_as_update_interface_list=new ArrayList<>();
	public Service_AS_controller(Main_Context mc) {
		this.mc=mc;
	}
	
	/**
	 * 添加回调
	 * @param service_as_update_interface
	 */
	public void set_service_as_update(service_as_update_interface service_as_update_interface) {
		this.service_as_update_interface_list.add(service_as_update_interface);
	}
	
	/**
	 * 节点加入
	 * @param message
	 */
	public void as_join(String message) {
		//{"mode":"AS-hard","function":"AS-node-join","node":"x"}
		JSONObject json_obj=mmy_Json.get_jsonobj(message);
		try {
			String node_id=json_obj.getString("node");
			if(mc.as_table.get_as_by_id(node_id)==null) {
				AS temp_as=new AS(node_id);
				this.mc.as_table.add_as(temp_as);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i=0;i<this.service_as_update_interface_list.size();i++) {
			this.service_as_update_interface_list.get(i).service_update();
		}
	}
	
	
	
	/**
	 * 服务注册
	 * @param message
	 */
	public void service_register(String message) {
		//{"mode":"AS-service","function":"AS-start-service-back","node":"x","service-mes":[]}
		JSONObject json_obj=mmy_Json.get_jsonobj(message);
		
		try {
			String node_id=json_obj.getString("node");
			JSONArray service_mes_array = json_obj.getJSONArray("service-mes");
			//判断节点是否存在
			AS temp_as=this.mc.as_table.get_as_by_id(node_id);
			if(temp_as!=null) {
				for(int i=0;i<service_mes_array.length();i++) {
					JSONObject service_mes=service_mes_array.getJSONObject(i);
					String service_id=service_mes_array.getJSONObject(i).getString("service_id");
					if(mc.service_table.get_all_service_by_id(service_id)==null) {
						Service service=new Service(service_mes);
						//System.out.println(service_id+"="+service.toString());
						temp_as.add_service(service);
						service.link_to_as(temp_as);
						this.mc.service_table.add_service(service);
					}
				}
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i=0;i<this.service_as_update_interface_list.size();i++) {
			this.service_as_update_interface_list.get(i).service_update();
		}
	}
	
	/**
	 * 节点退出
	 * @param message
	 */
	public void as_logout(AS as) {
		//{"mode":"AS-hard","function":"AS-node-logout","node":"x"}
		this.mc.service_table.delete_service_as(as);
		this.mc.as_table.delete_as(as);
		
		as=null;
		
		for(int i=0;i<this.service_as_update_interface_list.size();i++) {
			this.service_as_update_interface_list.get(i).service_update();
		}
	}
	
	/**
	 * 节点退出(id)
	 * @param message
	 */
	public void as_logout_id(String node_id) {
		AS temp_as=this.mc.as_table.get_as_by_id(node_id);
		if(temp_as!=null) {
			this.mc.service_table.delete_service_as(temp_as);
			this.mc.as_table.delete_as(temp_as);
		}
		
		
		temp_as=null;
		
		for(int i=0;i<this.service_as_update_interface_list.size();i++) {
			this.service_as_update_interface_list.get(i).service_update();
		}
	}
	
	
	
	
	/**
	 * 服务注销
	 * @param message
	 */
	public void service_logout(Service service) {
		this.mc.service_table.delete_service(service);
		if(service!=null) {
			String node_id=service.as_mes.node_id;
			this.mc.as_table.get_as_by_id(node_id).delete_service(service);
			
		}
		service=null;
		
		for(int i=0;i<this.service_as_update_interface_list.size();i++) {
			this.service_as_update_interface_list.get(i).service_update();
		}
	}
	
	/**
	 * 服务注销(id)
	 * @param message
	 */
	public void service_logout_id(String service_id) {
		Service service=this.mc.service_table.get_all_service_by_id(service_id);
		if(service!=null) {
			this.mc.service_table.delete_service(service);
			service.as_mes.delete_service(service);
			
			service=null;
		}
		
		for(int i=0;i<this.service_as_update_interface_list.size();i++) {
			this.service_as_update_interface_list.get(i).service_update();
		}
		
	}
	
	/**
	 * 服务注销(svhashid)
	 * @param message
	 */
	public void service_logout_name_version(String service_name_l,String version_l,String node_id) {
		
		AS temp_as=this.mc.as_table.get_as_by_id(node_id);
		if(temp_as!=null) {
			//ArrayList<Service> service_list=temp_as.get_service_name_version(service, version);
			//ArrayList<Service> del_list=new ArrayList<>();
			ArrayList<Service> del_list=new ArrayList<>();
			if(service_name_l.equals("*") && version_l.equals("*")) {	//所有
				ArrayList<Service> service_list=temp_as.get_all_service();
				del_list=service_list;
			}else if(service_name_l.equals("*") && !version_l.equals("*")) {
				String[] versionarr=version_l.split("\\|");
				
				for(int i=0;i<versionarr.length;i++) {
					ArrayList<Service> service_list=temp_as.get_all_service_by_version(versionarr[i]);
					del_list.addAll(service_list);
				}
			}else if(!service_name_l.equals("*") && version_l.equals("*")) {
				String[] service_namearr=service_name_l.split("\\|");
				
				for(int i=0;i<service_namearr.length;i++) {
					ArrayList<Service> service_list=temp_as.get_all_service_by_service_name(service_namearr[i]);
					del_list.addAll(service_list);
				}
			}else {
				ArrayList<Service> mid_list=new ArrayList<>();
				
				String[] service_namearr=service_name_l.split("\\|");
				String[] versionarr=version_l.split("\\|");
				for(int i=0;i<service_namearr.length;i++) {
					String service_name=service_namearr[i];
					ArrayList<Service> temp_list=temp_as.get_all_service_by_service_name(service_name);
					mid_list.addAll(temp_list);
				}
				
				for(int i=0;i<mid_list.size();i++) {
					if(Arrays.asList(versionarr).contains(mid_list.get(i).version)) {
						del_list.add(mid_list.get(i));
					}
				}
				
				
			}

			this.mc.service_table.delete_service_list(del_list);
			temp_as.delete_service_list(del_list);
			del_list=null;
			
		}
		
		for(int i=0;i<this.service_as_update_interface_list.size();i++) {
			this.service_as_update_interface_list.get(i).service_update();
		}
		
		
	}
	
}