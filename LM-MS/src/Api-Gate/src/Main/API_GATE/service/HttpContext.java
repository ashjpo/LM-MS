package Main.API_GATE.service;



import java.nio.channels.SelectionKey;

/**  
 * @Title:  HttpContext.java   
 * @Description: HttpContext http上下文
 * @author: Han   
 * @date:   2016年7月16日 下午2:20:00  
 */  
public class HttpContext {

    private Request request;
    private Response response;
    
    public void setContext(String requestHeader, SelectionKey key) {
        //初始化request
    	this.request = new HttpRequest(requestHeader,key);
        //初始化response
    	this.response = new HttpResponse(key);
    }

    
    /**
     * 得到Request
     * @param:  @return  
     * @return: Request
     * @Autor: Han
     */
    public Request getRequest() {
        return request;
    }
    
    /**
     * 得到Response
     * @param:  @return  
     * @return: Response
     * @Autor: Han
     */
    public Response getResponse() {
        return response;
    }
}