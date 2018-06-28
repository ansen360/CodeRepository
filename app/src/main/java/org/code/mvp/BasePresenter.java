package org.code.mvp;

import android.support.annotation.UiThread;

/**
 * Created by Ansen on 2018/6/28 09:38.
 *
 * @E-mail: ansen360@126.com
 * @Blog: "http://blog.csdn.net/qq_25804863"
 * @Github: "https://github.com/ansen360"
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: org.code.mvp
 * @Description: TODO
 */
public abstract class BasePresenter<V extends IView, M extends IModel> {
    protected V view;
    protected M model;

    public BasePresenter() {
        model = createModel();
    }

    @UiThread
    void attachView(V view) {
        this.view = view;
    }

    @UiThread
    void detachView() {
        this.view = null;
    }

    abstract M createModel();
}
