package com.koti.mailnotifier.mvp.mailsetup;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.koti.mailnotifier.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Koti on 2/22/2018.
 */

public class EmailSetupFragment extends Fragment implements EmailContract.View {

    private EmailContract.Presenter presenter;
    private OnCancelClick onCancelClick;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.inputHost)
    EditText host;
    @BindView(R.id.inputStoreType)
    EditText storeType;
    @BindView(R.id.inputPort)
    EditText port;
    @BindView(R.id.inputUsername)
    EditText username;
    @BindView(R.id.inputPassword)
    EditText password;

    public static EmailSetupFragment getInstance() {
        EmailSetupFragment emailSetupFragment = new EmailSetupFragment();
        return emailSetupFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onCancelClick = (OnCancelClick) context;
        } catch (ClassCastException exception) {
            throw new ClassCastException(exception.toString());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emailsetup, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onCancelClick = null;
    }

    @OnClick(R.id.btn_submit)
    public void onSubmit() {
        presenter.setupEmail(host.getText().toString(), storeType.getText().toString(), port.getText().toString(), username.getText().toString(), password.getText().toString());
    }

    @OnClick(R.id.btn_cancel)
    public void onCancel() {
        onCancelClick.onCancel();
    }

    @Override
    public void setPresenter(EmailContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showError(String error) {
        Snackbar.make(getView(), error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showLoadingIndicator(boolean isActive) {
        if (isActive)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showHome() {

    }

    interface OnCancelClick {
        public void onCancel();
    }
}
