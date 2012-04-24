package video.search.page;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public class PageEvent implements OnScrollListener {
	private OnPageTurnListener listener;
	private int itemCountPerPage;
	private int currentPage;
	
	public PageEvent(OnPageTurnListener listener, int itemCountPerPage){
		this.listener = listener;
		this.itemCountPerPage = itemCountPerPage;
	}
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		int page = firstVisibleItem / itemCountPerPage;
		if(currentPage != page){
			currentPage = page;
			listener.onPageTurn(page);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}
}
