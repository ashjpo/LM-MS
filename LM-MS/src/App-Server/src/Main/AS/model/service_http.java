package Main.AS.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.dgc.Lease;
import java.util.Random;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import Context.Main_Context;

public class service_http extends service{
	public service_model_rpc service_model_obj;
	public service_http(service_model service_model_obj,Main_Context mc) {
		super(service_model_obj, mc);
		
	}
	

	
	
	
}