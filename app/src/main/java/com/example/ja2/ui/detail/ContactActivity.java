package com.example.ja2.ui.detail;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ja2.R;
import com.example.ja2.db.entity.Contact;

public class ContactActivity extends AppCompatActivity implements View.OnClickListener {

    private Contact contact = null;
    private TextView textViewTitleScreen = null;
    private ImageView imageViewRemove = null;
    private ImageView imageViewAdd= null;
    private EditText editTextUserName = null;
    private EditText editTextEmail = null;

    @SuppressLint({"NewApi", "MissingInflatedId"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        contact = getIntent().getParcelableExtra(Contact.DATA_CONTACT, Contact.class);
        textViewTitleScreen = findViewById(R.id.text_view_title_screen);
        imageViewRemove = findViewById(R.id.image_view_remove);
        imageViewAdd = findViewById(R.id.image_view_add);
        editTextUserName = findViewById(R.id.edit_text_user_name);
        editTextEmail = findViewById(R.id.edit_text_email);
        if (contact != null) {
            Log.e("Tag", "--- update: " + contact);
            textViewTitleScreen.setText(R.string.title_edit_contact_screen);
            editTextUserName.setText(contact.getName());
            editTextEmail.setText(contact.getEmail());
            imageViewRemove.setVisibility(View.VISIBLE);
            imageViewAdd.setVisibility(View.GONE);
        } else {
            Log.e("Tag", "--- create new account");
            textViewTitleScreen.setText(R.string.title_add_contact_screen);
            imageViewRemove.setVisibility(View.GONE);
            imageViewAdd.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_view_back: {
                Log.e("Tag", "--- show dialog confirm save or update data contact");
                finish();
                break;
            }
            case R.id.image_view_remove: {
                Log.e("Tag", "--- remove contact");
                break;
            }
            case R.id.image_view_add: {
                Log.e("Tag", "--- add contact");
                break;
            }
            default: {
                Log.e("Tag", "--- unknown define action");
            }
        }
    }
}
