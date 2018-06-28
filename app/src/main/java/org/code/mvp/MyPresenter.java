package org.code.mvp;

/**
 * 实现IPresenter接口,处理Model层的数据获取及View层的UI显示
 */
public class MyPresenter extends IPresenter {


    @Override
    IModel createModel() {
        return new Model();
    }

    @Override
    public void doWork1() {
        view.onProgressLoading(0);
        model.getData1(new IModel.ICallback() {
            @Override
            public void onSuccess(String data) {
                // 数据获取成功回调
                view.showText(data);
            }

            @Override
            public void onFailure(String error) {
                // 数据获取失败回调
                view.showText(error);
            }
        });
        view.onProgressLoading(100);
    }

    @Override
    void doWork2() {

    }


    @Override
    public void detachView() {
        view = null;
    }


}
