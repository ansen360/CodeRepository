package com.ansen.gesture;

import android.content.Context;
import android.preference.SwitchPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

import com.ansen.R;

public class GestureSwitchPreference extends SwitchPreference {
	protected static final String TAG = "GestureSwitchPreference";
	private Switch mSwitch;
	private View mView;
	private Context mContext;
	private OnGestureClickListener mOnGestureClickListener;
	private OnGestureCheckedChangedListener mOnGestureCheckedChangedListener;

	public GestureSwitchPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setWidgetLayoutResource(R.layout.gesture_switch_preference);
		mContext = context;
	}

	public GestureSwitchPreference(Context context) {
		super(context);
		setWidgetLayoutResource(R.layout.gesture_switch_preference);
		mContext = context;
	}

	@Override
	protected void onBindView(View view) {
		mSwitch = (Switch) view.findViewById(R.id.prefrence_switch_id);
		mSwitch.setChecked(Utils.getConfig(mContext, GestureSwitchPreference.this.getKey(), false));
		mView = view;
		mView.setClickable(true);
		mView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(mSwitch.isChecked()) {
					String key = GestureSwitchPreference.this.getKey();
					if(mOnGestureClickListener!=null){
						mOnGestureClickListener.onClick(v,key);
					}
				}
			}
		});
		mSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton button,
					boolean isChecked) {
				String key = GestureSwitchPreference.this.getKey();
				if(mOnGestureCheckedChangedListener != null){
					mOnGestureCheckedChangedListener.OnChecked(key, isChecked);
				}
				Utils.setConfig(mContext, GestureSwitchPreference.this.getKey(), isChecked);
			}
		});
		
		super.onBindView(view);
	}
	
	public interface OnGestureClickListener {
		public void onClick(View view, String key);
	}
	
	public interface OnGestureCheckedChangedListener {
		public void OnChecked(String key, boolean isChecked);
	}
	
	public void setOnGestureClickListener(OnGestureClickListener l) {
		this.mOnGestureClickListener=l;
	}
	
	public void setOnGestureCheckedChangedListener(OnGestureCheckedChangedListener l) {
		this.mOnGestureCheckedChangedListener=l;
	}
}
