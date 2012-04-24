package video.search;

import java.io.File;

import video.main.*;
import video.module.GoodAdapter;
import video.module.Searcher;
import video.protocol.Good;
import video.values.Const;
import video.values.HanderMessage;
import video.values.SearchType;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends Activity {
	// 显示结果记录数的textview
	private TextView resultCount;
	private static final int CAPTURE_VIDEO_REQUEST = 1338;
	private final int REQUEST_MapPos = 1212;

	private static final String VIDEO_URL = "http://192.168.40.1/Videos/";
	private static final int NOPROGRESSS_DIALOG = 0;
	private static final int PROGRESS_DIALOG = 1;
	private static final int ITEM_COUNT_PER_PAGE = 10;
	private static final int PAGE_NUMBER_VISIBLE_DURATION = 2000;

	private Thread searchThread = null;
	public static int position = 0;
	// 显示进度条对话框
	private ProgressDialog progressDialog = null;

	// 结果商品
	public Good[] content = null;
	private int pageCount;
	static String featureString = "";
	
	public void setContent(Good[] a){
		this.content = a;
	}
	
	private void showResult() {
		pageCount = calculatePageCount(content.length);
		ListView resultList = (ListView) findViewById(R.id.lvResult);
		resultList.setOnScrollListener(new PageEvent(new ShowPageNumber(), ITEM_COUNT_PER_PAGE));
		GoodAdapter goodAdapter = new GoodAdapter(ResultActivity.this, content);
		resultList.setAdapter(goodAdapter);
	}
	
	private int calculatePageCount(int itemCount){
		if(itemCount % ITEM_COUNT_PER_PAGE == 0){
			return itemCount / ITEM_COUNT_PER_PAGE;
		} else {
			return itemCount / ITEM_COUNT_PER_PAGE + 1;
		}
	}
	
	private class ShowPageNumber implements OnPageTurnListener {
		private AutoHide autoHide;
		private TextView text;
		
		public ShowPageNumber(){
			text = (TextView)findViewById(R.id.tvPageNumber);
			autoHide = new AutoHide(text, PAGE_NUMBER_VISIBLE_DURATION);
		}
		
		@Override
		public void onPageTurn(int page) {
			text.setText(String.format("%d/%d页", page, pageCount));
			autoHide.activate();
		}
	}
	
	private void showResultCount(){
		resultCount.setText("搜索到相关商品 " + String.valueOf(content.length)+ " 件");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);
		resultCount = (TextView) findViewById(R.id.tvShower);
		Intent intent = getIntent();
		int type = intent.getIntExtra("type", 0);

		// 若果不进行搜索
		if (type == 0)
		{
			ResultActivity.this.finish();
			return;
		}
		String alpha = intent.getStringExtra("alpha");
		String samedegree = intent.getStringExtra("samedegree");
		String kind=intent.getStringExtra("kind");
		switch (type) {
		case SearchType.KEYWORDS:
			String keyWords = intent.getStringExtra("KeyWords");
			// 开始搜索线程
			if (keyWords != null) {
				if (keyWords != "") {
					startSearchByKeyWords(keyWords);
					return;
				}
			}
			break;
		case SearchType.PHOTO:
			
			startSearchByPhoto(intent.getByteArrayExtra("photo"),alpha,samedegree,kind);
			break;
		case SearchType.VIDEO:
			int cutNum=Integer.parseInt(intent.getStringExtra("cutNum"));
			startSearchByVideo((Uri)intent.getParcelableExtra("videofile"),cutNum,alpha,samedegree,kind);
			break;
		}

	}

	// 获取消息的句柄
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 显示结果
			String hint="";
			switch (msg.what) {
			// 搜索无结果
			case HanderMessage.NORES:
				hint= "不好意思，没结果哦，亲。";
				Toast.makeText(ResultActivity.this,hint , Toast.LENGTH_SHORT).show();
				finish();
				return;
			// 搜索出错，显示错误信息
			case HanderMessage.ERROR:
				hint= "亲，貌似出现问题了。晚点再试试吧，也许就行了。";
				Toast.makeText(ResultActivity.this,hint , Toast.LENGTH_LONG).show();
				ResultActivity.this.finish();
				return;
			case HanderMessage.UPDATE:
				progressDialog.setMessage("已成功解析"+ " " +msg.getData().getInt("progress")+" %" );
				progressDialog.setProgress(msg.getData().getInt("progress"));
				return;
			// 开始解析
			case HanderMessage.STARTANY:
				progressDialog.setMessage("开始解析视频。");
				return;
			// 解析完成
			case HanderMessage.FINISHANY:
				progressDialog.setMessage("解析完成，正在检索。");
				progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				return;
			// 显示结果的请求
			case HanderMessage.SHOWRES:
				toastSuccess();
				showResultCount();
				showResult();
				break;
			default:
				return;
			}
			if(progressDialog.isShowing())
			{
				progressDialog.dismiss();
			}
			Toast.makeText(ResultActivity.this,hint , Toast.LENGTH_SHORT).show();
		}
	};
	
	private void toastSuccess(){
		toast("搜索完成," + "搜索到相关商品 " + String.valueOf(content.length)+ " 件。");
	}
	
	private void toast(String message){
		Toast.makeText(ResultActivity.this, message, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		//初始化进度条
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("酷搜提示");
		progressDialog.setMessage("亲，请稍等，马上就好……");
		progressDialog.setCancelable(true);
		switch (id) {
		case NOPROGRESSS_DIALOG:
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			break;
		case PROGRESS_DIALOG:
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		default:
			break;
		}
		progressDialog.setIndeterminate(false);
		progressDialog.setButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (searchThread != null) {
					searchThread.stop();
					searchThread.interrupt();
				}
				ResultActivity.this.finish();
			}
		});
		return progressDialog;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
		// TODO Auto-generated method stub
		super.onPrepareDialog(id, dialog, args);
	}

	// 搜索线程
	private void startSearchByKeyWords(final String keyWords) {
		// 如果未输入关键字
		if (keyWords.trim() == "") {
			Toast.makeText(ResultActivity.this, "关键字不能为空。", Toast.LENGTH_SHORT)
					.show();
			ResultActivity.this.finish();
			return;
		}

		searchThread = new Thread() {
			@Override
			public void run() {
				new Searcher(mHandler, ResultActivity.this)
						.SearchByKeyWords(keyWords);
				super.run();
			}
		};
		showDialog(NOPROGRESSS_DIALOG);
		searchThread.start();
	}

	private void startSearchByPhoto(final byte[] photo,final String alpha,final String samedegree,final String kind) {
		showDialog(NOPROGRESSS_DIALOG);
		
		searchThread = new Thread() {
			@Override
			public void run() {
				new Searcher(mHandler, ResultActivity.this).SearchByFeature(
						FeatureCode.calculateImageFeatureCode(BitmapFactory.decodeByteArray(photo, 0, photo.length)),alpha,samedegree);
				super.run();
			}
		};
		searchThread.start();
	}
	private void startSearchByVideo(final Uri videoUri,final int cutNum,final String alpha,final String samedegree,final String kind) {
		showDialog(PROGRESS_DIALOG);
		
		searchThread = new Thread() {
			@Override
			public void run() {
				mHandler.sendEmptyMessage(HanderMessage.STARTANY);
				String feature=FeatureCode.calculateVideoFeatureCode(ResultActivity.this, mHandler, videoUri, cutNum);
				mHandler.sendEmptyMessage(HanderMessage.FINISHANY);
				new Searcher(mHandler, ResultActivity.this).SearchByFeature(feature,alpha,samedegree);
				super.run();
			}
		};
		searchThread.start();
	}
	// 创建选项菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.searchmenu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	// 创建上下文菜单
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.gooditem, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	// 监听上下文菜单事件
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		ListView lvListView = (ListView) findViewById(R.id.lvResult);

		GoodAdapter gAdapter = (GoodAdapter) lvListView.getAdapter();
		Good gd = (Good) gAdapter.getItem(position);
		switch (item.getItemId()) {
		case R.id.menuVideo:

			Intent intent = new Intent(ResultActivity.this,
					VideoPlayerActivity.class);

			intent.putExtra("name", gd.getName());
			intent.putExtra("url", ResultActivity.VIDEO_URL + gd.getId()
					+ ".3gp");
			startActivity(intent);
			break;
		case R.id.menuPosGood:
			Intent mapintent = new Intent(ResultActivity.this,
					MapPosActivity.class);
			mapintent.putExtra("pos", gd.getExactPosition());
			startActivityForResult(mapintent, REQUEST_MapPos);
			break;
		case R.id.menuDetail:
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(gd
					.getUrl()));
			startActivity(browserIntent);
			break;
		case R.id.menuAgain:
			File file=new File(Const.APP_DIR_TEMP+String.valueOf(gd.getId())+".jpg" );
			if(file.exists())
				{
				Intent intent2 = new Intent(ResultActivity.this,
						FixPhotoActivity.class);
				//intent.putExtra("bitmap", photo);
				intent2.putExtra("bitmap", CommonOperation.bitmapToBytes(BitmapFactory.decodeFile(Const.APP_DIR_TEMP+String.valueOf(gd.getId())+".jpg")));
				startActivity(intent2);
				finish();
				}
			else {
				{
					 Toast.makeText(ResultActivity.this,
							   "该商品暂无缩略图，无法搜索。", Toast.LENGTH_LONG).show();
				}
			}
		default:
			break;
		}
		return false;
	}

	// 监听选项菜单事件
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuLogin:
			break;

		case R.id.menuexit:
			finish();
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

}
