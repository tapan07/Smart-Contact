package com.tapan.smartcontact.home.view;

import com.tapan.smartcontact.core.base.view.BaseView;
import com.tapan.smartcontact.home.model.Contact;
import java.util.List;

public interface ContactView extends BaseView {

  void updateContacts(List<Contact> contacts);
}
