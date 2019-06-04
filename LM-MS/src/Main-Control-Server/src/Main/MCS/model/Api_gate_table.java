package Main.MCS.model;

import java.util.ArrayList;

import Context.Main_Context;

public class Api_gate_table{
	Main_Context mc;
	public ArrayList<Api_gate> api_gate_list=new ArrayList<>();
	public Api_gate_table(Main_Context mc) {
		this.mc=mc;
	}
	
	public ArrayList<Api_gate> get_api_gate(){
		return this.api_gate_list;
	}
	
	/**
	 * É¾³ıapi-gate
	 * @param api_gate
	 */
	public void delete_api_gate(Api_gate api_gate) {
		api_gate_list.remove(api_gate);
	}
	
	/**
	 * Ìí¼Óapi-gate
	 * @param as
	 */
	public void add_as(Api_gate api_gate) {
		if(this.get_api_gate_by_name(api_gate.api_gate_name)==null) {
			//System.out.println(api_gate.toString());
			api_gate_list.add(api_gate);
		}
	}
	
	public Api_gate get_api_gate_by_name(String api_gate_name) {
		for(int i=0;i<api_gate_list.size();i++) {
			if(api_gate_list.get(i).api_gate_name.equals(api_gate_name)) {
				return api_gate_list.get(i);
			}
		}
		
		return null;
	}
}