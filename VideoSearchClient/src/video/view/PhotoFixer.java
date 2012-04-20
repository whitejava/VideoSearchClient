package video.view;

import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.widget.ImageView;

public class PhotoFixer extends ImageView {
	//最终的位置
	float endX=0;
	float endY=0;
	//按下的位置
	float startX=0;
	float startY=0;
	//
	float nowX=0;
	float nowY=0;
	int photoWidth, photoHeight;
	private Bitmap cacheBitmap;
	private Paint paint;
	public PhotoFixer(Context context,Bitmap photo) {
		super(context);
		//取得图片
		cacheBitmap=photo;
		photoWidth=cacheBitmap.getWidth();
		photoHeight=cacheBitmap.getHeight();
		//rotate(90);
		//设置画笔风格
		paint=new Paint(Paint.DITHER_FLAG);
		paint.setColor(Color.BLUE);
		paint.setStyle(Paint.Style.STROKE);
		Path p= new Path();
		p.addRect(0, 0, 2, 2, Path.Direction.CW);
		paint.setPathEffect(new PathDashPathEffect(p, 3, 2, PathDashPathEffect.Style.ROTATE));
		//反锯齿
		paint.setAntiAlias(true);
		paint.setDither(true);
		this.setImageBitmap(cacheBitmap);
	}
	public void Init(byte[] data) {
		
	}
	//全选
	public void selectAll() {
		startX=0;
		startY=0;
		endX=0;
		endY=0;
		nowX=0;
		nowY=0;
		invalidate();
	}
	
	public void turnLeft() {
		rotate(-90);
	}
	//获取修正过的图片
	public Bitmap getFixedBitmap() {
		if(startX==0 && startY==0 && endX==0 && endY==0)
		{
			return cacheBitmap;
		}
		int x= (int) (Math.min(startX, endX));
		int y= (int) (Math.min(startY, endY));
		int width=(int) Math.abs(startX-endX);
		int height=(int) Math.abs(startY-endY);
		setDrawingCacheEnabled(true);
		Bitmap res=Bitmap.createBitmap(getDrawingCache(),x+2, y+2,width-4, height-4);
		setDrawingCacheEnabled(false);
		//B
			return res;
				//(int)endX- (int)startX,(int)endY-(int)startY);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x=event.getX();
		float y=event.getY();
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX=x;
			startY=y;
			break;
		case MotionEvent.ACTION_MOVE:
			if(Math.abs(x-startX)<=10)
			{
				x+=12*(x<startX?-1:1);
			}
			if(Math.abs(y-startY)<=10)
			{
				y+=12*(y<startY?-1:1);
			}
			x=x>1?x:1;
			y=y>1?y:1;
			nowX=x<getWidth()-1?x:getWidth()-1;
			nowY=y<getHeight()-1?y:getHeight()-1;
			break;
		case MotionEvent.ACTION_UP:
			if(Math.abs(x-startX)<=10)
			{
				x+=12*(x<startX?-1:1);
			}
			if(Math.abs(y-startY)<=10)
			{
				y+=12*(y<startY?-1:1);
			}
			x=x>1?x:1;
			y=y>1?y:1;
			nowX=x<getWidth()-1?x:getWidth()-1;
			nowY=y<getHeight()-1?y:getHeight()-1;
			endX=x<getWidth()-1?x:getWidth()-1;
			endY=y<getHeight()-1?y:getHeight()-1;
			break;
		}
		invalidate();
		return true;
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(nowX==0 && nowY==0)
			return;
		
		canvas.drawRect(new Rect((int)startX,(int)startY, (int)nowX, (int)nowY),paint);
		
	}
	public void turnRight() {
		rotate(90);
	}

	private void rotate(int degree) {
		Matrix matrix=new Matrix();		
		matrix.setRotate(degree);
		//修改图片
		cacheBitmap=Bitmap.createBitmap(cacheBitmap,0,0,photoWidth,photoHeight,matrix, true);
		//重设图片宽高
		photoWidth=cacheBitmap.getWidth();
		photoHeight=cacheBitmap.getHeight();
		this.setImageBitmap(cacheBitmap);
	}


}
