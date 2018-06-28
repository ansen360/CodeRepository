package org.code.mvp;

/**
 * Presenter层接口
 */
public abstract class IPresenter extends BasePresenter<IView, IModel> {

    abstract void doWork1();

    abstract void doWork2();



}
