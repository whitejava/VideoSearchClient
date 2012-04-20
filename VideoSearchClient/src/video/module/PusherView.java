package video.module;

import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;



public class PusherView {
	public static View create(Activity context, PusherEntity[] pushers){
		LinearLayout a = new LinearLayout(context);
		a.setBackgroundColor(Color.WHITE);
		a.setOrientation(LinearLayout.VERTICAL);
		// 广告
		View advertisement = new View(context);
		//a.addView(advertisement);
		// 第一行
		LinearLayout line1 = new LinearLayout(context);
		line1.setOrientation(LinearLayout.HORIZONTAL);
		line1.setGravity(Gravity.CENTER_HORIZONTAL);
		for(int i=0;i<2;i++)
			line1.addView(createPusherButton(context, pushers[i]));
		// 第二行
		LinearLayout line2 = new LinearLayout(context);
		line2.setOrientation(LinearLayout.HORIZONTAL);
		line2.setGravity(Gravity.CENTER_HORIZONTAL);
		for(int i=2;i<4;i++)
			line2.addView(createPusherButton(context, pushers[i]));

		a.addView(line1);
		a.addView(line2);
		return a;
	}
	
	public static View createPusherButton(final Activity context, final PusherEntity pusher){
		FrameLayout a = new FrameLayout(context);
		a.setBackgroundColor(Color.rgb(200,200,200));
		a.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
		a.setPadding(1, 1, 1, 1);
		FrameLayout b = new FrameLayout(context);
		b.setBackgroundColor(Color.WHITE);
		b.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		ImageView c = new ImageView(context);
		c.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pusher.weblink));
				context.startActivity(browserIntent);
			}});
		loadImageOnOtherThread(context, c, pusher.imageURL);
		c.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		b.addView(c);
		a.addView(b);
		return a;
	}
	
	private static void loadImageOnOtherThread(final Activity context, final ImageView button, final String imageUrl){
		LoadingThread.run(new Runnable(){
			@Override
			public void run() {
				InputStream in;
				try {
					in = new java.net.URL(imageUrl).openStream(); 
					Bitmap image = BitmapFactory.decodeStream(new SanInputStream(in));
					final Drawable a = new BitmapDrawable(image);
					context.runOnUiThread(new Runnable(){
						@Override
						public void run() {
							button.setImageDrawable(a);
							button.postInvalidateDelayed(100);
						}});	
				} catch(Exception exception){
					//throw new RuntimeException(exception);
				}
			}});
	}
	
	public static PusherEntity[] createSamples()
	{
		PusherEntity[] pEntities=new PusherEntity[8];
		for(int i=0;i<8;i++)
		{
			pEntities[i]=new PusherEntity();
		}
		pEntities[0].imageURL="http://q.i04.wimg.taobao.com/bao/uploaded/i1/T1hgK0XdhoXXaMLPoU_015311.jpg_b.jpg";
		pEntities[0].weblink="http://a.m.tmall.com/i9989681564.htm?sid=5dc57e3a9d95e559&spm=41.221635.238071.2";
		pEntities[1].imageURL="http://q.i03.wimg.taobao.com/bao/uploaded/i3/T1DFawXcXeXXbIOLHX_114854.jpg_b.jpg";
		pEntities[1].weblink="http://a.m.tmall.com/i12324613762.htm?sid=5dc57e3a9d95e559";
		pEntities[2].imageURL="http://q.i01.wimg.taobao.com/bao/uploaded/i2/T1ajGIXahuXXcqSJM6_061305.jpg_b.jpg";
		pEntities[2].weblink="http://a.m.tmall.com/i8811477443.htm?ali_refid=a3_420550_1006:1103115805:6::da5a57c8382e8633a908659ca9b39877&ali_trackid=1_da5a57c8382e8633a908659ca9b39877&sid=5dc57e3a9d95e559";
		pEntities[3].imageURL="http://q.i03.wimg.taobao.com/bao/uploaded/i3/T1DP9cXadbXXXDIsDb_123343.jpg_b.jpg";
		pEntities[3].weblink="http://a.m.tmall.com/i10482240370.htm?sid=5dc57e3a9d95e559";
		pEntities[4].imageURL="http://q.i04.wimg.taobao.com/bao/uploaded/i1/T1GiK3XnVoXXXzeSg._113348.jpg_b.jpg";
		pEntities[4].weblink="http://a.m.tmall.com/i15050812674.htm?sid=5dc57e3a9d95e559";
		pEntities[5].imageURL="http://q.i04.wimg.taobao.com/bao/uploaded/i4/T1qZuyXXtiXXalxZ.T_011255.jpg_b.jpg";
		pEntities[5].weblink="http://a.m.tmall.com/i4231815427.htm?sid=5dc57e3a9d95e559";
		pEntities[6].imageURL="http://q.i04.wimg.taobao.com/bao/uploaded/i2/T18cWMXmJlXXcjmEM9_105112.jpg_b.jpg";
		pEntities[6].weblink="http://a.m.tmall.com/i15176436709.htm?sid=5dc57e3a9d95e559";
		pEntities[7].imageURL="http://q.i04.wimg.taobao.com/bao/uploaded/i8/T1lHeEXcBkXXXYmYPb_122940.jpg_b.jpg";
		pEntities[7].weblink="http://a.m.tmall.com/i8812238503.htm?ali_refid=a3_420550_1006:1103115805:6::f066b45811380bf000282401f969b234&ali_trackid=1_f066b45811380bf000282401f969b234&sid=5dc57e3a9d95e559";
		
		
		return pEntities;
	}
}
