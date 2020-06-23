package com.tapan.smartcontact.core.network.callback;

import com.tapan.smartcontact.core.network.response.ContactPhoneResponse;
import com.tapan.smartcontact.core.network.service.PhoneContactService;
import io.reactivex.Single;

/**
 * API handler to fetch the cell phone content
 *
 * @author Tapan Rana (ttapan.rana@gmail.com)
 */
public class ContactAPIHandler {

  private String baseUrl;

  public ContactAPIHandler(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  /**
   * Retrieve the content of a web page
   *
   * @return {@link Single} result of the content
   */
  public Single<ContactPhoneResponse> getContacts() {
    PhoneContactService phoneContactService = RetrofitHandler.getRetrofitInstance(baseUrl)
        .create(PhoneContactService.class);

    return phoneContactService.getContacts();
  }
}
