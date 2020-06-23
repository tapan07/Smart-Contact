package com.tapan.smartcontact.home.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.tapan.smartcontact.R;
import com.tapan.smartcontact.home.model.Contact;
import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder>
    implements Filterable {

  private List<Contact> mContactList;
  private List<Contact> mContactListFiltered;
  private Context mContext;

  public ContactAdapter(List<Contact> contactList, Context context) {
    mContext = context;
    mContactList = contactList;
    mContactListFiltered = mContactList;
  }

  @NonNull
  @Override
  public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.contact_list_adapter, parent, false);
    return new ContactViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
    setData(holder, position);
  }

  private void setData(@NonNull ContactViewHolder holder, int position) {
    final Contact response = mContactListFiltered.get(position);

    String customerName = response.getFirstName().trim();
    if (!TextUtils.isEmpty(customerName)) {
      holder.nameTv.setText(customerName);
    } else {
      holder.nameTv.setText("");
    }
    if (!TextUtils.isEmpty(response.getMobileNumber())) {
      holder.numberTv.setText(response.getMobileNumber());
    } else {
      holder.numberTv.setText("");
    }

    holder.card.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

      }
    });
  }

  @Override
  public int getItemCount() {
    return mContactListFiltered.size();
  }

  @Override
  public Filter getFilter() {
    return new Filter() {

      @Override
      protected FilterResults performFiltering(CharSequence constraint) {
        String charString = constraint.toString();
        if (charString.isEmpty()) {
          mContactListFiltered.clear();
          mContactListFiltered.addAll(mContactList);
        } else {
          List<Contact> filteredList = new ArrayList<>();
          for (Contact row : mContactList) {
            if (row.getFirstName().toLowerCase().contains(charString.toLowerCase())
                || row.getMobileNumber().contains(charString)) {
              filteredList.add(row);
            }
          }
          mContactListFiltered = filteredList;
        }
        FilterResults filterResults = new FilterResults();
        filterResults.values = mContactListFiltered;
        return filterResults;
      }

      @Override
      protected void publishResults(CharSequence constraint, FilterResults results) {
        mContactListFiltered = (List<Contact>) results.values;
        notifyDataSetChanged();
      }
    };
  }

  static class ContactViewHolder extends RecyclerView.ViewHolder {

    private CardView card;
    private AppCompatTextView nameTv, numberTv, contactInitialTv;

    ContactViewHolder(View itemView) {
      super(itemView);
      card = itemView.findViewById(R.id.contact_cv);
      nameTv = itemView.findViewById(R.id.contact_name);
      numberTv = itemView.findViewById(R.id.contact_number);
    }
  }
}
