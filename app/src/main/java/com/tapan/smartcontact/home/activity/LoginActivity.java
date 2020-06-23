package com.tapan.smartcontact.home.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.provider.AuthCallback;
import com.auth0.android.provider.WebAuthProvider;
import com.auth0.android.result.Credentials;
import com.tapan.smartcontact.R;
import com.tapan.smartcontact.core.base.activity.BaseActivity;
import com.tapan.smartcontact.home.presenter.LoginPresenter;
import com.tapan.smartcontact.home.view.LoginView;

public class LoginActivity extends BaseActivity implements LoginView {

  public static final String EXTRA_ACCESS_TOKEN = "com.auth0.ACCESS_TOKEN";
  private Auth0 auth0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setUp();
    initiateData();
  }

  private void initiateData() {
    LoginPresenter<LoginView> loginPresenter = new LoginPresenter<>();
    loginPresenter.onAttach(this);
    //loginPresenter.getCellPhoneContent();
  }

  @Override
  protected int getLayoutResource() {
    return R.layout.activity_login;
  }

  @Override
  protected void setUp() {
    auth0 = new Auth0(this);
    auth0.setOIDCConformant(true);
    findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        login();
      }
    });
  }

  private void login() {
    WebAuthProvider.login(auth0)
        .withScheme("demo")
        .withAudience(String.format("https://%s/userinfo", getString(R.string.com_auth0_domain)))
        .start(this, new AuthCallback() {
          @Override
          public void onFailure(@NonNull final Dialog dialog) {
            runOnUiThread(new Runnable() {
              @Override
              public void run() {
                dialog.show();
              }
            });
          }

          @Override
          public void onFailure(final AuthenticationException exception) {
            runOnUiThread(new Runnable() {
              @Override
              public void run() {
                Toast.makeText(LoginActivity.this, "Error: " + exception.getMessage(),
                    Toast.LENGTH_SHORT).show();
              }
            });
          }

          @Override
          public void onSuccess(@NonNull final Credentials credentials) {
            runOnUiThread(new Runnable() {
              @Override
              public void run() {
                Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                intent.putExtra(EXTRA_ACCESS_TOKEN, credentials.getAccessToken());
                startActivity(intent);
                finish();
              }
            });
          }
        });
  }
}
