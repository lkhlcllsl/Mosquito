package com.lkh.android.base.ui.tabbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lkh.android.base.R;

import java.util.ArrayList;

/**
 * 左右切换控件
 * 布局里属性设置：ItemHeight:每一项的高度， ItemBackGround每一项的背景
 */
public abstract class TabBarBase extends LinearLayout{
	private Context mContext;
	protected ArrayList<View> mChildViewList;
	private CheckedChangedListener mCheckedChangedListener;
	private CheckedChangedListener mCurrentPositionClickListener;
	protected int mCurrent;

	private TypedArray typedArray;
	private int mItemHeight;

	public TabBarBase(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseTabBar);
		if (typedArray != null){
			mItemHeight = typedArray.getDimensionPixelSize(R.styleable.BaseTabBar_ItemHeight, LayoutParams.WRAP_CONTENT);
		}
	}

	public TabBarBase(Context context) {
		super(context);
		this.mContext = context;
	}

    /**
     * 初始化tab
     * @param tabNames
     */
	public void initTabs(String[] tabNames){
		
		mChildViewList = new ArrayList<View>();
		mCurrent = -1;
		for (int i=0; i<tabNames.length; i++){
			View view = getSingleView(tabNames[i], i);
			this.addView(view);
			mChildViewList.add(view);		
		}
		if (typedArray != null){
			typedArray.recycle();
		}
	}
	
	
	private View getSingleView(String name, final int position){
		final View childView = getView(mContext, name, position);
		
		ViewGroup.LayoutParams params = new LayoutParams(0, mItemHeight, 1);
		childView.setLayoutParams(params);

		childView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mCurrent != position){
					mCurrent = position;
					changeState();
					if (mCheckedChangedListener != null){
						mCheckedChangedListener.onCheckedChanged(childView, position);	
					}
				}else{
					if (mCurrentPositionClickListener != null){
						mCurrentPositionClickListener.onCheckedChanged(childView, position);
					}
				}
			}
		});
		
		Drawable itemBackGround = null;
		if (typedArray != null){
			itemBackGround = typedArray.getDrawable(R.styleable.BaseTabBar_ItemBackGround);
		}
		if (itemBackGround != null){
			childView.setBackgroundDrawable(itemBackGround);
		}
		return childView;
	}
	
	private void changeState(){
		for (int i=0; i<mChildViewList.size(); i++){
			View view = mChildViewList.get(i);
			if (i != mCurrent){
				stateChanged(view, false);
			}else {
				stateChanged(view, true);
			}
		}
	}

    /**
     * 切换状态改变时调用，用于设置所有view的状态
     * @param view 要设置的view
     * @param isChecked view是否被选中
     */
	public abstract void stateChanged(View view, boolean isChecked);

    /**
     * 每一项view的获取
     * @param context 上下文
     * @param name 名称
     * @param position 位置
     * @return view
     */
	public abstract View getView(Context context, String name, final int position);

    /**
     * 主动点击某项
     * @param position 每几项
     */
	public void setItemClicked(int position){
		mChildViewList.get(position).performClick();
	}

    /**
     * 点击当前未选中item时调用
     * @param listener
     */
	public void setOnCheckedChangedListener(CheckedChangedListener listener){
		this.mCheckedChangedListener = listener;
	}

    /**
     * 点击当前选中的item时调用
     * @param listener
     */
	public void setOnCurrentPositionClickListener(CheckedChangedListener listener){
		this.mCurrentPositionClickListener = listener;
	}

	public static interface CheckedChangedListener{
        /**
         * 被点击时调用
         * @param view 被点的view
         * @param position 所在位置
         */
		void onCheckedChanged(View view, int position);
	}
}
