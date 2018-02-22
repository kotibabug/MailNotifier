package com.koti.mailnotifier.mvp.mailsetup;

/**
 * Created by Koti on 2/22/2018.
 */

public class EmailPresenter implements EmailContract.Presenter {

    private EmailContract.View emailView;
    private boolean isLoaderActive = false;

    public EmailPresenter(EmailContract.View emailView) {
        this.emailView = emailView;
        emailView.setPresenter(this);
    }

    @Override
    public void setupEmail(String host, String storeType, String port, String username, String password) {
        if (!isLoaderActive) {
            emailView.showLoadingIndicator(true);
            if(validateemailSetup(host,storeType,port,username,password))
                emailView.showError("Fill Required Details");
        }
    }

    private boolean validateemailSetup(String host, String storeType, String port, String username, String password) {
        if (host.trim().length() <= 0 || storeType.trim().length() <= 0 || port.trim().length() <= 0 || username.trim().length() <= 0 || password.trim().length() <= 0)
            return false;
        return true;
    }
}
