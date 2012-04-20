package video.module;

import android.os.Handler;
import video.protocol.Good;
import video.search.ResultActivity;
import video.values.HanderMessage;

public class Shower {
	private ResultActivity resultActivity;
	private Handler mhandler = null;
	
	public Shower(ResultActivity parent, Handler mHandler) {
		this.resultActivity = parent;
		this.mhandler = mHandler;
	}
	
	public void ShowResult(Good[] goods) {
		if(goods == null || goods.length == 0){
			noResult();
		} else {
			showResult(goods);
		}
	}
	
	private void noResult(){
		mhandler.sendEmptyMessage(HanderMessage.NORES);
	}
	
	private void showResult(Good[] goods){
		resultActivity.setContent(goods);
		mhandler.sendEmptyMessage(HanderMessage.SHOWRES);
	}
}