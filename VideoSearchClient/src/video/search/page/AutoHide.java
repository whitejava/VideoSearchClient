package video.search.page;

import android.os.Handler;
import android.os.Message;
import android.view.View;

class AutoHide {
	private View v;
	private int visibleDuration;
	private boolean activated;
	private Handler timer = new Timer();
	
	public AutoHide(View v, int visibleDuration){
		this.v = v;
		this.visibleDuration = visibleDuration;
		deactivate();
	}
	
	public synchronized void activate(){
		if(activated){
			timer.removeMessages(0);
		}
		activated = true;
		v.setVisibility(View.VISIBLE);
		setTimer();
	}
	
	private void setTimer(){
		Message m = new Message();
		m.what = 0;
		timer.sendMessageDelayed(m, visibleDuration);
	}
	
	private synchronized void deactivate(){
		activated = false;
		v.setVisibility(View.INVISIBLE);
	}

	private class Timer extends Handler {
		@Override
		public void handleMessage(Message m){
			deactivate();
		}
	}
}