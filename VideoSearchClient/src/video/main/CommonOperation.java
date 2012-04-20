package video.main;

import java.io.ByteArrayOutputStream;
import java.io.File;
import video.search.PrevVideoActivity;
import video.search.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Spinner;

public class CommonOperation {
	
	public static Boolean createPath(String path) {
	     File file = new File(path);
	     if(file.exists())
	     {
	    	 return true;
	     }
	     else
	     {
	          if(file.mkdir())
	        	  {
	        	  	return true;
	        	  }
	     }
	     return false;
	 }
	
	public static void showAdvanceDialog(final Context context, final Intent intent){
		View saveDialog=((Activity) context).getLayoutInflater().inflate(R.layout.searchdetail, null);
		final Spinner spAlpha= (Spinner)saveDialog.findViewById(R.id.spAlpha);
		final Spinner spSameDegree=(Spinner)saveDialog.findViewById(R.id.spSameDegree);
		final Spinner spKind=(Spinner)saveDialog.findViewById(R.id.spKind);
		new AlertDialog.Builder(context).setView(saveDialog).setPositiveButton("搜索", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				String alpha=spAlpha.getSelectedItem().toString();
				String kind=spKind.getSelectedItem().toString();
				String sameDegree=spSameDegree.getSelectedItem().toString();
				//放入三个参数
				intent.putExtra("alpha", alpha);
				intent.putExtra("kind", kind);
				intent.putExtra("samedegree",sameDegree);
				//启动搜索结果活动		
				context.startActivity(intent);
				if(context.getClass()==PrevVideoActivity.class)
				{
					((PrevVideoActivity)context).stopPlayBack();
				}
				((Activity) context).finish();
				

			}
		}).setNegativeButton("取消", null).show();
		
	}
	
	public static byte[] bitmapToBytes(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
		return os.toByteArray();
		//return bitmap
	}
}
