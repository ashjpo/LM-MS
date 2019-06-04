package Main.AS.model;
import java.util.Arrays;
import java.util.ArrayList;

public class service_model_table{
	ArrayList<service_model> sm_list=new ArrayList<>();
	
	/**
	 * 添加service_model
	 * @param service_model
	 */
	public void add_service_model(service_model service_model) {
		sm_list.add(service_model);
	}
	
	public void delete_service_model(service_model service_model) {
		sm_list.remove(service_model);
	}
	
	
	/**
	 * 获取service_model
	 * @param service_name
	 * @param version
	 * @return
	 */
	public ArrayList<service_model> get_service_model(String service_name_l,String version_l) {
		ArrayList<service_model> return_list=new ArrayList<>();
		if(service_name_l.equals("*") && version_l.equals("*")) {	//所有
			return_list=sm_list;
			return return_list;
		}else if(service_name_l.equals("*") && !version_l.equals("*")) {
			String[] versionarr=version_l.split("\\|");
			for(int i=0;i<versionarr.length;i++) {
				String version=versionarr[i];
				ArrayList<service_model> temp_list=this.get_service_model_by_version(version);
				return_list.addAll(temp_list);
			}
			return return_list;
		}else if(!service_name_l.equals("*") && version_l.equals("*")) {
			String[] service_namearr=service_name_l.split("\\|");
			for(int i=0;i<service_namearr.length;i++) {
				String service_name=service_namearr[i];
				ArrayList<service_model> temp_list=this.get_service_model_by_name(service_name);
				return_list.addAll(temp_list);
			}
			return return_list;
		}else {
			ArrayList<service_model> mid_list=new ArrayList<>();
			String[] service_namearr=service_name_l.split("\\|");
			String[] versionarr=version_l.split("\\|");
			for(int i=0;i<service_namearr.length;i++) {
				String service_name=service_namearr[i];
				ArrayList<service_model> temp_list=this.get_service_model_by_name(service_name);
				mid_list.addAll(temp_list);
			}
			
			for(int i=0;i<mid_list.size();i++) {
				
				if(Arrays.asList(versionarr).contains(mid_list.get(i).version)) {
					return_list.add(sm_list.get(i));
				}
			}
			return return_list;
		}
		
	}
	
	/**
	 * 获取某个version的服务【不需要直接调用】
	 * @param version
	 * @return
	 */
	private ArrayList<service_model> get_service_model_by_version(String version) {
		ArrayList<service_model> return_list=new ArrayList<>();
		for(int i=0;i<sm_list.size();i++) {
			if(sm_list.get(i).version.equals(version)) {
				return_list.add(sm_list.get(i));
			}
		}
		
		return return_list;
	}
	
	/**
	 * 获取某个name的服务【不需要直接调用】
	 * @param name
	 * @return
	 */
	private ArrayList<service_model> get_service_model_by_name(String name) {
		ArrayList<service_model> return_list=new ArrayList<>();
		for(int i=0;i<sm_list.size();i++) {
			if(sm_list.get(i).service_name.equals(name)) {
				return_list.add(sm_list.get(i));
			}
		}
		
		return return_list;
	}
}