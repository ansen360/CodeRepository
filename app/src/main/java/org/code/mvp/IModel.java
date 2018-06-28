package org.code.mvp;

/**
 * Model层接口
 */
public interface IModel {

    void getData1(ICallback callback);
    void getData2(ICallback callback);

    interface ICallback {

        void onSuccess(String data);

        void onFailure(String error);
    }
}
