package org.code.mvp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 实现IView接口,实现ui操作具体逻辑
 */
public class ViewActivity extends Activity implements IView {

    private ProgressBar progressBar;
    private TextView infoText;

    private IPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化控件
        // progressBar = null;
        // infoText = null;

        // 初始化Presenter
        mPresenter = new MyPresenter();
        mPresenter.attachView(this);
        // 实现仅仅通过Presenter来处理业务逻辑
        mPresenter.doWork1();
    }

    @Override
    public void onProgressLoading(int progress) {
        // UI展示 业务处理中
        progressBar.setProgress(progress);
    }

    @Override
    public void showText(String data) {
        // UI展示 业务处理完成结果
        infoText.setText(data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
