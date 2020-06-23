package com.tapan.smartcontact.home.presenter;

import com.tapan.smartcontact.core.base.presenter.BasePresenter;
import com.tapan.smartcontact.home.callback.LoginListener;
import com.tapan.smartcontact.home.view.LoginView;

public class LoginPresenter<V extends LoginView> extends BasePresenter<V>
    implements LoginListener<V> {

  private static final String TAG = "LoginPresenter";

  public LoginPresenter() {
  }
}
