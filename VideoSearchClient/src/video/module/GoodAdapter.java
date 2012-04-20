package video.module;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.Templates;

import video.protocol.Good;
import video.search.R;
import video.search.ResultActivity;
import video.values.Const;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GoodAdapter extends BaseAdapter {
	private Good[] goods=null;
	private Context context;
	private int positon=0;
	//属性
	public int getPositon() {
		return positon;
	}
	public void setPositon(int positon) {
		this.positon = positon;
	}
	
	public GoodAdapter(Context context,Good[] goods)
	{
		this.context=context;
		this.goods=goods;
	}
	public void removeAll()
	{
		goods=null;
		notifyDataSetChanged();
	}
	public Good[] getGoods() {
		return goods;
	}

	public void setGoods(Good[] goods) {
		this.goods = goods;
	}

	public Context getParent() {
		return context;
	}

	public void setParent(Context parent) {
		this.context = parent;
	}

	@Override
	public int getCount() {
		return goods.length;
	}

	@Override
	public Object getItem(int position) {
		return goods[position];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int positon, View convertView, ViewGroup parent) {
		
		String inflaterString=Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(inflaterString);
		//得到Item的线性布局
		RelativeLayout relativeLayout=null;
		relativeLayout=(RelativeLayout)layoutInflater.inflate(R.layout.itemview, null);
		//设置图片
		ImageView imageView=(ImageView)relativeLayout.findViewById(R.id.imgItemPic);
		String filePath=Const.APP_DIR_TEMP+String.valueOf(goods[positon].getId())+".jpg";
		File file=new File(filePath);
		if( (file.exists()) )
		{
			imageView.setImageBitmap(BitmapFactory.decodeFile(filePath));
		}
		else {
			loadImageOnLoadingThread(imageView,goods[positon].getFullUrl(),file );
		}
		
		//设置名称文本
		TextView tvName=(TextView)relativeLayout.findViewById(R.id.tvName);
		tvName.setText(goods[positon].getName());
		//设置位置
		TextView tvPosition=(TextView)relativeLayout.findViewById(R.id.tvPosition);
		tvPosition.setText(goods[positon].getPosition());
		//设置价格
		TextView tvPrice=(TextView)relativeLayout.findViewById(R.id.tvPrice);
		tvPrice.setText(String.valueOf(goods[positon].getPrice()));
		((Activity)context).registerForContextMenu(relativeLayout);
		relativeLayout.setLongClickable(true);
		relativeLayout.setTag(positon);
		relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
		
			@Override
			public boolean onLongClick(View v) {
				int i= (Integer) v.getTag();
				Log.e("错误", String.valueOf(i));
				ResultActivity.position= (Integer) v.getTag();
				return false;
			}
		});
		return relativeLayout;
	}
	private static void loadImageOnLoadingThread(final ImageView view, final String url,final File file){
		LoadingThread.run(new Runnable(){
			@Override
			public void run() {
				try{
					InputStream in = new java.net.URL(url).openStream();
					Bitmap image = BitmapFactory.decodeStream(new SanInputStream(in));
					FileOutputStream outputStream=new FileOutputStream(file);
					image.compress(CompressFormat.JPEG, 100, outputStream);
					outputStream.close();
					((ImageView)view).setImageBitmap(image);
					view.postInvalidate();
				}
				catch(Exception e){
				}
			}});
	}
}
