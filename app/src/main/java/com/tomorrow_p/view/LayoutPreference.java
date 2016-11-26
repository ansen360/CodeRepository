package com.tomorrow_p.view;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Ansen on 2016/11/24 10:43.
 *
 * @E-mail: tomorrow_p@163.com
 * @Blog: http://blog.csdn.net/qq_25804863
 * @Github: https://github.com/1031307403/
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: com.tomorrow_p.view
 * @Description: TODO
 */

public class LayoutPreference extends Preference {

    private View mRootView;

    public LayoutPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSelectable(false);
        //TODO:ansen 16.11.24
        /*final TypedArray a = context.obtainStyledAttributes(
                attrs, com.android.internal.R.styleable.Preference, 0, 0);
        int layoutResource = a.getResourceId(com.android.internal.R.styleable.Preference_layout,
                0);
        if (layoutResource == 0) {
            throw new IllegalArgumentException("LayoutPreference requires a layout to be defined");
        }
        // Need to create view now so that findViewById can be called immediately.
        final View view = LayoutInflater.from(getContext())
                .inflate(layoutResource, null, false);

//        final ViewGroup allDetails = (ViewGroup) view.findViewById(R.id.all_details);
//        if (allDetails != null) {
//            Utils.forceCustomPadding(allDetails, true *//* additive padding *//*);
//        }
        mRootView = view;
        setShouldDisableView(false);*/
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        return mRootView;
    }

    @Override
    protected void onBindView(View view) {
        // Do nothing.
    }

    public View findViewById(int id) {
        return mRootView.findViewById(id);
    }

}
