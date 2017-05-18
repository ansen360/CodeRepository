package com.code.gesture;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Ansen on 2017/3/23 17:29.
 *
 * @E-mail: tomorrow_p@163.com
 * @Blog: http://blog.csdn.net/qq_25804863
 * @Github: https://github.com/ansen360
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: com.tomorrow_p.gesture
 * @Description: TODO
 */
public class WallOfTextPreference extends Preference {


    public WallOfTextPreference(Context context) {
        super(context);
    }

    public WallOfTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WallOfTextPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WallOfTextPreference(Context context, AttributeSet attrs, int defStyleAttr,
                                int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        final TextView summary = (TextView) view.findViewById(android.R.id.summary);
        summary.setMaxLines(20);
    }
}
