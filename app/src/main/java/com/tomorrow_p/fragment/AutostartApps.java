package com.tomorrow_p.fragment;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import java.util.List;

/**
 * Created by Ansen on 2016/11/22 11:09.
 *
 * @E-mail: tomorrow_p@163.com
 * @Blog: http://blog.csdn.net/qq_25804863
 * @Github: https://github.com/1031307403/
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: com.tomorrow_p
 * @Description: TODO
 */
public class AutostartApps extends Fragment {

    private static final String TAG = "AutostartApps";
    PackageManager mPackageManager;
    private ArrayList<AppBean> mAllowList = new ArrayList<AppBean>();
    private ArrayList<AppBean> mDisallowList = new ArrayList<AppBean>();
    private AutostartAdapter mAllowAdapter, mdisAllowAdapter;
    private TextView mAllowText, mDisallowText, mLoadData;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPackageManager = getActivity().getPackageManager();
        View root = inflater.inflate(R.layout.fragemnt_autostart, null);
        ListView mListAllow = (ListView) root.findViewById(R.id.list_allow);
        ListView mListDisallow = (ListView) root.findViewById(R.id.list_disallow);
        mAllowText = (TextView) root.findViewById(R.id.allow_text);
        mDisallowText = (TextView) root.findViewById(R.id.disallow_text);
        mLoadData = (TextView) root.findViewById(R.id.load_data);
        mAllowAdapter = new AutostartAdapter(mAllowList);
        mdisAllowAdapter = new AutostartAdapter(mDisallowList);
        mListAllow.setAdapter(mAllowAdapter);
        mListDisallow.setAdapter(mdisAllowAdapter);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAllowList.clear();
        mDisallowList.clear();

        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                initData();
                return null;
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mLoadData.setVisibility(View.GONE);
                mAllowText.setText(mAllowList.size() + getResources().getString(R.string.allow_autostart));
                mDisallowText.setText(mDisallowList.size() + getResources().getString(R.string.disallow_autostart));
                mAllowAdapter.notifyDataSetChanged();
                mdisAllowAdapter.notifyDataSetChanged();
            }
        };
        asyncTask.execute();
    }

    private void initData() {
        Intent intent = new Intent(Intent.ACTION_BOOT_COMPLETED);
        List<ResolveInfo> resolveInfoList = mPackageManager.queryBroadcastReceivers(intent, PackageManager.GET_DISABLED_COMPONENTS);

        final ArrayList<AppBean> temp1 = new ArrayList<>();
        final ArrayList<AppBean> temp2 = new ArrayList<>();
        String oldPackageName = "";
        for (ResolveInfo resolveInfo : resolveInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            if ((packageName != null && packageName.contains("com.android")) || packageName.equals("android")) {
                continue;
            }
            ComponentName componentName = new ComponentName(packageName, resolveInfo.activityInfo.name);
            AppBean appBean = new AppBean();
            int status = getActivity().getPackageManager().getComponentEnabledSetting(componentName);

            try {
                PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo(packageName, 0);
                appBean.icon = packageInfo.applicationInfo.loadIcon(mPackageManager);
                appBean.name = packageInfo.applicationInfo.loadLabel(mPackageManager);
                appBean.componentName = componentName;
                Log.d(TAG, "status: " + status + " appName: " + packageInfo.applicationInfo.loadLabel(mPackageManager) +
                        "  packagename: " + packageName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if (PackageManager.COMPONENT_ENABLED_STATE_DISABLED == status) {
                appBean.status = false;
                if (!packageName.equals(oldPackageName)) {
                    temp2.add(appBean);
                }
            } else {
                appBean.status = true;
                if (!packageName.equals(oldPackageName)) {
                    temp1.add(appBean);
                }
            }
            oldPackageName = packageName;
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAllowList.addAll(temp1);
                mDisallowList.addAll(temp2);
            }
        });
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
            name.setText(appBean.name);
            switchStatus.setChecked(appBean.status);
            switchStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mPackageManager.setComponentEnabledSetting(appBean.componentName,
                            isChecked ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP);

                }
            });
            return inflate;
        }
    }

    private class AppBean {
        public Drawable icon;
        public CharSequence name;
        public boolean status;
        public ComponentName componentName;
    }
}
