package com.safly.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public abstract class BaseRecycleView extends RecyclerView{

	private static final String TAG = "BaseRecycleView";
	
	private View emptyView;

	private DataEmptyObserview dataEmptyObserview;


	public BaseRecycleView(Context context) {
		this(context, null);
	}
	
	public BaseRecycleView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public BaseRecycleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.dataEmptyObserview = new DataEmptyObserview();
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		/**
		 *拦截多点触控
		 */
		if(event.getActionMasked() == MotionEvent.ACTION_POINTER_UP ||
				event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN){
			return true;
		}
		return super.onInterceptTouchEvent(event);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		setLayoutManager(getLayoutManger());
		addItemDecoration(getItemDecoration());
	}
	
	@Override
	public void setAdapter(Adapter adapter) {
		Adapter oldAdapter = getAdapter();
		if(oldAdapter != null && emptyView != null){
			oldAdapter.unregisterAdapterDataObserver(dataEmptyObserview);
		}
		if(adapter == null){
			throw new NullPointerException("adapter is null!");
		}
		if(!(adapter instanceof BaseRecyleAdapter)){
			throw new ClassCastException("RecyclerView's adapter can not cast to BaseRecyleAdapter," +
					"so userd adapter should extends BaseRecyleAdapter");
		}
		super.setAdapter(adapter);
		adapter.registerAdapterDataObserver(dataEmptyObserview);
		dataEmptyObserview.onChanged();
	}

	/**
	 * 设置数据为空时要显示的布局
	 * @param emptyView
	 */
	public void setEmptyView(View emptyView){
		this.emptyView = emptyView;
		dataEmptyObserview.onChanged();
	}

	/**
	 *根据自己的需要设置不同的LayoutManger 
	 */
	public abstract LayoutManager getLayoutManger();

	/**
	 * 获取分割线
	 */
	public abstract ItemDecoration getItemDecoration();

	/**
	 * 数据观察者
	 */
	private final class DataEmptyObserview extends AdapterDataObserver{
		@Override
		public void onChanged() {
			Log.i("FinalRecycleView","onChanged");
			Adapter adapter = getAdapter();
			if(adapter != null && emptyView != null){
				final boolean isEmpty = adapter.getItemCount() == 0;
				emptyView.setVisibility(isEmpty?VISIBLE:GONE);
				BaseRecycleView.this.setVisibility(isEmpty?GONE:VISIBLE);
			}
		}
	}
}
