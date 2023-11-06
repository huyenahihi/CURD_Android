package com.example.ja2.ui.detail;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ja2.R;
import com.example.ja2.db.entity.Contact;

public class ContactActivity extends AppCompatActivity {

    private Contact contact = null;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        contact = getIntent().getParcelableExtra(Contact.DATA_CONTACT, Contact.class);
        if(contact != null) {
            Log.e("Tag", "--- update: " + contact);
        } else {
            Log.e("Tag", "--- create new account");
        }
    }
}
