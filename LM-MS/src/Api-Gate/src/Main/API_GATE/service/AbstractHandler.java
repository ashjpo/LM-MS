package Main.API_GATE.service;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Context.Main_Context;

/**  
 * @Title:  AbstractHandler.java   
 * @Description: Handler抽象类
 * @author: Han   
 * @date:   2016年7月16日 下午2:11:57  
 */  
public class AbstractHandler implements Handler {
    
    protected HttpContext context;
    Main_Context mc;
    public AbstractHandler(HttpContext context,Main_Context mc) {
        this.context = context;
        this.mc=mc;
    }

    @Override
    public void doGet() {
        
    }

    @Override
    public void doPost() {
        
    }

    @Override
    public void destory() {
        context = null;
    }

    /**
     * 通过上下文，返回封装response响应
     * @param:  @param context  
     * @return: void
     * @Autor: Han
     */
    protected void sendResponse_text(String return_message) {
        //new ResponseHandler().write(context);
    	Request request = context.getRequest();
        Response response = context.getResponse();
    	String protocol = request.getProtocol();
    	int statuCode = response.getStatuCode();
    	String statuCodeStr = response.getStatuCodeStr();
    	String serverName = Response.SERVER_NAME;
    	String contentType = response.getContentType();
    	SelectionKey key = response.getKey();
    	Selector selector = key.selector();
    	SocketChannel channel = (SocketChannel)key.channel();
    	
    	if(this.isJson(return_message)) {
    		contentType="application/json;";
    	}else {
    		contentType="text/plain;";
    	}
    	//???
        //contentType="text/html;";
        StringBuilder sb = new StringBuilder();
        //状态行
        sb.append(protocol + " " + statuCode + " " + statuCodeStr + "\r\n");
        //响应头
        sb.append("Server: " + serverName + "\r\n");
        sb.append("Content-Type: " + contentType + "charset=utf-8\r\n");
        //sb.append("Transfer-Encoding: chunked"+ "\r\n");
        sb.append("Date: " + new Date() + "\r\n");
        sb.append("vary: Accept-Encoding"+ "\r\n");
        //响应内容
        sb.append("\n");
        //sb.append(html);
        //test_json="aaa";
        sb.append(return_message);
        ByteBuffer buffer;
		try {
			buffer = ByteBuffer.wrap(sb.toString().getBytes("UTF-8"));
			channel.register(selector, SelectionKey.OP_WRITE);

			if(channel.isOpen()) {
				channel.write(buffer);
			}
			if(channel.isOpen()) {
				channel.shutdownInput(); 
				channel.shutdownOutput(); 
				channel.close();
			}
			
		} catch (UnsupportedEncodingException | ClosedChannelException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
			try {
				channel.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}catch(Throwable t) {
			try {
				channel.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}


        
    }
   
    
    /**
     * [TODO]
     * @param html_path
     */
    protected void sendResponse_html(String return_message) {
    	//new ResponseHandler().write(context);
    	Request request = context.getRequest();
        Response response = context.getResponse();
    	String protocol = request.getProtocol();
    	int statuCode = response.getStatuCode();
    	String statuCodeStr = response.getStatuCodeStr();
    	String serverName = Response.SERVER_NAME;
    	String contentType = response.getContentType();
    	SelectionKey key = response.getKey();
    	Selector selector = key.selector();
    	SocketChannel channel = (SocketChannel)key.channel();
    	
        contentType="text/html;";
        StringBuilder sb = new StringBuilder();
        //状态行
        sb.append(protocol + " " + statuCode + " " + statuCodeStr + "\r\n");
        //响应头
        sb.append("Server: " + serverName + "\r\n");
        sb.append("Content-Type: " + contentType + "charset=utf-8\r\n");
        //sb.append("Transfer-Encoding: chunked"+ "\r\n");
        sb.append("Date: " + new Date() + "\r\n");
        sb.append("vary: Accept-Encoding"+ "\r\n");
        //响应内容
        sb.append("\n");
        //sb.append(html);
        //test_json="aaa";
        sb.append(return_message);
        ByteBuffer buffer;
		try {
			buffer = ByteBuffer.wrap(sb.toString().getBytes("UTF-8"));
			channel.register(selector, SelectionKey.OP_WRITE);

			if(channel.isOpen()) {
				channel.write(buffer);
			}
			if(channel.isOpen()) {
				channel.shutdownInput(); 
				channel.shutdownOutput(); 
				channel.close();
			}
			
		} catch (UnsupportedEncodingException | ClosedChannelException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
			try {
				channel.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}catch(Throwable t) {
			try {
				channel.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
    }
    
    public boolean isJson(String content) {
    	try {
            JSONObject jsonObject=new JSONObject(content);
            return  true;
    	} catch (Exception e) {
			try {
				JSONArray jsonObject=new JSONArray(content);
				return  true;
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				return  false;
			}
					
	    }
    }
}