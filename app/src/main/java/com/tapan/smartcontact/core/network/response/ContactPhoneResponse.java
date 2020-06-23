package com.tapan.smartcontact.core.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tapan.smartcontact.home.model.Contact;
import java.util.List;

public class ContactPhoneResponse {

  @SerializedName("data")
  @Expose
  private List<Contact> contacts;

  public List<Contact> getContacts() {
    return contacts;
  }

  public void setContacts(List<Contact> contacts) {
    this.contacts = contacts;
  }
}
