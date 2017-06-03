package org.code.gesture;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.code.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class AppListPreference extends PreferenceFragment implements AdapterView.OnItemClickListener {
    public static List<AppInfo> mAppInfos = new ArrayList<AppInfo>();
    private ListView mListView;
    private ContentResolver mContentResolver;
    String gesture;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gesture_fragment_app_list, container, false);
        mListView = ((ListView) view.findViewById(R.id.listviewApp));
        gesture = getArguments().getString("gesture");
        mContentResolver = getActivity().getContentResolver();
        initData();
        AppListAdapter appListAdapter = new AppListAdapter();
        mListView.setAdapter(appListAdapter);
        mListView.setOnItemClickListener(this);
        return view;
    }
    
    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
        AppListPreference.AppInfo appInfo = mAppInfos.get(paramInt);
        ContentValues values = new ContentValues();
        values.put("pakname", appInfo.getPkgName());
        values.put("classname", appInfo.getActivityName());
        mContentResolver.update(Utils.URI, values, "item=?", new String[]{gesture});

        getFragmentManager().beginTransaction().remove(this).commit();
        getFragmentManager().popBackStack("SELECT", getFragmentManager().POP_BACK_STACK_INCLUSIVE);
        getFragmentManager().popBackStack("GESTURE1", getFragmentManager().POP_BACK_STACK_INCLUSIVE);
    }

    public void initData() {
        mAppInfos.clear();
        PackageManager packageManager = getActivity().getPackageManager();
        Intent intent = new Intent("android.intent.action.MAIN", null);
        intent.addCategory("android.intent.category.LAUNCHER");
        List appList = packageManager.queryIntentActivities(intent, 0);
        Collections.sort(appList, new ResolveInfo.DisplayNameComparator(packageManager));
        Iterator localIterator = appList.iterator();
        while (localIterator.hasNext()) {
            ResolveInfo localResolveInfo = (ResolveInfo) localIterator.next();
            String name = localResolveInfo.activityInfo.name;
            String packageName = localResolveInfo.activityInfo.packageName;
            String label = (String) localResolveInfo.loadLabel(packageManager);
            Drawable icon = localResolveInfo.loadIcon(packageManager);
            Intent appIntent = new Intent();
            appIntent.setComponent(new ComponentName(packageName, name));
            AppInfo appInfo = new AppInfo();
            appInfo.setAppLabel(label);
            appInfo.setAppIcon(icon);
            appInfo.setIntent(appIntent);
            appInfo.setPkgName(packageName);
            appInfo.setActivityName(name);
            mAppInfos.add(appInfo);
        }
    }

    public class AppListAdapter extends BaseAdapter {
        public AppListAdapter() {
        }

        public int getCount() {
            return mAppInfos.size();
        }

        public Object getItem(int paramInt) {
            return mAppInfos.get(paramInt);
        }

        public long getItemId(int paramInt) {
            return 0L;
        }

        @Override
        public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
            ViewHolder viewHolder;
            AppListPreference.AppInfo appInfo = mAppInfos.get(paramInt);
            if (paramView == null) {
                paramView = View.inflate(getActivity(), R.layout.gesture_item_app_list, null);
                viewHolder = new ViewHolder();
                viewHolder.appIcon = ((ImageView) paramView.findViewById(R.id.imgApp));
                viewHolder.tvAppLabel = ((TextView) paramView.findViewById(R.id.tvAppLabel));
                paramView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) paramView.getTag();
            }
            viewHolder.appIcon.setImageDrawable(appInfo.getAppIcon());
            viewHolder.tvAppLabel.setText(appInfo.getAppLabel());
            return paramView;
        }

        class ViewHolder {
            ImageView appIcon;
            TextView tvAppLabel;

        }
    }

    public class AppInfo {
        private String activityName;
        private Drawable appIcon;
        private String appLabel;
        private Intent intent;
        private String pkgName;

        public AppInfo() {
        }

        public String getActivityName() {
            return this.activityName;
        }

        public Drawable getAppIcon() {
            return this.appIcon;
        }

        public String getAppLabel() {
            return this.appLabel;
        }

        public Intent getIntent() {
            return this.intent;
        }

        public String getPkgName() {
            return this.pkgName;
        }

        public void setActivityName(String paramString) {
            this.activityName = paramString;
        }

        public void setAppIcon(Drawable paramDrawable) {
            this.appIcon = paramDrawable;
        }

        public void setAppLabel(String paramString) {
            this.appLabel = paramString;
        }

        public void setIntent(Intent paramIntent) {
            this.intent = paramIntent;
        }

        public void setPkgName(String paramString) {
            this.pkgName = paramString;
        }
    }
}