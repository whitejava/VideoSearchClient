package video.search.page;

import android.widget.TextView;

public class ShowPageNumber implements OnPageTurnListener {
	private static final int PAGE_NUMBER_VISIBLE_DURATION = 2000;
	
	private AutoHide autoHide;
	private TextView textView;
	private int pageCount;
	
	public ShowPageNumber(TextView textView, int pageCount){
		this.textView = textView;
		this.pageCount = pageCount;
		autoHide = new AutoHide(textView, PAGE_NUMBER_VISIBLE_DURATION);
	}
	
	@Override
	public void onPageTurn(int page) {
		textView.setText(String.format("%d/%dҳ", page+1, pageCount));
		autoHide.activate();
	}
}