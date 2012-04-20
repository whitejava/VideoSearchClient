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

interface OnPageTurnListener{
	public void onPageTurn(int page);
}

class PageEvent implements OnScrollListener {
	private OnPageTurnListener listener;
	private int itemCountPerPage;
	private int currentPage;
	
	public PageEvent(OnPageTurnListener listener, int itemCountPerPage){
		this.listener = listener;
		this.itemCountPerPage = itemCountPerPage;
	}
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		int page = firstVisibleItem / itemCountPerPage;
		if(currentPage != page){
			currentPage = page;
			listener.onPageTurn(page);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}
}

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

public class ResultActivity extends Activity {
	// ��ʾ�����¼����textview
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
	// ��ʾ�������Ի���
	private ProgressDialog progressDialog = null;

	// �����Ʒ
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
			text.setText(String.format("%d/%dҳ", page, pageCount));
			autoHide.activate();
		}
	}
	
	private void showResultCount(){
		resultCount.setText("�����������Ʒ " + String.valueOf(content.length)+ " ��");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);
		resultCount = (TextView) findViewById(R.id.tvShower);
		Intent intent = getIntent();
		int type = intent.getIntExtra("type", 0);

		// ��������������
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
			// ��ʼ�����߳�
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

	// ��ȡ��Ϣ�ľ��
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// ��ʾ���
			String hint="";
			switch (msg.what) {
			// �����޽��
			case HanderMessage.NORES:
				hint= "������˼��û���Ŷ���ס�";
				Toast.makeText(ResultActivity.this,hint , Toast.LENGTH_SHORT).show();
				finish();
				return;
			// ������������ʾ������Ϣ
			case HanderMessage.ERROR:
				hint= "�ף�ò�Ƴ��������ˡ����������԰ɣ�Ҳ�������ˡ�";
				Toast.makeText(ResultActivity.this,hint , Toast.LENGTH_LONG).show();
				ResultActivity.this.finish();
				return;
			case HanderMessage.UPDATE:
				progressDialog.setMessage("�ѳɹ�����"+ " " +msg.getData().getInt("progress")+" %" );
				progressDialog.setProgress(msg.getData().getInt("progress"));
				return;
			// ��ʼ����
			case HanderMessage.STARTANY:
				progressDialog.setMessage("��ʼ������Ƶ��");
				return;
			// �������
			case HanderMessage.FINISHANY:
				progressDialog.setMessage("������ɣ����ڼ�����");
				progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				return;
			// ��ʾ���������
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
		toast("�������," + "�����������Ʒ " + String.valueOf(content.length)+ " ����");
	}
	
	private void toast(String message){
		Toast.makeText(ResultActivity.this, message, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		//��ʼ��������
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("������ʾ");
		progressDialog.setMessage("�ף����Եȣ����Ͼͺá���");
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
		progressDialog.setButton("ȡ��", new DialogInterface.OnClickListener() {
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

	// �����߳�
	private void startSearchByKeyWords(final String keyWords) {
		// ���δ����ؼ���
		if (keyWords.trim() == "") {
			Toast.makeText(ResultActivity.this, "�ؼ��ֲ���Ϊ�ա�", Toast.LENGTH_SHORT)
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
	// ����ѡ��˵�
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.searchmenu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	// ���������Ĳ˵�
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.gooditem, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	// ���������Ĳ˵��¼�
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
							   "����Ʒ��������ͼ���޷�������", Toast.LENGTH_LONG).show();
				}
			}
		default:
			break;
		}
		return false;
	}

	// ����ѡ��˵��¼�
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