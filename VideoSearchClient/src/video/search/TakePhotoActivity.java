package video.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class TakePhotoActivity extends Activity {
	SurfaceView sView;
	SurfaceHolder surfaceHolder;
	int screenWidth;
	int ScreenHeight;
	Camera camera;
	boolean isPreview=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.takephoto);
		WindowManager wm=(WindowManager)getSystemService(Context.WINDOW_SERVICE);
		Display display=wm.getDefaultDisplay();
		screenWidth=display.getWidth();
		ScreenHeight=display.getHeight();
		sView=(SurfaceView)findViewById(R.id.sfvPPreview);
		surfaceHolder=sView.getHolder();
		surfaceHolder.addCallback(new SurfaceHolder.Callback() {
			
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				if(camera!=null)
				{
					if(isPreview)
						camera.stopPreview();
					camera.release();
					camera=null;
				}
				
			}
			
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				initeCamera();
				
			}
			
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {

			}
		});
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	protected void initeCamera() {
		if(!isPreview)
		{
			camera=Camera.open();
		}
		if(camera!=null && !isPreview)
		{
			try 
			{
				Camera.Parameters parameters=camera.getParameters();
				camera.setDisplayOrientation(90);
				parameters.setPreviewSize(screenWidth,ScreenHeight);
				parameters.setPreviewFrameRate(4);
				parameters.setPictureFormat(PixelFormat.JPEG);
				parameters.set("jpeg-quality", 85);
				parameters.setPictureSize(screenWidth, ScreenHeight);
				camera.setParameters(parameters);
				camera.setPreviewDisplay(surfaceHolder);
				camera.startPreview();
				camera.autoFocus(null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			isPreview=true;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_CAMERA:
			if(camera !=null && event.getRepeatCount()==0)
			{
				//≈ƒ’’
				camera.takePicture(null, null, myjpegCallback);
				return true;
			}
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	PictureCallback myjpegCallback=new PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {		
			Intent intent =new Intent(TakePhotoActivity.this,FixPhotoActivity.class);
			intent.putExtra("bitmap",data);
			startActivity(intent);		
			finish();
		}
	};
}
