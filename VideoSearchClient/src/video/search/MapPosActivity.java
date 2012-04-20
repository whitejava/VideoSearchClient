package video.search;

import java.util.List;

import video.main.PosOverLay;



import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MapPosActivity extends MapActivity{

	ToggleButton tgMaptype;
	MapView mapView;

	MapController controller;
	Bitmap posBitmap;
	LocationManager locManger;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mappos);
		//获取点位图片
		 posBitmap =BitmapFactory.decodeResource(getResources(), R.drawable.dest);
		
		mapView=(MapView)findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		mapView.displayZoomControls(true);
		mapView.setSatellite(false);
		controller=mapView.getController();
		tgMaptype=(ToggleButton)findViewById(R.id.tgbtnTrafic);
		Intent intent=getIntent();
		String pos= intent.getStringExtra("pos");
		
		String lng=pos.trim().split(" ")[0];
		String lat= pos.trim().split(" ")[1];
		Button btnBack=(Button)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MapPosActivity.this.finish();	
			}
		});
		try
		{
			double dLong=Double.parseDouble(lng);
			double dLat=Double.parseDouble(lat);
			//更新地图
			updateMapView(dLong,dLat);
			GeoPoint gpGeoPoint=new GeoPoint((int)(dLat*1E6), (int)(dLong*1E6));
			
			controller.animateTo(gpGeoPoint);
			List<Overlay> olList=mapView.getOverlays();
			olList.clear();
			olList.add(new PosOverLay(gpGeoPoint,posBitmap));	
		}
		//若经纬度不正确
		catch (Exception e) {
			Toast.makeText(this, "无法获取商品经纬度，请返回重试。", Toast.LENGTH_LONG).show();
		}

		
		tgMaptype.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked)
				{
					mapView.setSatellite(true);
				}
				else
				{
					mapView.setSatellite(false);
				}
			}
		}) ;
			
			
	}
	private void updateMapView(double dLong, double dLat) {
		// TODO Auto-generated method stub
		GeoPoint gpGeoPoint=new GeoPoint((int)(dLat*1E6), (int)(dLong*1E6));
		mapView.displayZoomControls(true);
		controller.animateTo(gpGeoPoint);
		controller.setZoom(20);

		List<Overlay> olList=mapView.getOverlays();
		olList.clear();
		olList.add(new PosOverLay(gpGeoPoint,posBitmap));
		
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return true;
	}
	}
