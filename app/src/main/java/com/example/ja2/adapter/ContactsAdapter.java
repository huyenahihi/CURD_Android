package com.example.ja2.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ja2.R;
import com.example.ja2.db.entity.Contact;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> {

    // 1- Variable
    private final ArrayList<Contact> contactsList;
    private final OnItemClickListener callBack;

    //bo sung
    public ContactsAdapter(ArrayList<Contact> contacts, OnItemClickListener callBack) {
        this.contactsList = contacts;
        this.callBack = callBack;
    }

    // tạo ra đối tượng ViewHolder, trong nó chứa View hiện thị dữ liệu
    @NonNull
    @Override
    public ContactsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Nạp layout cho View biểu diễn phần tử cu the
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new MyViewHolder(itemView);
    }

    //chuyển dữ liệu phần tử vào ViewHolder
    @Override
    @SuppressLint("RecyclerView")
    public void onBindViewHolder(@NonNull ContactsAdapter.MyViewHolder holder, int positions) {
        final Contact contact = contactsList.get(holder.getAdapterPosition());
        holder.name.setText(contact.getName());
        holder.email.setText(contact.getEmail());
        // Xử lý khi nút Chi tiết được bấm
        holder.itemView.setOnClickListener(view ->
                callBack.onItemClickListener(holder.getAdapterPosition(), contact));
    }

    // cho biết số phần tử của dữ liệu
    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    public void removeItem(int position) {
        contactsList.remove(position);
        notifyItemRemoved(position);
    }

    public void updatePosition(int position, Contact contact) {
        contactsList.set(position, contact);
        notifyItemChanged(position);
    }

    public void addTheFirsItem(Contact contact) {
        contactsList.add(0, contact);
        notifyItemInserted(0);
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position, Contact contact);
    }

    // 2- ViewHolder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView email;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.email = itemView.findViewById(R.id.email);
        }
    }
}
