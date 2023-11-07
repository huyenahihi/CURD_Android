package com.example.ja2.ui.main;

import static com.example.ja2.ui.detail.ParkingActivity.ADD_PARKING;
import static com.example.ja2.ui.detail.ParkingActivity.DATA_POSITION;
import static com.example.ja2.ui.detail.ParkingActivity.REMOVE_PARKING;
import static com.example.ja2.ui.detail.ParkingActivity.UPDATE_PARKING;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ja2.R;
import com.example.ja2.db.DatabaseHelper;
import com.example.ja2.db.entity.Parking;
import com.example.ja2.ui.detail.ParkingActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ParkingAdapter.OnItemClickListener {

    private final int DISPLAY_NORMAL = 0;
    private final int DISPLAY_SEARCH = 1;
    private final ArrayList<Parking> parkingArrayList = new ArrayList<>();
    private ViewFlipper viewFlipper = null;
    private EditText editTextQuery = null;
    private ParkingAdapter adapter;

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            String action = intent.getAction();
            if (action.equals(ADD_PARKING)) {
                Parking parking = intent.getParcelableExtra(Parking.DATA_PARKING);
                adapter.addTheFirsItem(parking);
                Toast.makeText(MainActivity.this, R.string.toast_message_create_contact_successful, Toast.LENGTH_LONG).show();
            } else if (action.equals(REMOVE_PARKING)) {
                int position = intent.getIntExtra(DATA_POSITION, -1);
                adapter.removeItem(position);
                Toast.makeText(MainActivity.this, R.string.toast_message_remove_contact_successful, Toast.LENGTH_LONG).show();
            } else if (action.equals(UPDATE_PARKING)) {
                int position = intent.getIntExtra(DATA_POSITION, -1);
                Parking parking = intent.getParcelableExtra(Parking.DATA_PARKING);
                adapter.updatePosition(position, parking);
                Toast.makeText(MainActivity.this, R.string.toast_message_update_contact_successful, Toast.LENGTH_LONG).show();
            }
        }
    });
    private RecyclerView recyclerView;
    private DatabaseHelper db;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewFlipper = findViewById(R.id.view_flipper);
        editTextQuery = findViewById(R.id.edit_text_query);
        editTextQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mHandler.removeCallbacksAndMessages(null);
                mHandler.postDelayed(() -> {
                    String keyword = s.toString().trim();
                    ArrayList mDataSearch = null;
                    if(!TextUtils.isEmpty(keyword)) {
                        mDataSearch = db.searchParking(keyword);
                    } else {
                        viewFlipper.setDisplayedChild(DISPLAY_NORMAL);
                        Log.e("Tag", "--- search key: " + keyword);
                        mDataSearch = db.getListParking();
                    }
                    adapter.submitData(mDataSearch);
                }, 250);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // RecyclerVIew
        recyclerView = findViewById(R.id.recycler_view_parking);
        db = new DatabaseHelper(this);
        // Contacts List
        parkingArrayList.addAll(db.getListParking());
        adapter = new ParkingAdapter(parkingArrayList, MainActivity.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ParkingActivity.class);
            mStartForResult.launch(intent);
        });
    }

    // Menu bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClickListener(int position, Parking parking) {
        Intent intent = new Intent(MainActivity.this, ParkingActivity.class);
        if (parking != null) {
            intent.putExtra(DATA_POSITION, position);
            intent.putExtra(Parking.DATA_PARKING, parking);
        }
        mStartForResult.launch(intent);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.image_view_search) {
            viewFlipper.setDisplayedChild(DISPLAY_SEARCH);
        }
    }
}