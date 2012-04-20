package video.search;

import video.main.CommonOperation;
import video.main.FeatureCode;
import video.values.SearchType;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.VideoView;

public class PrevVideoActivity extends Activity implements OnClickListener {
	private Uri videoFileUri=null;
	private VideoView videoView=null;
	private Button btnSearch=null;
	private Button btnCancel=null;
	private Button btnAdvance=null;
	private MediaController mediaController=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prevideo);
		videoFileUri=(Uri)getIntent().getParcelableExtra("videofile");
		videoView=(VideoView)findViewById(R.id.videoView1);
		videoView.setVideoURI(videoFileUri);
		mediaController=new MediaController(this);
		videoView.setMediaController(mediaController);
		btnSearch=(Button)findViewById(R.id.btnSearch);
		btnCancel=(Button)findViewById(R.id.btnCancel);
		btnAdvance=(Button)findViewById(R.id.btnAdvance);
		btnSearch.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		btnAdvance.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		
		Intent searchIntent=new Intent(PrevVideoActivity.this,ResultActivity.class);
		//放置视频搜索类型
		searchIntent.putExtra("type", SearchType.VIDEO);
		//放置视频数据
		searchIntent.putExtra("videofile", videoFileUri);
		//获取需要帧数
		Spinner spCutNum=(Spinner)findViewById(R.id.spCutNum);
		searchIntent.putExtra("cutNum",spCutNum.getSelectedItem().toString());
		switch (v.getId()) {
		case R.id.btnCancel:
			finish();
			break;
		case R.id.btnAdvance:
			CommonOperation.showAdvanceDialog(PrevVideoActivity.this,searchIntent);
			break;
		case R.id.btnSearch:
			//放置三参数
			stopPlayBack();
			searchIntent.putExtra("alpha", "0.5");
			searchIntent.putExtra("kind", "全部");
			searchIntent.putExtra("samedegree","0.0");
			startActivity(searchIntent);
			finish();
			break;
		default:
			break;
		}
	}
	public void stopPlayBack() {
		if (videoView.isPlaying())
		{
			videoView.stopPlayback();
		}
		
	}
	
}
