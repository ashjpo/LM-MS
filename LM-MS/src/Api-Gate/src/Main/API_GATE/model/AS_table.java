package Main.API_GATE.model;

import java.util.ArrayList;

import Context.Main_Context;

public class AS_table{
	Main_Context mc;
	public AS_table(Main_Context mc) {
		this.mc=mc;
	}
	public ArrayList<AS> as_list=new ArrayList<>();
	/**
	 * 添加AS
	 * @param as
	 */
	public void add_as(AS as) {
		if(this.get_as_by_id(as.node_id)==null) {
			as_list.add(as);
		}
	}
	
	
	/**
	 * 删除AS
	 * @param as
	 */
	public void delete_as(AS as) {
		AS temp_as=get_as_by_id(as.node_id);
		if(temp_as!=null) {
			as_list.remove(as);
		}
	}
	
	public ArrayList<AS> get_all_as(){
		return this.as_list;
	}
	
	/**
	 * 通过service_id查询service
	 * @param service_id
	 * @return Service/null
	 */
	public AS get_as_by_id(String node_id) {
		for(int i=0;i<as_list.size();i++) {
			//System.out.println(as_list.size()+"="+as_list.get(i).node_id);
			if(as_list.get(i).node_id.equals(node_id)) {
				return as_list.get(i);
			}
		}
		
		return null;
	}
}