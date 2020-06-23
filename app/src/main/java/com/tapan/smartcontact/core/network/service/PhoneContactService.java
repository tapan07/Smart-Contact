package com.tapan.smartcontact.core.network.service;

import com.tapan.smartcontact.core.network.response.ContactPhoneResponse;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Handle the API services
 *
 * @author Tapan Rana (ttapan.rana@gmail.com)
 */
public interface PhoneContactService {

  @Headers({ "Content-Type:application/json" })
  @GET("prod/contact-list")
  Single<ContactPhoneResponse> getContacts();
}
