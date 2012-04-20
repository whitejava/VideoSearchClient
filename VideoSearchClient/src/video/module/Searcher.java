package video.module;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import video.protocol.Engine;
import video.protocol.Good;
import video.search.ResultActivity;
import video.values.HanderMessage;

public class Searcher  {
	private Handler mHandler;
	private ResultActivity parent;
	
	public Searcher(Handler mHandler, ResultActivity parent) {
		this.mHandler = mHandler;
		this.parent = parent;
	}
	
	//调用Shower显示结果
	private void show(Good[] goods) {
		Message message = new Message();
		mHandler.sendMessage(message);
		Shower shower = new Shower(parent, mHandler);
		shower.ShowResult(goods);
	}

	public void SearchByKeyWords(String keyWords) 
	{
		try
		{
			Engine engine=new Engine();
			Good[] goods=engine.SearchByKeyWords(keyWords, "00");
		    show(goods);
		}
		catch (Exception e) {
			Log.e("错误",e.getMessage());
			mHandler.sendEmptyMessage(HanderMessage.ERROR);
		}
	}

	public void SearchByFeature(String features,String alpha,String samedegree) {
		try{
			Engine engine=new Engine();
			Good[] goods=engine.SearchByKeyFeatures(features, "", alpha, samedegree);
			show(goods);
		}
		catch (Exception e) {
			mHandler.sendEmptyMessage(HanderMessage.ERROR);
		}
	}
}
