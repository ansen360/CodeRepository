package org.code.mvp;

/**
 * 实现IPresenter接口,处理Model层的数据获取及View层的UI显示
 */
public class Presenter implements IPresenter, IModel.ICallback {

    private final IView mView;
    private final IModel mModel;

    public Presenter(IView view) {
        mView = view;
        mModel = new Model();
    }

    @Override
    public void loadData() {
        mView.onProgressLoading(0);
        mModel.getData(Presenter.this);
        mView.onProgressLoading(100);
    }

    @Override
    public void onSuccess(String data) {
        // 数据获取成功回调
        mView.setData(data);
    }

    @Override
    public void onFailure(String error) {
        // 数据获取失败回调
        mView.setData(error);
    }
}
