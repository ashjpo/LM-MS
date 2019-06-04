package Main.API_GATE.service;

import Context.Main_Context;

public class HttpResponseDealer extends AbstractHandler{
	Main_Context mc;
	public HttpResponseDealer(HttpContext context,Main_Context mc) {
		super(context,mc);
		this.mc=mc;
	}
	
	@Override
	public void doGet() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void doPost() {
		// TODO Auto-generated method stub
		
	}
	
    public void response_text(String message) {
        //通过请求方式选择具体解决方法
        String method = context.getRequest().getMethod();
        if(method.equals(Request.GET)) {
            this.doGet();
        } else if (method.equals(Request.POST)) {
            this.doPost();
        }
		super.sendResponse_text(message);
    }
    
    public void response_html(String message) {
        //通过请求方式选择具体解决方法
        String method = context.getRequest().getMethod();
        if(method.equals(Request.GET)) {
            this.doGet();
        } else if (method.equals(Request.POST)) {
            this.doPost();
        }
		super.sendResponse_html(message);
    }
}