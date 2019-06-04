package Main.API_GATE.work;

import org.json.JSONException;
import org.json.JSONObject;

import Context.Main_Context;
import Main.API_GATE.service.HttpContext;
import Main.API_GATE.service.HttpResponseDealer;

public class response_controller{
	Main_Context mc;
	public response_controller(Main_Context mc) {
		this.mc=mc;
	}
	
	/**
	 * error
	 * @param httpContext
	 */
	public void error_response(HttpContext httpContext) {
		JSONObject jsonObject=new JSONObject();
		try {
			jsonObject.put("return_code",1);
			jsonObject.put("error_mes","error");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        HttpResponseDealer httpResponseDealer=new HttpResponseDealer(httpContext,this.mc);
        httpResponseDealer.response_text(jsonObject.toString());
	}
	
	/**
	 * out_time
	 * @param httpContext
	 */
	public void out_time_response(HttpContext httpContext) {
		JSONObject jsonObject=new JSONObject();
		try {
			jsonObject.put("return_code",1);
			jsonObject.put("error_mes","out time");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        HttpResponseDealer httpResponseDealer=new HttpResponseDealer(httpContext,this.mc);
        httpResponseDealer.response_text(jsonObject.toString());
	}
	
	/**
	 * no_request
	 * @param httpContext
	 */
	public void no_request_response(HttpContext httpContext) {
		JSONObject jsonObject=new JSONObject();
		try {
			jsonObject.put("return_code",2);
			jsonObject.put("error_mes","no_request");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        HttpResponseDealer httpResponseDealer=new HttpResponseDealer(httpContext,this.mc);
        httpResponseDealer.response_text(jsonObject.toString());
	}
	
	/**
	 * temp
	 * @param httpContext
	 */
	public void temp_response(HttpContext httpContext) {
		
	}
	
	/**
	 * default
	 * @param httpContext
	 */
	public void default_response(HttpContext httpContext) {
		
	}
	
	/**
	 * limit_flow
	 * @param httpContext
	 */
	public void limit_flow_response(HttpContext httpContext) {
		JSONObject jsonObject=new JSONObject();
		try {
			jsonObject.put("return_code",-1);
			jsonObject.put("error_mes","limit_flow");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        HttpResponseDealer httpResponseDealer=new HttpResponseDealer(httpContext,this.mc);
        httpResponseDealer.response_text(jsonObject.toString());
	}
	
	/**
	 * html[TODO]
	 * @param httpContext
	 */
	public void html_response(HttpContext httpContext,String message) {
		HttpResponseDealer httpResponseDealer=new HttpResponseDealer(httpContext,this.mc);
        httpResponseDealer.response_html(message);
	}
	
	/**
	 * text
	 * @param httpContext
	 */
	public void text_response(HttpContext httpContext,String message) {
		HttpResponseDealer httpResponseDealer=new HttpResponseDealer(httpContext,this.mc);
        httpResponseDealer.response_text(message);
	}
	
	/**
	 * json_text
	 * @param httpContext
	 */
	public void json_text_response(HttpContext httpContext,String message) {
        HttpResponseDealer httpResponseDealer=new HttpResponseDealer(httpContext,this.mc);
        httpResponseDealer.response_text(message);
	}
	
	/**
	 * forbid
	 * @param httpContext
	 */
	public void forbid_response(HttpContext httpContext) {
		JSONObject jsonObject=new JSONObject();
		try {
			jsonObject.put("return_code",-1);
			jsonObject.put("error_mes","forbid");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        HttpResponseDealer httpResponseDealer=new HttpResponseDealer(httpContext,this.mc);
        httpResponseDealer.response_text(jsonObject.toString());
	}
	
	
}