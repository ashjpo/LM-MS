package Main.API_GATE.service;



import java.nio.channels.SelectionKey;

/**  
 * @Title:  Response.java   
 * @Description: �ӿ���ƣ�response�ӿ�
 * @author: Han   
 * @date:   2016��7��16�� ����2:19:25  
 */  
public interface Response {
    
    //����������
    public static final String SERVER_NAME = "MMY_Service";
    
    public String getContentType();
    
    public int getStatuCode();
    
    public String getStatuCodeStr();
    
    public String getHtmlFile();
    
    public void setHtmlFile(String htmlFile);
    
    public SelectionKey getKey();
    
    public void setContentType(String contentType);
    
    public void setStatuCode(int statuCode);
    
    public void setStatuCodeStr(String statuCodeStr);
}