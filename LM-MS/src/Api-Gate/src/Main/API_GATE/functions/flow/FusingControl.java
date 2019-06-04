package Main.API_GATE.functions.flow;


import java.util.Timer;
import java.util.TimerTask;

import Context.Main_Context;

public class FusingControl{
	public String fusing_status="open";	//(open/midopen/close)
	public long limit_fusing_error_times=100;
	public long recover_open_times=5;
	
	public long recover_midopen_time=60;	//60��ָ��ɰ뿪
	//��������Ĵ���
	public long error_times=0;
	//mid_open->open
	public long success_times=0;
	
	private boolean if_use=false;
	
	//�ж��ٴ������ɹ����֮ǰ��error_times
	public long success_clear_error_times=0;
	public boolean last_ask_if_success=true;
	
	Timer recover_Timer = new Timer();
	Main_Context mc;
	public FusingControl(Main_Context mc) {
		this.mc=mc;
		this.if_use=mc.config_obj.fusing_if_use;
	}
	
	public static FusingControl newFusingControl(Main_Context mc) {
        return new FusingControl(mc);
    }
	
	
	
	/**
	 * �����۶����ƴ���
	 * @param limit_fusing_error_times
	 */
	public FusingControl set_limit_fusing_error_times(long error_times) {
		this.limit_fusing_error_times=error_times;
		return this;
	}
	
	
	/**
	 * ���ö��ٴδӰ뿪�ָ���ȫ��
	 * @param recover_open_times
	 */
	public FusingControl set_recover_open_times(long recover_open_times) {
		this.recover_open_times=recover_open_times;
		return this;
	}
	
	/**
	 * �ָ��ɰ뿪��ʱ��
	 * @param time
	 */
	public FusingControl set_recover_midopen_time(long time) {
		this.recover_midopen_time=time;
		return this;
	}
	
	/**
	 * midopen->open
	 */
	public void midopen_to_open() {
		this.error_times=0;
		this.set_fusing_status("open");
		this.success_times=0;
	}
	
	/**
	 * close->midopen
	 */
	public void close_to_midopen() {
		this.error_times=0;
		this.set_fusing_status("midopen");
		this.success_times=0;
	}
	
	public void midopen_to_close() {
		this.error_times=0;
		this.set_fusing_status("close");
		this.success_times=0;
		recover_Timer.schedule(new TimerTask() {
            @Override
            public void run() {
            	close_to_midopen();
            }
        },recover_midopen_time*1000);
		
	}
	
	
	/**
	 * open->close
	 */
	public void open_to_close() {
		this.error_times=0;
		this.set_fusing_status("close");
		this.success_times=0;
		this.success_clear_error_times=0;
		recover_Timer.schedule(new TimerTask() {
            @Override
            public void run() {
            	close_to_midopen();
            }
        },recover_midopen_time*1000);
	}
	
	
	/**
	 * �ڱ��midopen״̬�����óɹ��Ĵ���
	 */
	public void set_midopen_success() {
		if(this.fusing_status.equals("midopen")) {
			this.success_times+=1;
			if(this.success_times>this.recover_open_times) {
				midopen_to_open();
			}
		}
	}
	
	/**
	 * ���������������
	 */
	public void set_open_error() {
		if(this.fusing_status.equals("open")) {
			this.error_times+=1;
			if(this.error_times>this.limit_fusing_error_times) {
				open_to_close();
			}
		}else if(this.fusing_status.equals("midopen")) {
			midopen_to_close();
		}
		
	}
	
	/**
	 * ��������ɹ������á�
	 */
	public void set_ask_ok() {
		if(this.fusing_status.equals("open")) {
			if(last_ask_if_success) {
				success_clear_error_times+=1;
			}else {
				success_clear_error_times=1;
			}
			
			if(success_clear_error_times>5) {
				this.error_times=0;
			}
			set_midopen_success();
			
		}
		
		set_midopen_success();
		last_ask_if_success=true;
	}
	
	
	/**
	 * ��������ʧ�ܡ����á�
	 */
	public void set_ask_no() {
		set_open_error();
		last_ask_if_success=false;
	}
	
	/**
	 * �ж��Ƿ����ͨ�������á�
	 * @return
	 */
	public boolean if_through() {
		if(if_use) {
			if(this.fusing_status.equals("close")) {
				return false;
			}else {
				return true;
			}
		}else {
			return true;
		}
	}
	
	
	
	
	/**
	 * �����۶�״̬
	 * @param fusing_status
	 */
	private void set_fusing_status(String fusing_status) {
		if(fusing_status.equals("open")) {
			this.fusing_status="open";
		}else if(fusing_status.equals("midopen")) {
			this.fusing_status="midopen";
		}else if(fusing_status.equals("close")) {
			this.fusing_status="close";
		}else {
			this.fusing_status="open";
		}
	}
	
	
	
	
	
	
	
}