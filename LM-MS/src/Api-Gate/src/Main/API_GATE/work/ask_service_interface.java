package Main.API_GATE.work;

import Main.API_GATE.service.HttpContext;

public interface ask_service_interface{
	public void request_rpc_ok(HttpContext httpContext,String return_message,String syn_asyn,match_ask match_ask,aggregation_dealer aggregation_dealer_obj);
	public void request_http_ok(HttpContext httpContext,String return_message,match_ask match_ask,aggregation_dealer aggregation_dealer_obj);
	public void request_error(HttpContext httpContext,match_ask match_ask,aggregation_dealer aggregation_dealer_obj);
}