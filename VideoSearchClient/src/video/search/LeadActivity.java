package video.search;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.ksoap2.serialization.SoapObject;

import video.ad.AdBanner;
import video.main.CommonOperation;
import video.values.Const;
import video.values.HanderMessage;
import video.values.SearchType;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.AlertDialog.Builder;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewDebug.FlagToString;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class LeadActivity extends Activity implements OnClickListener {

	private static final int RECODE_TAKEPHOTO = 0x001;
	private static final int RECODE_CHOOSEPHOTO = 0x002;
	private static final int RECODE_TAKEVIDEO = 0x003;
	private static final int RECODE_CHOOSEVIDEO = 0x004;
	private static final int NOYIFICATION_ID = 0x1123;
	private Button btnPhoto = null;
	private Button btnVideo = null;
	private Button btnCPhoto = null;
	private Button btnCVideo = null;
	private Button btnKeyWords = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logo);
		(new loadMain()).start();
	}
	
	private void showAd(){
		LinearLayout ll = (LinearLayout)findViewById(R.id.lllogo);
		AdBanner.create(this, ll);
	}

	private void init() {
		showAd();
		btnPhoto = (Button) findViewById(R.id.btnsphoto);
		btnVideo = (Button) findViewById(R.id.btnsvideo);
		btnCPhoto = (Button) findViewById(R.id.btnscphoto);
		btnCVideo = (Button) findViewById(R.id.btnscvideo);
		btnKeyWords = (Button) findViewById(R.id.btnskeywords);

		btnPhoto.setOnClickListener(this);
		btnVideo.setOnClickListener(this);
		btnCPhoto.setOnClickListener(this);
		btnCVideo.setOnClickListener(this);
		btnKeyWords.setOnClickListener(this);
		
		SharedPreferences preferences;
		SharedPreferences.Editor editor;
		preferences=getSharedPreferences("coolsou", MODE_PRIVATE);
		editor=preferences.edit();
		//如果是首次运行本程序
		if(preferences.getBoolean("isFirstTime", true)==true)
		{
			//只要任何一个目录创建失败就自动退出
			if(!CommonOperation.createPath(Const.APP_DIR) || !CommonOperation.createPath(Const.APP_DIR_PHOTO) 
				|| !CommonOperation.createPath(Const.APP_DIR_TEMP)	) 
			{
				Toast.makeText(this, "非常抱歉，创建程序所需目录失败，请检查程序是否具有权限，程序将自动退出。", Toast.LENGTH_LONG).show();
				this.finish();
				return;
			}
			else
			{
				//设置为非第一次运行
				setNotify();
				Toast.makeText(this, "欢迎首次使用酷搜，希望您搜得愉快。", Toast.LENGTH_SHORT).show();
				editor.putBoolean("isFirstTime", false);
				editor.commit();
			}
		}
		else {
			Toast.makeText(this, "欢迎回来，酷搜为您提供便捷搜索服务。",1000).show();
		}
		
	}
	private void setNotify() {
		Intent intent =new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.coolsou.com"));
		PendingIntent pi=PendingIntent.getActivity(LeadActivity.this, 0, intent, 0);
		Notification notify=new Notification();
		notify.icon=R.drawable.notify;
		notify.tickerText="欢迎首次使用酷搜，请访问我们的主页获取帮助信息。";
		notify.when=System.currentTimeMillis();
		notify.defaults=Notification.DEFAULT_SOUND;
		notify.flags= Notification.FLAG_AUTO_CANCEL;
		notify.setLatestEventInfo(LeadActivity.this, "酷搜", "点击访问我们的主页获取帮助信息。", pi);
		NotificationManager notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		notificationManager.notify(NOYIFICATION_ID,notify);
	}
	// 处理线程消息的Handler
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 收到线程载入主界面的消息
			case HanderMessage.LOADLAYOUT:
				setContentView(R.layout.lead);
				init();
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}

	};

	@Override
	public void onBackPressed() {
		showExitDialog();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Uri mMediaUri;
		 if(data==null)
		 {
			 showCancel();
      				  return;
		 }
		switch (requestCode) {
		case RECODE_TAKEPHOTO:
			startPhotoFixer(analyseData(data));
			break;
		case RECODE_CHOOSEPHOTO:
			mMediaUri=data.getData();   
	            Cursor cursor = getContentResolver().query(mMediaUri, null, null, null, null);   
	            cursor.moveToFirst();   
	            String mediaFilePath = cursor.getString(1);
	            
	            Bitmap photo = BitmapFactory.decodeFile(mediaFilePath);
	            Display currentDisplay=getWindowManager().getDefaultDisplay();
	            float rate= (((float)photo.getWidth())/photo.getHeight());
	            Bitmap photoScaled=ThumbnailUtils.extractThumbnail(photo, currentDisplay.getWidth(), (int) (currentDisplay.getWidth()/rate));
			startPhotoFixer(photoScaled);
			break;
		case RECODE_TAKEVIDEO :
			if(resultCode!= RESULT_OK)
				break;
			Uri videoFileUri = data.getData();
			if(videoFileUri==null)
			{
				showCancel();
	      				  return;
			}
			Intent intent=new Intent(LeadActivity.this,PrevVideoActivity.class);
			intent.putExtra("videofile", videoFileUri);
			startActivity(intent);
			break;
		case RECODE_CHOOSEVIDEO:
			mMediaUri=data.getData();   
	        Cursor cursor1 = getContentResolver().query(mMediaUri, null, null, null, null);   
	        cursor1.moveToFirst();   
	        File mediaFilePath1 =new File( cursor1.getString(1));
	            Uri videofile= Uri.fromFile(mediaFilePath1);
	           	 if (videofile== null){
	            	showCancel();
	      				  return;
	            }
	           	Intent intent1=new Intent(LeadActivity.this,PrevVideoActivity.class);
	 			intent1.putExtra("videofile", videofile);
				startActivity(intent1);
			break;
		}
		
		
	}
	private void showCancel()
	{
		Toast.makeText(LeadActivity.this,
				   "搜索取消。", Toast.LENGTH_SHORT).show();
	}
	private Bitmap analyseData(Intent data) {
		 if (data==null)
		  {   
			 Toast.makeText(LeadActivity.this,
					   "搜索取消。", Toast.LENGTH_LONG).show();
			 return null;
		 }
			Uri uri = data.getData();
			Bitmap photo = null;
			if (uri != null) {
				photo = BitmapFactory.decodeFile(uri.getPath());
			}
			if (photo == null) {
				Bundle bundle = data.getExtras();
				if (bundle != null) {
					photo = (Bitmap) bundle.get("data");
				} else {
					Toast.makeText(LeadActivity.this,
					   "搜索取消。", Toast.LENGTH_LONG).show();
					  return  null;
				}
			}
			return photo;
	}
  private void startPhotoFixer(Bitmap photo) {
	   if(photo==null || photo.getWidth()==-1)
	   {
		   Toast.makeText(LeadActivity.this,
				   "搜索取消。", Toast.LENGTH_LONG).show();
		   return ;
	   }
		Intent intent = new Intent(LeadActivity.this,
				FixPhotoActivity.class);
		//intent.putExtra("bitmap", photo);
		intent.putExtra("bitmap", CommonOperation.bitmapToBytes(photo));
		startActivity(intent);
}


	// 读入主界面的线程
	private class loadMain extends Thread {
		@Override
		public void run() {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Message msg = new Message();
			msg.what = HanderMessage.LOADLAYOUT;
			mHandler.sendMessage(msg);
			super.run();
		}

	}

	// 菜单功能实现
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuLogin:
			Intent intent =new Intent(LeadActivity.this,LoginActivity.class);
			startActivity(intent);
			break;

		case R.id.menuexit:
			showExitDialog();
			break;
		}
		return true;
	}
	private void showExitDialog()
	{
		final Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("确认");
		builder.setMessage("确定退出酷搜？");
		builder.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						LeadActivity.this.finish();
					}
				});
		builder.setNegativeButton("取消", null);
		builder.create().show();
	}
	// 创建菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.searchmenu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		// 按下关键字搜索按钮
		case R.id.btnskeywords:
			String keyWords = ((EditText) findViewById(R.id.edtKeywords))
					.getText().toString();
			// 启动搜索结果界面
			Intent skintent = new Intent(LeadActivity.this,
					ResultActivity.class);
			skintent.putExtra("KeyWords", keyWords);
			skintent.putExtra("type", SearchType.KEYWORDS);
			startActivity(skintent);
			break;
		//拍照搜索
		case R.id.btnsphoto:
			Intent tpintent = new Intent();
			tpintent.setAction("android.media.action.IMAGE_CAPTURE");
			startActivityForResult(tpintent, RECODE_TAKEPHOTO);
		   break;
		case R.id.btnscphoto:
			Intent cpintent=new Intent();
			cpintent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*" );   
			cpintent.setAction(Intent.ACTION_PICK); 
			startActivityForResult(cpintent, RECODE_CHOOSEPHOTO);
			break;
		//录像搜索
		case R.id.btnsvideo:
			Intent tvIntent=new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
			startActivityForResult(tvIntent, RECODE_TAKEVIDEO);
			break;
		case R.id.btnscvideo:
			Intent cvintent=new Intent();
			cvintent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "video/*" );   
			cvintent.setAction(Intent.ACTION_PICK); 
			startActivityForResult(cvintent, RECODE_CHOOSEVIDEO);
			break;
			
		}
	}
}
