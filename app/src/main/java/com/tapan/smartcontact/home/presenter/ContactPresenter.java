package com.tapan.smartcontact.home.presenter;

import com.tapan.smartcontact.R;
import com.tapan.smartcontact.core.base.presenter.BasePresenter;
import com.tapan.smartcontact.core.network.callback.ContactAPIHandler;
import com.tapan.smartcontact.core.network.response.ContactPhoneResponse;
import com.tapan.smartcontact.core.rx.BaseObserver;
import com.tapan.smartcontact.core.util.CoreConstants;
import com.tapan.smartcontact.home.callback.ContactListener;
import com.tapan.smartcontact.home.view.ContactView;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.schedulers.Schedulers;

public class ContactPresenter<V extends ContactView> extends BasePresenter<V>
    implements ContactListener<V> {

  public ContactPresenter() {
  }

  @Override
  public void getAPIContacts() {
    if (getView() != null) {
      if (getView().isNetworkConnected()) {
        processAPIContacts();
      } else {
        getView().showError(R.string.msg_network_error);
      }
    }
  }

  private void processAPIContacts() {
    getView().showLoading();
    ContactAPIHandler contactAPIHandler = new ContactAPIHandler(CoreConstants.CONTACT_URL);
    contactRequest(contactAPIHandler);
  }

  private void contactRequest(ContactAPIHandler contactAPIHandler) {
    contactAPIHandler.getContacts()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(contactObserver());
  }

  private SingleObserver<? super ContactPhoneResponse> contactObserver() {
    return new BaseObserver<ContactPhoneResponse>() {
      @Override
      public void onSuccess(ContactPhoneResponse response) {
        getView().hideLoading();
        lazySet(DisposableHelper.DISPOSED);
        getView().updateContacts(response.getContacts());
      }

      @Override
      public void onError(Throwable ex) {
        getView().hideLoading();
      }
    };
  }
}
