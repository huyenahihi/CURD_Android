package com.example.ja2.ui.detail;

import static com.example.ja2.db.entity.Hike.DATA_HIKE;
import static com.example.ja2.ui.observation.ObservationActivity.ADD_TASK;
import static com.example.ja2.ui.observation.ObservationActivity.REMOVE_TASK;
import static com.example.ja2.ui.observation.ObservationActivity.UPDATE_TASK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ja2.R;
import com.example.ja2.db.DatabaseHelper;
import com.example.ja2.db.entity.Hike;
import com.example.ja2.db.entity.Observation;
import com.example.ja2.ui.observation.ObservationActivity;
import com.example.ja2.ui.observation.ObservationAdapter;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * @noinspection deprecation
 */
public class DetailHikeActivity extends AppCompatActivity implements View.OnClickListener, ObservationAdapter.OnItemClickListener {

    public static final String DATA_POSITION = "DATA_POSITION";
    public static final String REMOVE_HIKE = "REMOVE_HIKE";
    public static final String ADD_HIKE = "ADD_HIKE";
    public static final String UPDATE_HIKE = "UPDATE_HIKE";
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    Calendar calendar = Calendar.getInstance();
    DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    private Hike hike = new Hike();
    private int position = -1;
    private TextView textViewTitleScreen = null;
    private ImageView imageViewRemove = null;
    private ImageView imageViewAdd = null;
    private EditText editTextUserName = null;
    private EditText editTextDescription = null;
    private EditText editTextLocation = null;
    private EditText editTextLength = null;
    private CheckBox checkBoxAvailable = null;
    private Spinner spinner = null;
    private TextView textViewDate = null;
    private LinearLayout linearLayoutGroupTask = null;
    private RecyclerView recyclerViewTask = null;
    private ObservationAdapter adapter = null;
    ActivityResultLauncher<Intent> mStartForResultTask = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            String action = intent.getAction();
            if (action.equals(ADD_TASK)) {
                Observation observation = intent.getParcelableExtra(Observation.DATA_OBSERVATION);
                adapter.addTheFirsItem(observation);
                recyclerViewTask.getLayoutManager().scrollToPosition(0);
            } else if (action.equals(REMOVE_TASK)) {
                int position = intent.getIntExtra(ObservationActivity.DATA_POSITION, -1);
                adapter.removeItem(position);
            } else if (action.equals(UPDATE_TASK)) {
                int position = intent.getIntExtra(ObservationActivity.DATA_POSITION, -1);
                Observation observation = intent.getParcelableExtra(Observation.DATA_OBSERVATION);
                adapter.updatePosition(position, observation);
                recyclerViewTask.getLayoutManager().scrollToPosition(position);
            }
        }
    });
    private LevelAdapter adapterLevel = null;
    private DatabaseHelper db;

    @SuppressLint({"NewApi", "MissingInflatedId"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike);
        db = new DatabaseHelper(this);
        hike = getIntent().getParcelableExtra(DATA_HIKE);
        position = getIntent().getIntExtra(DetailHikeActivity.DATA_POSITION, -1);
        textViewTitleScreen = findViewById(R.id.text_view_title_screen);
        imageViewRemove = findViewById(R.id.image_view_remove);
        imageViewAdd = findViewById(R.id.image_view_add);
        editTextUserName = findViewById(R.id.edit_text_user_name);
        editTextDescription = findViewById(R.id.edit_text_description);
        editTextLocation = findViewById(R.id.edit_text_location);
        editTextLength = findViewById(R.id.edit_text_length);
        checkBoxAvailable = findViewById(R.id.check_box_available);
        spinner = findViewById(R.id.spinner_level);
        textViewDate = findViewById(R.id.text_view_date);
        linearLayoutGroupTask = findViewById(R.id.linear_layout_group_task);
        recyclerViewTask = findViewById(R.id.recycler_view_task);
        recyclerViewTask.setNestedScrollingEnabled(true);
        recyclerViewTask.setHasFixedSize(true);
        ArrayList mData = new ArrayList();
        mData.add(1);
        mData.add(2);
        mData.add(3);
        mData.add(4);
        adapterLevel = new LevelAdapter(mData);
        spinner.setAdapter(adapterLevel);
        if (hike != null) {
            textViewTitleScreen.setText(R.string.title_edit_hike_screen);
            editTextUserName.setText(hike.getName());
            editTextDescription.setText(hike.getDescription());
            editTextLocation.setText(hike.getLocation());
            editTextLength.setText(String.valueOf(hike.getLength()));
            checkBoxAvailable.setChecked(hike.getAvailable());
            spinner.setSelection(hike.getLevel() - 1);
            Date date = new Date(hike.getDate());
            calendar.setTime(date);
            textViewDate.setText(dateFormat.format(date));
            imageViewRemove.setVisibility(View.VISIBLE);
            imageViewAdd.setVisibility(View.GONE);
            linearLayoutGroupTask.setVisibility(View.VISIBLE);
            ArrayList mDataTask = db.getListObservation(hike.getId());
            adapter = new ObservationAdapter(mDataTask, this);
            recyclerViewTask.setAdapter(adapter);
        } else {
            Log.e("Tag", "--- create new hike");
            textViewTitleScreen.setText(R.string.title_add_hike_screen);
            imageViewRemove.setVisibility(View.GONE);
            imageViewAdd.setVisibility(View.VISIBLE);
            linearLayoutGroupTask.setVisibility(View.GONE); //ẩn toàn bộ
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_dialog_confirm);
        builder.setMessage(R.string.message_dialog_confirm);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.button_confirm, (dialog, which) -> {
            Intent intent = new Intent();
            String name = editTextUserName.getText().toString().trim();
            String description = editTextDescription.getText().toString().trim();
            String location = editTextLocation.getText().toString().trim();
            String height = editTextLength.getText().toString().trim();
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(location) || TextUtils.isEmpty(height)) {
                Toast.makeText(DetailHikeActivity.this, R.string.validate_form_input_hike, Toast.LENGTH_LONG).show();
            } else {
                hike.setName(name);
                hike.setDescription(description);
                hike.setLocation(location);
                hike.setLength(Double.valueOf(height));
                hike.setAvailable(checkBoxAvailable.isChecked());
                hike.setLevel(spinner.getSelectedItemPosition() + 1);
                hike.setDate(calendar.getTimeInMillis());
                if (position != -1) { //nếu position khác -1-> thực hiện lệnh update
                    int id = db.updateHike(hike);
                    intent.setAction(UPDATE_HIKE);
                    intent.putExtra(DetailHikeActivity.DATA_POSITION, position);
                    intent.putExtra(DATA_HIKE, hike);
                } else {
                    db.insertHike(hike);
                    intent.setAction(ADD_HIKE);
                    intent.putExtra(DATA_HIKE, hike);
                }
                setResult(Activity.RESULT_OK, intent); //=> Báo về màn trước để check
                finish();
                super.onBackPressed();
            }
        });
        builder.setNegativeButton(R.string.button_cancel, (dialog, which) -> {
            finish();
            dialog.dismiss();
        });
        builder.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_view_back: {
                if (position != -1) {
                    onBackPressed();
                } else {
                    String name = editTextUserName.getText().toString().trim();
                    String description = editTextDescription.getText().toString().trim();
                    String location = editTextLocation.getText().toString().trim();
                    String height = editTextLength.getText().toString().trim();
                    if (TextUtils.isEmpty(name) || TextUtils.isEmpty(location) || TextUtils.isEmpty(height)) {
                        finish();
                    } else {
                        onBackPressed();
                    }
                }
                break;
            }
            case R.id.image_view_remove: {
                Log.e("Tag", "--- remove task");
                db.deleteHike(hike);
                Intent intent = new Intent();
                intent.setAction(REMOVE_HIKE);
                intent.putExtra(DetailHikeActivity.DATA_POSITION, position);
                intent.putExtra(DATA_HIKE, hike);
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
            }
            case R.id.image_view_add: {
                String name = editTextUserName.getText().toString().trim();
                String description = editTextDescription.getText().toString().trim();
                String location = editTextLocation.getText().toString().trim();
                String height = editTextLength.getText().toString().trim();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(location) || TextUtils.isEmpty(height)) {
                    Toast.makeText(DetailHikeActivity.this, R.string.validate_form_input_hike, Toast.LENGTH_LONG).show();
                } else {
                    this.hike = new Hike();
                    this.hike.setName(name);
                    this.hike.setDescription(description);
                    this.hike.setLocation(location);
                    this.hike.setLength(Double.valueOf(height));
                    this.hike.setAvailable(checkBoxAvailable.isChecked());
                    this.hike.setLevel(spinner.getSelectedItemPosition() + 1);
                    this.hike.setDate(calendar.getTimeInMillis());
                    long id = db.insertHike(this.hike);
                    Hike hike = db.getHike(id);
                    if (hike != null) {
                        Log.e("Tag", "--- add hike: " + hike);
                        Intent intent = new Intent();
                        intent.setAction(ADD_HIKE);
                        intent.putExtra(DATA_HIKE, hike);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                }
                break;
            }
            case R.id.image_view_edit: {
                Intent intent = new Intent(DetailHikeActivity.this, ObservationActivity.class);
                Observation observation = new Observation();
                observation.setUID(hike.getId());
                intent.putExtra(Observation.DATA_OBSERVATION, observation);
                mStartForResultTask.launch(intent);
                break;
            }
            case R.id.text_view_date: {
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance((mView, year, monthOfYear, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    textViewDate.setText(dateFormat.format(calendar.getTime()));
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show(getSupportFragmentManager(), "DatePickerDialog");
                break;
            }
            default: {
                Log.e("Tag", "--- unknown define action");
            }
        }
    }

    @Override
    public void onItemClickListener(int position, Observation observation) {
        Log.e("Tag", "--- navigate to edit task: " + observation.getId());
        Intent intent = new Intent(DetailHikeActivity.this, ObservationActivity.class);
        intent.putExtra(ObservationActivity.DATA_POSITION, position);
        intent.putExtra(Observation.DATA_OBSERVATION, observation);
        mStartForResultTask.launch(intent);
    }
}
