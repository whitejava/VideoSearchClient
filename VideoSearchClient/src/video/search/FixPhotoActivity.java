package video.search;

import java.io.File;
import java.io.FileOutputStream;

import video.main.CommonOperation;
import video.main.FeatureCode;
import video.values.Const;
import video.values.SearchType;
import video.view.PhotoFixer;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class FixPhotoActivity extends Activity implements OnClickListener {
	PhotoFixer photoFixer=null;
	private Button btnSelectAll=null;
	private Button btnTurnLeft=null;
	private Button btnTurnRight=null;
	private Button btnSave=null;
	private Button btnCancel=null;
	private Button btnSearch=null;
	private Button btnAdvance=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fixphoto);
		LinearLayout ll= (LinearLayout)findViewById(R.id.fixerlayout);
		//得到照片
		Bitmap photo=BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("bitmap"), 0, getIntent().getByteArrayExtra("bitmap").length);
		if(photo==null)
		{
			finish();
		}
		photoFixer=new PhotoFixer(this,photo);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		photoFixer.setLayoutParams(params);
		ll.addView(photoFixer);
		
		photo=null;
		//找到按钮
		btnSelectAll=(Button)findViewById(R.id.btnSelectAll);
		btnTurnLeft=(Button)findViewById(R.id.btnTurnLeft);
		btnTurnRight=(Button)findViewById(R.id.btnTurnRight);
		btnSave=(Button)findViewById(R.id.btnSave);
		btnCancel=(Button)findViewById(R.id.btnCancel);
		btnSearch=(Button)findViewById(R.id.btnSearch);
		btnAdvance=(Button)findViewById(R.id.btnAdvance);
		//设置事件
		btnSelectAll.setOnClickListener(this);
		btnTurnLeft.setOnClickListener(this);
		btnTurnRight.setOnClickListener(this);
		btnSave.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		btnSearch.setOnClickListener(this);
		btnAdvance.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		Intent searchIntent=new Intent(FixPhotoActivity.this,ResultActivity.class);
		//放置类型
		searchIntent.putExtra("type", SearchType.PHOTO);
		//放置图片
		byte[] photo =CommonOperation.bitmapToBytes( photoFixer.getFixedBitmap());
		searchIntent.putExtra("photo", photo);
		
		switch (v.getId()) {
		case R.id.btnSelectAll:
			photoFixer.selectAll();
			break;
		case R.id.btnCancel:
			finish();
			break;
		case R.id.btnTurnLeft:
			photoFixer.turnLeft();
			break;
		case R.id.btnTurnRight:
			photoFixer.turnRight();
			break;
		case R.id.btnSave:
			showSaveDialog();
			break;
		case R.id.btnSearch:
			//放置三参数
			searchIntent.putExtra("alpha", "0.5");
			searchIntent.putExtra("kind", "全部");
			searchIntent.putExtra("samedegree","0.0");
			startActivity(searchIntent);
			finish();
			break;
		case R.id.btnAdvance:
			CommonOperation.showAdvanceDialog(FixPhotoActivity.this,searchIntent);		
		default:
			break;
		}
	}
	private void showSaveDialog() {
		final Bitmap bm=photoFixer.getFixedBitmap();
		View saveDialog=getLayoutInflater().inflate(R.layout.savephoto, null);
		final EditText photoNameEditText=(EditText)saveDialog.findViewById(R.id.editText1);
		ImageView show=(ImageView)saveDialog.findViewById(R.id.imageView1);
		TextView tView=(TextView)saveDialog.findViewById(R.id.textView2);
		tView.setText("图片大小: "+bm.getWidth()+"(宽) * "+bm.getHeight()+"(高)");
		show.setImageBitmap(bm);
		Time time=new Time();
		time.setToNow();
		photoNameEditText.setText(String.valueOf(time.toMillis(false)));
		new AlertDialog.Builder(FixPhotoActivity.this).setView(saveDialog).setPositiveButton("保存", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				File file=new File(Const.APP_DIR_PHOTO,photoNameEditText.getText().toString()+".jpg");
				FileOutputStream outputStream=null;
				try{
					outputStream=new FileOutputStream(file);
					bm.compress(CompressFormat.JPEG, 100, outputStream);
					Toast.makeText(FixPhotoActivity.this, "成功保存文件到 " +file.getPath(), 3000).show();
					outputStream.close();
				}
				catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(FixPhotoActivity.this, "保存文件失败，请检查权限。"+e.getMessage(), 3000).show();
				}
			}
		}).setNegativeButton("取消", null).show();
	}
	
	
	
}
