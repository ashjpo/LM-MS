package Main.API_GATE.service;




import java.nio.channels.SelectionKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;



/**  
 * @Title:  HttpRequest.java   
 * @Description: HTTP����(���кܶ෽������д��)
 * @author: Han   
 * @date:   2016��7��15�� ����9:16:45  
 */  
public class HttpRequest implements Request {
    
    //����
    private Map<String, String> get_params = new HashMap<>();
    private Map<String, String> post_params = new HashMap<>();
    
    //����ͷ(Request Header)
    private Map<String, String> headers = new HashMap<>();
    
    //���󷽷�
    private String method;
    
    //uri
    private String uri;
    
    //Э��汾
    private String protocol;
    
    private String request_ip;
    
    private String httpHeader;
    
    public HttpRequest(String httpHeader,SelectionKey key) {
    	this.request_ip=key.channel().toString().split("/")[2].replace("]","").split(":")[0];
    	this.httpHeader=httpHeader;
        init(httpHeader);
    }
    
    public String get_httpHeader() {
		return this.httpHeader;
	}
    
    public String get_request_ip() {
		return this.request_ip;
	}

    private void init(String httpHeader) {
        //���������
        String[] headers = httpHeader.split("\r\n");
        //��������ʽ
        initMethod(headers[0]);
        //����URI
        initURI(headers[0]);
        //���ð汾Э��
        initProtocol(headers[0]);
        //��������ͷ
        initRequestHeaders(headers);
    }

    /**
     * �������󷽷�
     * @param:  @param str  
     * @return: void
     * @Autor: Han
     */
    private void initMethod(String str) {
        method = str.substring(0, str.indexOf(" "));
    }
    
    /**
     * ����request����
     * @param:  @param attr  
     * @return: void
     * @Autor: Han
     */
    private void initgetAttribute(String attr) {
        String[] attrs = attr.split("&");
        for (String string : attrs) {
            String key = string.substring(0, string.indexOf("="));
            String value = string.substring(string.indexOf("=") + 1);
            get_params.put(key, value);
        }
    }
    
    /**
     * ����request����
     * @param:  @param attr  
     * @return: void
     * @Autor: Han
     */
    private void initpostAttribute(String attr) {
    	//System.out.println(attr);
        String[] attrs = attr.split("&");
        for (String string : attrs) {
            String key = string.substring(0, string.indexOf("="));
            String value = string.substring(string.indexOf("=") + 1);
            post_params.put(key, value);
        }
    }

    /**
     * ����uri
     * @param:  @param str  
     * @return: void
     * @Autor: Han
     */
    private void initURI(String str) {
        uri = str.substring(str.indexOf(" ") + 1, str.indexOf(" ", str.indexOf(" ") + 1));
        //�����get�������������Ų���   /index?a=1&b=2
        //if(method.toUpperCase().equals("GET")) {
        //���ʺű�ʾ������в���
        if(uri.contains("?")) {
             String attr = uri.substring(uri.indexOf("?") + 1, uri.length());
            uri = uri.substring(0, uri.indexOf("?"));
            initgetAttribute(attr);
        }
        //}
    }
    
    /**
     * ��ʼ������ͷ
     * @param:  @param strs  
     * @return: void
     * @Autor: Han
     */
    private void initRequestHeaders(String[] strs) {
        //ȥ����һ��
    	int now_line=0;
        for(int i = 1; i < strs.length; i++) {
        	try {
        		//System.out.println(strs[i]+">>>");
                String key = strs[i].substring(0, strs[i].indexOf(":"));
                String value = strs[i].substring(strs[i].indexOf(":") + 1);
                headers.put(key, value);
			} catch (Exception e) {
				now_line=i;
				break;
			}
        }
        int post_parmas_line=now_line+1;
        if(strs[post_parmas_line].contains("=")) {
        	initpostAttribute(strs[post_parmas_line]);
        }
        
    }
    
    /**
     * ����Э��汾
     * @param:  @param str  
     * @return: void
     * @Autor: Han
     */
    private void initProtocol(String str) {
        protocol = str.substring(str.lastIndexOf(" ") + 1, str.length());
    }

    @Override
    public Map<String, String> get_getparams() {
        return get_params;
    }
    
    @Override
    public Map<String, String> get_postparams() {
        return post_params;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getUri() {
        return uri;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public Set<String> getHeaderNames() {
        return headers.keySet();
    }

    @Override
    public Object getHeader(String key) {
        return headers.get(key);
    }
}