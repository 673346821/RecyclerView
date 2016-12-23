package com.safly.recyclerview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.List;

public class FinalAdapter extends BaseRecyleAdapter<String,BaseViewHolder> {

	RecyclerView recyclerView;

	public FinalAdapter(Context context, List<String> datas,RecyclerView recyclerView) {
		super(context, datas);
		this.recyclerView = recyclerView;
	}

	@Override
	public int getItemLayoutId(int viewType) {

		RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

		if (layoutManager instanceof LinearLayoutManager){
			int orientation = ((LinearLayoutManager) layoutManager).getOrientation();
			if (orientation == LinearLayoutManager.VERTICAL) {//垂直
				return R.layout.item;
			}else if(orientation == LinearLayoutManager.HORIZONTAL){
				return R.layout.item_h;
			}
		}

		return 0;
	}


	@Override
	public BaseViewHolder convertCreateViewHolder(View itemView) {
		return new BaseViewHolder(itemView);
	}

	@Override
	public void convertBindViewHolder(BaseViewHolder viewHolder, int position) {
		viewHolder.setText(R.id.item_text, mDatas.get(position));
	}
}
