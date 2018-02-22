package com.koti.mailnotifier.mvp;

/**
 * Created by Koti on 2/22/2018.
 */

public interface BaseView<T> {
    void setPresenter(T presenter);
}
