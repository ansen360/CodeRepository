package com.code.view;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.code.R;

/**
 * Created by admin on 2016/9/19.
 */
public class FlowButton extends TextView {

    private static final int COLOR1 = 0xFF5698BB;
    private static final int COLOR2 = 0xFF569874;
    private static final int COLOR3 = 0xFF5698CD;
    private static final int COLOR4 = 0xFF003366;
    private static final int COLOR5 = 0xFF9f5f9f;
    private static final int COLOR6 = 0xFFff6ec7;
    private static final int COLOR7 = 0xFF236b8e;
    private static final int PADDING_TOP = 12;
    private static final int PADDING_LEFT = 20;

    private static int[] COLORS = new int[]{COLOR1, COLOR2, COLOR3, COLOR4, COLOR5, COLOR6, COLOR7};
    private static int COLOR_INDEX = 0;

    private void nextColor() {
        COLOR_INDEX = (COLOR_INDEX + 1) % COLORS.length;
    }

    public FlowButton(Context context, String buttonName, int backgroundRes, OnClickListener listener) {
        super(context);
        nextColor();
        setText(buttonName);
        setBackgroundResource(backgroundRes);
        setOnClickListener(listener);
        setTextSize(16);
        setTextColor(COLORS[COLOR_INDEX % COLORS.length]);
        setPadding(PADDING_LEFT, PADDING_TOP, PADDING_LEFT, PADDING_TOP);

    }

    public FlowButton(Context context, String buttonName, OnClickListener listener) {
        super(context);
        nextColor();
        setText(buttonName);
        setBackgroundResource(R.drawable.btn_circular_cancel);
        setOnClickListener(listener);
        setTextSize(16);
        setTextColor(COLORS[COLOR_INDEX % COLORS.length]);
        int i = COLOR_INDEX % COLORS.length;
        setPadding(PADDING_LEFT, PADDING_TOP, PADDING_LEFT, PADDING_TOP);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 10, 10, 10);
        setLayoutParams(layoutParams);
    }
}
