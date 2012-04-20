package video.main;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class PosOverLay extends Overlay {
	Bitmap posbitBitmap;
	GeoPoint gp;
	public PosOverLay(GeoPoint gp,Bitmap posBitmap)
	{
		super();
		this.gp=gp;
		this.posbitBitmap=posBitmap;
	}
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		// TODO Auto-generated method stub
		if(!shadow)
		{
			Projection proj=mapView.getProjection();
			Point point=new Point();
			proj.toPixels(gp, point);
			canvas.drawBitmap(posbitBitmap, point.x-posbitBitmap.getWidth()/2,point.y-posbitBitmap.getHeight(),null);
		}
	}
	
}
