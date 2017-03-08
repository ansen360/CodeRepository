package com.tomorrow_p.fragment;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.tomorrow_p.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;


/**
 * Created by Ansen on 2016/11/22 11:09.
 *
 * @E-mail: tomorrow_p@163.com
 * @Blog: http://blog.csdn.net/qq_25804863
 * @Github: https://github.com/ansen360
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: com.tomorrow_p
 * @Description: TODO
 */
public class BackgroundApps extends Fragment {

    private static final String TAG = "BackgroundApps";
    PackageManager mPackageManager;
    private ArrayList<AppBean> mAllowList = new ArrayList<AppBean>();
    private ArrayList<AppBean> mDisallowList = new ArrayList<AppBean>();
    private AutostartAdapter mAllowAdapter, mdisAllowAdapter;
    private TextView mAllowText, mDisallowText;
    private ContentResolver mContentResolver;
    private ListView mListAllow, mListDisallow;
    private ProgressDialog mProgressDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPackageManager = getActivity().getPackageManager();
        mContentResolver = getActivity().getContentResolver();
        View root = inflater.inflate(R.layout.fragment_autostart, null);
        mListAllow = (ListView) root.findViewById(R.id.list_allow);
        mListDisallow = (ListView) root.findViewById(R.id.list_disallow);
        mAllowText = (TextView) root.findViewById(R.id.allow_text);
        mDisallowText = (TextView) root.findViewById(R.id.disallow_text);
        mAllowAdapter = new AutostartAdapter(mAllowList);
        mdisAllowAdapter = new AutostartAdapter(mDisallowList);
        mListAllow.setAdapter(mAllowAdapter);
        mListDisallow.setAdapter(mdisAllowAdapter);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(getResources().getString(R.string.load_data));
        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        mAllowList.clear();
        mDisallowList.clear();
        mProgressDialog.show();
        getDisallowList();
        initData();
        mAllowText.setText(mAllowList.size() + getResources().getString(R.string.allow_background));
        mDisallowText.setText(mDisallowList.size() + getResources().getString(R.string.disallow_background));
        mAllowAdapter.notifyDataSetChanged();
        mdisAllowAdapter.notifyDataSetChanged();
        mProgressDialog.dismiss();
    }

    private HashSet<String> mDisallowAutoStart = new HashSet<>();
    public static final Uri URI = Uri.parse("content://com.tomorrow_p.apps/autostart");

    private void getDisallowList() {
        mDisallowAutoStart.clear();
        Cursor cursor = mContentResolver.query(URI, null, null, null, null);
        while (cursor != null && cursor.moveToNext()) {
            String package_name = cursor.getString(cursor.getColumnIndex("package_name"));
            mDisallowAutoStart.add(package_name);
        }
        Log.d(TAG, "mDisallowAutoStart size: " + mDisallowAutoStart.size());
        if (cursor != null) {
            cursor.close();
        }
    }

    public void initData() {
        Intent intent = new Intent("android.intent.action.MAIN", null);
        intent.addCategory("android.intent.category.LAUNCHER");
        List appList = mPackageManager.queryIntentActivities(intent, 0);
        Collections.sort(appList, new ResolveInfo.DisplayNameComparator(mPackageManager));
        Iterator localIterator = appList.iterator();
        while (localIterator.hasNext()) {
            ResolveInfo localResolveInfo = (ResolveInfo) localIterator.next();
            //TODO: for system process
//            if (localResolveInfo.system) {
//                continue;
//            }
            String packageName = localResolveInfo.activityInfo.packageName;
            String label = (String) localResolveInfo.loadLabel(mPackageManager);
            Drawable icon = localResolveInfo.loadIcon(mPackageManager);
            AppBean appInfo = new AppBean();
            appInfo.label = label;
            appInfo.icon = icon;
            appInfo.packagName = packageName;
            if (mDisallowAutoStart.contains(packageName)) { //禁止后台运行
                mDisallowList.add(appInfo);
                appInfo.status = false;
            } else {
                mAllowList.add(appInfo);
                appInfo.status = true;
            }
        }
    }


    private class AutostartAdapter extends BaseAdapter {
        private List<AppBean> mList;

        public AutostartAdapter(List<AppBean> list) {
            this.mList = list;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View inflate = convertView.inflate(getActivity(), R.layout.item_autostart_app, null);
            ImageView icon = (ImageView) inflate.findViewById(R.id.icon);
            TextView name = (TextView) inflate.findViewById(R.id.name);
            Switch switchStatus = (Switch) inflate.findViewById(R.id.switch_status);
            final AppBean appBean = mList.get(position);
            icon.setImageDrawable(appBean.icon);
            name.setText(appBean.label);
            switchStatus.setChecked(appBean.status);
            switchStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        int delete = mContentResolver.delete(URI, "package_name = ?", new String[]{appBean.packagName});
                        Log.e(TAG, "delete: " + delete);
                    } else {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("package_name", appBean.packagName);
                        contentValues.put("app_name", appBean.label.toString());
                        mContentResolver.insert(URI, contentValues);
                    }
                }
            });
            return inflate;
        }
    }

    private class AppBean {
        public Drawable icon;
        public CharSequence label;
        public String packagName;
        public boolean status;
    }
}
