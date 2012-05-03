package video.ad;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import android.app.Activity;
import android.widget.LinearLayout;

public class AdBanner {
	private static final String PUBLISHER_ID = "a14f91146c6419a";
	
	public static void create(Activity activity, LinearLayout view){
	    AdView adView = new AdView(activity, AdSize.BANNER, PUBLISHER_ID);
	    view.addView(adView);
	    adView.loadAd(new AdRequest());
	}
}
