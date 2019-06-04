package Main.API_GATE.service;




import java.nio.channels.SelectionKey;


/**  
 * @Title:  HttpResponse.java   
 * @Description: http��Ӧ
 * @author: Han   
 * @date:   2016��7��16�� ����2:20:41  
 */  
public class HttpResponse implements Response {
    
    private SelectionKey key;
    //��������  defalut Ϊtext/html
    private String contentType = "application/json";
    //��Ӧ��  defalut Ϊ200
    private int StatuCode = 200;
    private String statuCodeStr = "OK";
    private String htmlFile = "";

    public HttpResponse(SelectionKey key) {
        this.key = key;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public int getStatuCode() {
        return StatuCode;
    }

    @Override
    public SelectionKey getKey() {
        return key;
    }

    @Override
    public String getStatuCodeStr() {
        return statuCodeStr;
    }

    @Override
    public String getHtmlFile() {
        return htmlFile;
    }

    @Override
    public void setHtmlFile(String htmlFile) {
        this.htmlFile = htmlFile;
    }

    @Override
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public void setStatuCode(int statuCode) {
        StatuCode = statuCode;
    }

    @Override
    public void setStatuCodeStr(String statuCodeStr) {
        this.statuCodeStr = statuCodeStr;
    }
}