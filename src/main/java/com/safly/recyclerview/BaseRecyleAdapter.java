package com.safly.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.List;

public abstract class BaseRecyleAdapter<T, VH extends BaseViewHolder> extends RecyclerView.Adapter<VH> {
    public Context mContext;
    public List<T> mDatas;

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }


    public BaseRecyleAdapter(Context context, List<T> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        final View contentView = inflater.inflate(getItemLayoutId(viewType), null, false);
        Log.i("BaseRecyleAdapter","viewType---"+getItemLayoutId(viewType));
        VH vh = convertCreateViewHolder(contentView);
        return vh;
    }

    @Override
    public void onBindViewHolder(final VH viewHolder, final int position) {
        convertBindViewHolder(viewHolder, position);
        if (mOnItemClickLitener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = viewHolder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(viewHolder.itemView, pos);
                }
            });

            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = viewHolder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(viewHolder.itemView, pos);
                    return true;
                }
            });
        }
    }


    public void addData(int position, T t) {
        mDatas.add(position, t);
        notifyItemInserted(position);
        flushDate();
    }

    public void removeData(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
        flushDate();
    }


    public void flushDate() {
        notifyDataSetChanged();
    }

    /**
     * 给View赋值内容显示
     *
     * @param viewHolder
     * @param position
     */
    public abstract void convertBindViewHolder(VH viewHolder, final int position);

    /**
     * 设置item布局
     *
     * @param viewType
     * @return
     */
    public abstract int getItemLayoutId(int viewType);


    /**
     * 对外接口，viewHolder与itemView进行绑定
     *
     * @param itemView
     * @return VH
     */
    public abstract VH convertCreateViewHolder(View itemView);
}
