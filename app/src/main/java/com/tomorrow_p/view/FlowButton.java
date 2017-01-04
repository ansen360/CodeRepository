package com.tomorrow_p.view;

import android.content.Context;
import android.widget.Button;

/**
 * Created by admin on 2016/9/19.
 */
public class FlowButton extends Button {

    private static final int COLOR1 = 0xFF5698BB;
    private static final int COLOR2 = 0xFF569874;
    private static final int COLOR3 = 0xaa5698CD;
    private static final int COLOR4 = 0x66CD9856;
    private static final int PADDING = 0;

    private static int[] COLORS = new int[]{COLOR1, COLOR2, COLOR3, COLOR4};
    private static int COLOR_INDEX = 0;

    private void nextColor() {
        COLOR_INDEX = (COLOR_INDEX + 1) % COLORS.length;
    }

    public FlowButton(Context context, String buttonName, int backgroundRes, OnClickListener listener) {
        super(context);
        nextColor();
        setText(buttonName);
        setBackgroundColor(backgroundRes);
        setOnClickListener(listener);
        setAllCaps(false);
        setTextSize(16);
        setPadding(PADDING, PADDING, PADDING, PADDING);
    }

    public FlowButton(Context context, String buttonName, OnClickListener listener) {
        super(context);
        nextColor();
        setText(buttonName);
        setBackgroundColor(COLORS[COLOR_INDEX % COLORS.length]);
        setOnClickListener(listener);
        setTextSize(16);
        setAllCaps(false);
        setPadding(PADDING, PADDING, PADDING, PADDING);
    }
}
