package Main.API_GATE.service;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.Map;
import java.util.Set;

import Context.Main_Context;
import Main.API_GATE.work.response_controller;

public class HttpRequestDealer implements Runnable{
	private SelectionKey key;
	private HttpContext httpcontext = new HttpContext();
	private String requestHeader;
	Main_Context mc;
	public HttpRequestDealer(String requestHeader, SelectionKey key,Main_Context mc) {
        this.key = key;
        this.requestHeader = requestHeader;
        this.mc=mc;
    }
	@Override
	public void run() {
		//System.out.println(requestHeader);
		try {
			httpcontext.setContext(requestHeader, key);
			//System.out.println(requestHeader);
			this.mc.service_dispatch_obj.deal_request(httpcontext);
		}catch (Exception e) {
			e.printStackTrace();
			try {
				key.channel().close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			
		}

	}
	
}