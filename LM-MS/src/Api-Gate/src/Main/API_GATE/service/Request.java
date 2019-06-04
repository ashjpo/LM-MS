package Main.API_GATE.service;



import java.util.Map;
import java.util.Set;

/**  
 * @Title:  Request.java   
 * @Description: �ӿ����:Request�ӿ�
 * @author: Han   
 * @date:   2016��7��15�� ����9:21:45  
 */  
public interface Request {
    
    public static final String POST = "POST";
    
    public static final String GET = "GET";
    /**
     * �õ�����
     * @param:  @return  
     * @return: Map<String,Object>
     * @Autor: Han
     */
    public Map<String, String> get_getparams();
    
    public Map<String, String> get_postparams();
    
    public String get_request_ip();
    
    public String get_httpHeader();
    
    /**
     * �õ�����ʽ
     * @param:  @return  
     * @return: String
     * @Autor: Han
     */
    public String getMethod();
    
    /**
     * �õ�URI
     * @param:  @return  
     * @return: String
     * @Autor: Han
     */
    public String getUri();

    /**
     * �汾Э��
     * @param:  @return  
     * @return: String
     * @Autor: Han
     */
    public String getProtocol();

    /**
     * �õ�����ͷMap
     * @param:  @return  
     * @return: String
     * @Autor: Han
     */
    public Map<String, String> getHeaders();

    /**
     * �õ�����ͷ��������
     * @param:  @return  
     * @return: String
     * @Autor: Han
     */
    public Set<String> getHeaderNames();

    /**
     * ��������ͷ���õ���Ӧ������ͷ
     * @param:  @return  
     * @return: String
     * @Autor: Han
     */
    public Object getHeader(String key);
}