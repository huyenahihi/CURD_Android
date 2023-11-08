package com.example.ja2.ui.main;

import static com.example.ja2.ui.detail.DetailHikeActivity.ADD_HIKE;
import static com.example.ja2.ui.detail.DetailHikeActivity.DATA_POSITION;
import static com.example.ja2.ui.detail.DetailHikeActivity.REMOVE_HIKE;
import static com.example.ja2.ui.detail.DetailHikeActivity.UPDATE_HIKE;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ja2.R;
import com.example.ja2.db.DatabaseHelper;
import com.example.ja2.db.entity.Hike;
import com.example.ja2.ui.detail.DetailHikeActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ParkingAdapter.OnItemClickListener {

    private final int DISPLAY_NORMAL = 0;
    private final int DISPLAY_SEARCH = 1;
    private final ArrayList<Hike> hikeArrayList = new ArrayList<>();
    private ViewFlipper viewFlipper = null;
    private EditText editTextQuery = null;
    private ParkingAdapter adapter;
    private RecyclerView recyclerView;
    //Màn nhận kết quả khi màn detailparking được add/edit/delete thành công dữ liệu
    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            String action = intent.getAction();
            if (action.equals(ADD_HIKE)) {
                Hike hike = intent.getParcelableExtra(Hike.DATA_HIKE);
                adapter.addParking(0, hike);
                recyclerView.getLayoutManager().scrollToPosition(0);
                Toast.makeText(MainActivity.this, R.string.toast_message_create_hike_successful, Toast.LENGTH_LONG).show();
            } else if (action.equals(REMOVE_HIKE)) {
                int position = intent.getIntExtra(DATA_POSITION, -1);
                adapter.removeItem(position);
                Toast.makeText(MainActivity.this, R.string.toast_message_remove_hike_successful, Toast.LENGTH_LONG).show();
            } else if (action.equals(UPDATE_HIKE)) {
                int position = intent.getIntExtra(DATA_POSITION, -1);
                Hike hike = intent.getParcelableExtra(Hike.DATA_HIKE);
                adapter.updatePosition(position, hike);
                Toast.makeText(MainActivity.this, R.string.toast_message_update_hike_successful, Toast.LENGTH_LONG).show();
            }
        }
    });
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
                        Log.e("Tag", "--- search key: " + keyword);
                        mDataSearch = db.searchHike(keyword);
                    } else {
                        viewFlipper.setDisplayedChild(DISPLAY_NORMAL);
                        mDataSearch = db.getListHike();
                        editTextQuery.clearFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(editTextQuery.getWindowToken(), 0);
                    }
                    adapter.submitData(mDataSearch);
                }, 250);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        recyclerView = findViewById(R.id.recycler_view_parking);
        db = new DatabaseHelper(this);
        hikeArrayList.addAll(db.getListHike());
        //gắn dữ liệu để đưa lên listview
        adapter = new ParkingAdapter(hikeArrayList, MainActivity.this);
        recyclerView.setItemAnimator(new DefaultItemAnimator()); //hiển thị animation
        recyclerView.setAdapter(adapter); //
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, DetailHikeActivity.class);
            mStartForResult.launch(intent);
        });
    }

//    // Menu bar
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onItemClickListener(int position, Hike hike) {
        Intent intent = new Intent(MainActivity.this, DetailHikeActivity.class);
        if (hike != null) {
            intent.putExtra(DATA_POSITION, position);
            intent.putExtra(Hike.DATA_HIKE, hike);
        }
        mStartForResult.launch(intent);
    }

    @Override
    public void onClick(View view) { //display search ở màn Main
        if (view.getId() == R.id.image_view_search) {
            editTextQuery.setFocusable(true);
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            editTextQuery.requestFocus();
            imm.showSoftInput(editTextQuery, InputMethodManager.SHOW_IMPLICIT);
            viewFlipper.setDisplayedChild(DISPLAY_SEARCH);
        }
    }
}