package Main.API_GATE.service;



/**  
 * @Title:  Handler.java   
 * @Description: �ӿ���ƣ�������Handler�ӿ�
 * @author: Han   
 * @date:   2016��7��12�� ����7:12:37  
 */  
public interface Handler {    
    /**
     * Get��ʽִ�и÷���
     * @param:  @param context  
     * @return: void
     * @Autor: Han
     */
    public void doGet();
    
    /**
     * POST��ʽִ�и÷���
     * @param:  @param context  
     * @return: void
     * @Autor: Han
     */
    public void doPost();
    
    /**
     * ����Handler(��û������... - -!)
     * @param:  @param context  
     * @return: void
     * @Autor: Han
     */
    public void destory();
}