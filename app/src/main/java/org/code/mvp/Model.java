package org.code.mvp;

/**
 * 实现IModel接口,实现获取数据具体逻辑
 */
public class Model implements IModel {

    @Override
    public void getData(final ICallback callback) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(5000);  // 模拟获取数据
                    callback.onSuccess("获取数据 成功 ");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    callback.onFailure("获取数据 失败");
                }
            }
        }.start();

    }
}
