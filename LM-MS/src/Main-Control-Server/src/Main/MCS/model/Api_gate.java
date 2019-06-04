package Main.MCS.model;


public class Api_gate{
	public String api_gate_name;
	public long start_time;
	//心跳包时间
	public long last_hart_time;
	public Api_gate(String api_gate_name) {
		this.api_gate_name=api_gate_name;
		this.start_time=System.currentTimeMillis();
		this.last_hart_time=System.currentTimeMillis();
	}
}