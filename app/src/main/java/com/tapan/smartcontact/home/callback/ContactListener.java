package com.tapan.smartcontact.home.callback;

import com.tapan.smartcontact.core.base.callback.BaseListener;
import com.tapan.smartcontact.home.view.ContactView;

public interface ContactListener<V extends ContactView> extends BaseListener<V> {

  void getAPIContacts();
}
