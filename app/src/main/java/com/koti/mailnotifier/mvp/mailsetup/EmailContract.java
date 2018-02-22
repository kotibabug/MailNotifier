package com.koti.mailnotifier.mvp.mailsetup;

import com.koti.mailnotifier.mvp.BaseView;

/**
 * Created by Koti on 2/22/2018.
 */

public interface EmailContract {
    interface View extends BaseView<Presenter> {
        void showLoadingIndicator(boolean isActive);

        void showHome();

        void showError(String error);
    }

    interface Presenter {
        void setupEmail(String host, String storeType, String port, String username, String password);
    }
}
