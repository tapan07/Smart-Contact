package com.tapan.smartcontact.home.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.tapan.smartcontact.R;
import com.tapan.smartcontact.core.base.activity.BaseActivity;
import com.tapan.smartcontact.home.adapter.ContactAdapter;
import com.tapan.smartcontact.home.model.Contact;
import com.tapan.smartcontact.home.presenter.ContactPresenter;
import com.tapan.smartcontact.home.view.ContactView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static android.content.pm.PackageManager.PERMISSION_DENIED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class ContactActivity extends BaseActivity
    implements ContactView, SearchView.OnQueryTextListener,
    View.OnClickListener {

  private static final int GET_CONTACTS_PERMISSION_REQUEST = 2001;

  ContactPresenter<ContactView> mPresenter;
  private ContactAdapter mContactAdapter;
  private SearchView mSearchView;
  private RecyclerView mContactRV;
  private List<Contact> mContacts;

  private String[] appPermissions =
      { Manifest.permission.READ_CONTACTS };

  private static Map<String, String> sortByName(Map<String, String> map) {
    List<Map.Entry<String, String>> list = new LinkedList<>(map.entrySet());
    Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
      @Override
      public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
        return o1.getValue().compareToIgnoreCase(o2.getValue());
      }
    });
    Map<String, String> sortedMap = new LinkedHashMap<>();
    for (Map.Entry<String, String> entry : list) {
      sortedMap.put(entry.getKey(), entry.getValue());
    }
    return sortedMap;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mPresenter = new ContactPresenter<>();
    mPresenter.onAttach(this);
    setUp();
    setTitle("Contact");
  }

  @Override
  protected int getLayoutResource() {
    return R.layout.activity_contact;
  }

  @Override
  protected void setUp() {
    initUi();
    mContacts = new ArrayList<>();
    addListener();
  }

  @Override
  protected void onStart() {
    super.onStart();
    checkContactPermission();
  }

  private void addListener() {
    mSearchView.setOnQueryTextListener(this);
    mSearchView.setIconifiedByDefault(false);
  }

  private void checkContactPermission() {
    if (hasPermission(appPermissions[0])) {
      displayContacts();
    } else {
      requestPermissionsSafely(appPermissions, GET_CONTACTS_PERMISSION_REQUEST);
    }
  }

  private Map<String, String> loadContacts() {
    Map<String, String> map = new HashMap<>();
    String PHONE_NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
    String NAME = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
    ContentResolver cr = getContentResolver();
    Cursor cur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        new String[] { PHONE_NUMBER, NAME }, null, null, null);
    if (cur != null) {
      while (cur.moveToNext()) {
        String number = cur.getString(0);
        String name = cur.getString(1);
        if (!TextUtils.isEmpty(number) && !TextUtils.isEmpty(name)) {
          number = number.replaceAll(" ", "");
          if (!map.containsKey(number)) {
            map.put(number, name);
          }
        }
      }
      cur.close();
    }
    return map;
  }

  private void displayContacts() {
    mContacts.addAll(localContacts());
    mPresenter.getAPIContacts();
  }

  private List<Contact> localContacts() {
    List<Contact> contacts = new ArrayList<>();
    Map<String, String> fetchedContacts = loadContacts();
    Map<String, String> filteredContacts = sortByName(fetchedContacts);
    for (Map.Entry<String, String> contactEntry : filteredContacts.entrySet()) {
      Contact contact = new Contact();
      contact.setMobileNumber(((Map.Entry) contactEntry).getKey().toString());
      contact.setFirstName(((Map.Entry) contactEntry).getValue().toString());
      contacts.add(contact);
    }
    return contacts;
  }

  private void initUi() {
    mSearchView = findViewById(R.id.search);
    mContactRV = findViewById(R.id.contact_list);
  }

  @Override
  public boolean onQueryTextSubmit(String query) {
    return false;
  }

  @Override
  public boolean onQueryTextChange(String newText) {
    if (newText != null && mContactAdapter != null) {
      if (mContactAdapter.getFilter() != null) {
        mContactAdapter.getFilter().filter(newText);
      } else {
        return false;
      }
    }
    return false;
  }

  private void setUpRecyclerView(List<Contact> contacts) {
    LinearLayoutManager layoutManager =
        new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
    mContactRV.setLayoutManager(layoutManager);
    mContactAdapter = new ContactAdapter(contacts, this);
    mContactRV.setAdapter(mContactAdapter);
  }

  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.add) {
      moveToAddContactScreen();
    }
  }

  private void moveToAddContactScreen() {
    //Intent intent = new Intent(this, AddContactActivity.class);
    //startActivity(intent);
    //finish();
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    if (requestCode == GET_CONTACTS_PERMISSION_REQUEST) {
      if (permissions[0].equals(appPermissions[0])) {
        if (grantResults[0] == PERMISSION_DENIED) {
          showYesNoAlert(getResources().getString(R.string.permission_alert_dialog_title),
              getResources().getString(R.string.permission_alert_dialog_message),
              new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                  Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                      Uri.fromParts("package", ContactActivity.this.getPackageName(), null));
                  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                  ContactActivity.this.startActivity(intent);
                }
              }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                  ContactActivity.this.finish();
                  dialog.dismiss();
                }
              });
        } else if (grantResults[0] == PERMISSION_GRANTED) {
          displayContacts();
        }
      }
    }
  }

  public void showYesNoAlert(String title, String message,
      DialogInterface.OnClickListener iYesBtnClickListener,
      DialogInterface.OnClickListener iNoBtnClickListener) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle(title);
    builder.setMessage(message);
    builder.setCancelable(false);

    builder.setPositiveButton("Yes", iYesBtnClickListener);
    builder.setNegativeButton("No", iNoBtnClickListener);
    AlertDialog mAlertDialog = builder.create();
    mAlertDialog.show();
    mAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        .setTextColor(getResources().getColor(R.color.green));
    mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        .setTextColor(getResources().getColor(R.color.green));
    TextView textView = mAlertDialog.findViewById(android.R.id.message);
  }

  @Override
  public void updateContacts(List<Contact> contacts) {
    mContacts.addAll(contacts);
    setUpRecyclerView(mContacts);
  }
}
