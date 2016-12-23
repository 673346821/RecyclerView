package com.safly.recyclerview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.State;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

public class FlexDividerDecoration extends RecyclerView.ItemDecoration {

    /**
     * private int spanCount;
     * private ShowStyle showStyle;
     * private Drawable divider;
     * private int dividerWidth;
     */
    private Drawable mDivider;
    private int mSize;
    private static final int[] ATTRS = new int[] { android.R.attr.listDivider };

    private int type;
    private static final int TYPE_SDK = 0x001;
    private static final int TYPE_DRAWABLE = 0X002;
    private final Rect bounds = new Rect();


    public FlexDividerDecoration(Builder builder) {
        final Builder br = builder;
        if (br.mDrawable != null) {
            this.mDivider = br.mDrawable;
            this.type = TYPE_DRAWABLE;
        }

        if (br.mDrawable == null) {
            final TypedArray a = br.mContext.obtainStyledAttributes(ATTRS);
            this.mDivider = a.getDrawable(0);
            a.recycle();
            this.type = TYPE_SDK;
        }

        this.mSize = br.mSize;
    }

    public static final class Builder {
        private Context mContext;
        private Drawable mDrawable = null;
        private int mSize = 0;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setDrawable(final Drawable drawable) {
            this.mDrawable = drawable;
            return this;
        }

        public Builder setSize(final int size) {
            this.mSize = size;
            return this;
        }

        public FlexDividerDecoration build() {
            return new FlexDividerDecoration(this);
        }

    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, State state) {
        if(parent.getAdapter() == null){
            return;
        }
        drawDivider(c,parent);
    }

    private void drawDivider(Canvas c, RecyclerView parent){
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            drawRect(c,measureHorizontalBounds(params,child));
            drawRect(c,measureVerticalBounds(params, child));
        }
    }

    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = 1;
        LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager)
                    .getSpanCount();
        }
        return spanCount;
    }

    private void drawRect(Canvas c, Rect bounds){
        switch (type){
            case TYPE_DRAWABLE:
                mDivider.setBounds(bounds);
                mDivider.draw(c);
                break;
        }
    }

    private Rect measureHorizontalBounds(RecyclerView.LayoutParams params, View child) {
        bounds.left = child.getLeft() - params.leftMargin;
        bounds.right = child.getRight() + params.rightMargin+mSize;
        bounds.top = child.getBottom() + params.bottomMargin;
        bounds.bottom = bounds.top+mSize;
        return bounds;
    }

    private Rect measureVerticalBounds(RecyclerView.LayoutParams params, View child) {
        bounds.top = child.getTop() - params.topMargin;
        bounds.bottom = child.getBottom() + params.bottomMargin+mSize;
        bounds.left = child.getRight() + params.rightMargin;
        bounds.right = bounds.left+mSize;
        return bounds;
    }

    private Rect getOutRect(RecyclerView parent, int pos, int spanCount, int childCount){
        LayoutManager layoutManager = parent.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager){

//            boolean isLastColum = isLastColum(parent, pos, spanCount, childCount);
//            boolean isLastRow = isLastRaw(parent, pos, spanCount, childCount);
            final int lastRowStartIndex = childCount - (childCount % spanCount == 0?spanCount:childCount % spanCount);
            final boolean isLastRow = pos >= lastRowStartIndex;
            final boolean isLastColum = (pos + 1) % spanCount == 0;

            if (isLastRow && isLastColum){
                return new Rect(0,0,0,0);
            }else if(isLastRow && !isLastColum){
                return new Rect(0,0,mSize,0);
            }else if(!isLastRow && isLastColum){
                return new Rect(0,0,0,mSize);
            }else{
                return new Rect(0,0,mSize,mSize);
            }
        }else if(layoutManager instanceof LinearLayoutManager){
            int orientation = ((LinearLayoutManager)layoutManager).getOrientation();
            if(orientation == LinearLayoutManager.VERTICAL){//垂直风向滚动
                if((pos+1) == childCount)//最后一行不显示分割线
                    return new Rect(0,0,0,0);
                else
                    return new Rect(0,0,0,mSize);
            } else{//水平风向滚动
                if((pos+1) == childCount)//最后一列不显示分割线
                    return new Rect(0,0,0,0);
                else
                    return new Rect(0,0,mSize,0);
            }
        }else if (layoutManager instanceof StaggeredGridLayoutManager){
            return new Rect(0,0,0,0);
        }
        return null;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
        int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        int childCount = parent.getAdapter().getItemCount();
        final int spanCount = getSpanCount(parent);
        outRect.set(getOutRect(parent, itemPosition, spanCount, childCount));
    }


    /**
     * 偏移量
     */
    private boolean isLastRaw(RecyclerView parent, int pos, int spanCount,
                              int childCount) {
        LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            childCount = childCount - childCount % spanCount;
            if (pos >= childCount)// 如果是最后一行，则不需要绘制底部
                return true;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                childCount = childCount - childCount % spanCount;
                // 如果是最后一行，则不需要绘制底部
                if (pos >= childCount)
                    return true;
            } else
            // StaggeredGridLayoutManager 且横向滚动
            {
                // 如果是最后一行，则不需要绘制底部
                if ((pos + 1) % spanCount == 0) {
                    return true;
                }
            }
        }else if(layoutManager instanceof  LinearLayoutManager){
            int orientation = ((LinearLayoutManager) layoutManager).getOrientation();
            if (orientation == LinearLayoutManager.VERTICAL) {
                if (pos+1 == childCount)
                    return true;
            }

        }
        return false;
    }
    private boolean isLastColum(RecyclerView parent, int pos, int spanCount,
                                int childCount) {
        LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
            {
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
                {
                    return true;
                }
            } else {
                childCount = childCount - childCount % spanCount;
                if (pos >= childCount)// 如果是最后一列，则不需要绘制右边
                    return true;
            }
        }else if(layoutManager instanceof  LinearLayoutManager){
            int orientation = ((LinearLayoutManager) layoutManager).getOrientation();
            if (orientation == LinearLayoutManager.HORIZONTAL) {
                if (pos+1 == childCount)
                    return true;
            }
        }
        return false;
    }
}
