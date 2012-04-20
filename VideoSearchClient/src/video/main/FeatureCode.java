package video.main;

import video.values.HanderMessage;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class FeatureCode {
	public static String calculateImageFeatureCode(Bitmap bitmap) {
		bitmap = normalizeBitmap(bitmap);
		int[] histogram = getIntHistogram(bitmap);
		return formatHistogram(histogram);
	}

	private static Bitmap normalizeBitmap(Bitmap bitmap) {
		return Bitmap.createScaledBitmap(bitmap, 256, 256, true);
	}

	public static String calculateVideoFeatureCode(Context context,Handler mHandler, Uri videoUri, int cutNum) {
		
		long videoDuration = getVideoDuration(context, videoUri) * 1000;
		
		StringBuilder result = new StringBuilder();
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		retriever.setDataSource(context, videoUri);
		
		for (int i = 0; i < cutNum; i++) {
			long at = videoDuration * i / (cutNum+1);
			// Bitmap frame = getVideoFrame(context, videoUri, at);
			Bitmap frame = null;
			try {
				frame = retriever.getFrameAtTime(at);

				// 如果没取到帧
				if (frame != null) {
					String code = calculateImageFeatureCode(frame);
					// 释放掉该图片
					frame.recycle();
					result.append(code);
					result.append("|");
				}
				// 发送更新
				double p = (i + 1) * 1.0 / cutNum;
				updateProgress(p, mHandler);
				// 发送更新消息
			} 
			catch (Exception e) {
				Log.e("错误", e.getMessage());

			}
		}
		retriever.release();
		try{
		deleteLastChar(result);
		}
		catch (Exception e) {
			return "";
		}
		return result.toString();
	}
	private static void updateProgress(double p, Handler mHandler) throws Exception
	{
		Message message = new Message();
		message.what = HanderMessage.UPDATE;
		Bundle bundle = new Bundle();
		bundle.putInt("progress", (int) ((p) * 100));
		message.setData(bundle);
		mHandler.sendMessage(message);
	}
	private static long getVideoDuration(final Context context,final Uri videoUri) {
		MediaPlayer a= new MediaPlayer();
		try {
			a.setDataSource(context, videoUri);
			a.prepare();
			int result = a.getDuration();
			a.release();
			return result;
		} catch (Exception exception) {
			a.release();
			throw new RuntimeException(exception);
		}
	}

	private static Bitmap getVideoFrame(Context context, Uri uri, long time) {
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();

		try {
			retriever.setDataSource(context, uri);
			return retriever.getFrameAtTime(time);
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		} catch (RuntimeException ex) {
			ex.printStackTrace();
		} finally {
			try {
				retriever.release();
			} catch (RuntimeException ex) {
			}
		}
		return null;
	}

	private static float[] getFloatHistogram(Bitmap bitmap) {
		int[] a = getIntHistogram(bitmap);
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int area = width * height;
		float[] result = new float[a.length];
		for (int i = 0; i < a.length; i++) {
			result[i] = a[i] / (float) area;
		}
		return result;
	}

	private static int[] getIntHistogram(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int[] result = new int[256];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int color = bitmap.getPixel(x, y);
				int greyScale = getGreyScale(color);
				result[greyScale]++;
			}
		}
		bitmap.recycle();
		return result;
	}

	private static int getGreyScale(int color) {
		int red = Color.red(color);
		int green = Color.green(color);
		int blue = Color.blue(color);
		return (red + green + blue) / 3;
	}

	private static String formatHistogram(int[] histogram) {
		StringBuilder result = new StringBuilder();
		for (int each : histogram) {
			result.append(Integer.toString(each));
			result.append(',');
		}
		deleteLastChar(result);
		return result.toString();
	}

	private static void deleteLastChar(StringBuilder builder) throws StringIndexOutOfBoundsException {
		int length = builder.length();
		int index = length - 1;
		builder.deleteCharAt(index);
	}
}