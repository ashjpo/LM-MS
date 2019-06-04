package Main.API_GATE.service;



/**  
 * @Title:  Handler.java   
 * @Description: 接口设计：处理器Handler接口
 * @author: Han   
 * @date:   2016年7月12日 下午7:12:37  
 */  
public interface Handler {    
    /**
     * Get形式执行该方法
     * @param:  @param context  
     * @return: void
     * @Autor: Han
     */
    public void doGet();
    
    /**
     * POST形式执行该方法
     * @param:  @param context  
     * @return: void
     * @Autor: Han
     */
    public void doPost();
    
    /**
     * 销毁Handler(并没有销毁... - -!)
     * @param:  @param context  
     * @return: void
     * @Autor: Han
     */
    public void destory();
}