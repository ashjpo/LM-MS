package Main.API_GATE.service;



import java.nio.channels.SelectionKey;

/**  
 * @Title:  HttpContext.java   
 * @Description: HttpContext http������
 * @author: Han   
 * @date:   2016��7��16�� ����2:20:00  
 */  
public class HttpContext {

    private Request request;
    private Response response;
    
    public void setContext(String requestHeader, SelectionKey key) {
        //��ʼ��request
    	this.request = new HttpRequest(requestHeader,key);
        //��ʼ��response
    	this.response = new HttpResponse(key);
    }

    
    /**
     * �õ�Request
     * @param:  @return  
     * @return: Request
     * @Autor: Han
     */
    public Request getRequest() {
        return request;
    }
    
    /**
     * �õ�Response
     * @param:  @return  
     * @return: Response
     * @Autor: Han
     */
    public Response getResponse() {
        return response;
    }
}