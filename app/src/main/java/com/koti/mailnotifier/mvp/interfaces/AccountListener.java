package com.koti.mailnotifier.mvp.interfaces;

import com.koti.mailnotifier.mvp.db.Account;

/**
 * Created by Koti on 2/20/2018.
 */

public interface AccountListener {
    public void onAccountCreated(Account account);

    public void onAccountDeleted();

    public void onGetAccount(Account account);

    public void onUpdateAccount(Account account);

    public void onError(int errorCode, String error);
}
