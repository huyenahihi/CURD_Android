package com.example.ja2.ui.detail;

import static com.example.ja2.db.entity.Parking.DATA_PARKING;
import static com.example.ja2.ui.task.TaskActivity.ADD_TASK;
import static com.example.ja2.ui.task.TaskActivity.REMOVE_TASK;
import static com.example.ja2.ui.task.TaskActivity.UPDATE_TASK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
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
import com.example.ja2.db.entity.Parking;
import com.example.ja2.db.entity.Task;
import com.example.ja2.ui.task.TaskActivity;
import com.example.ja2.ui.task.TaskAdapter;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * @noinspection deprecation
 */
public class ParkingActivity extends AppCompatActivity implements View.OnClickListener, TaskAdapter.OnItemClickListener {

    public static final String DATA_POSITION = "DATA_POSITION";
    public static final String REMOVE_PARKING = "REMOVE_PARKING";
    public static final String ADD_PARKING = "ADD_PARKING";
    public static final String UPDATE_PARKING = "UPDATE_PARKING";
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    Calendar now = Calendar.getInstance();
    DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    private Parking parking = null;
    private int position = -1;
    private TextView textViewTitleScreen = null;
    private ImageView imageViewRemove = null;
    private ImageView imageViewAdd = null;
    private EditText editTextUserName = null;
    private EditText editTextEmail = null;
    private EditText editTextDescription = null;
    private EditText editTextLocation = null;
    private EditText editTextLength = null;
    private CheckBox checkBoxAvailable = null;
    private Spinner spinner = null;
    private TextView textViewDate = null;
    private LinearLayout linearLayoutGroupTask = null;
    private RecyclerView recyclerViewTask = null;
    private TaskAdapter adapter = null;
    ActivityResultLauncher<Intent> mStartForResultTask = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            String action = intent.getAction();
            if (action.equals(ADD_TASK)) {
                Task task = intent.getParcelableExtra(Task.DATA_TASK);
                adapter.addTheFirsItem(task);
                recyclerViewTask.getLayoutManager().scrollToPosition(0);
            } else if (action.equals(REMOVE_TASK)) {
                int position = intent.getIntExtra(TaskActivity.DATA_POSITION, -1);
                adapter.removeItem(position);
            } else if (action.equals(UPDATE_TASK)) {
                int position = intent.getIntExtra(TaskActivity.DATA_POSITION, -1);
                Task task = intent.getParcelableExtra(Task.DATA_TASK);
                adapter.updatePosition(position, task);
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
        setContentView(R.layout.activity_parking);
        db = new DatabaseHelper(this);
        parking = getIntent().getParcelableExtra(DATA_PARKING);
        position = getIntent().getIntExtra(ParkingActivity.DATA_POSITION, -1);
        textViewTitleScreen = findViewById(R.id.text_view_title_screen);
        imageViewRemove = findViewById(R.id.image_view_remove);
        imageViewAdd = findViewById(R.id.image_view_add);
        editTextUserName = findViewById(R.id.edit_text_user_name);
        editTextEmail = findViewById(R.id.edit_text_email);
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
        if (parking != null) {
            Log.e("Tag", "--- update: " + parking);
            textViewTitleScreen.setText(R.string.title_edit_parking_screen);
            editTextUserName.setText(parking.getName());
            editTextEmail.setText(parking.getEmail());
            editTextDescription.setText(parking.getDescription());
            editTextLocation.setText(parking.getLocation());
            editTextLength.setText(String.valueOf(parking.getLength()));
            checkBoxAvailable.setChecked(parking.getAvailable());
            spinner.setSelection(parking.getLevel() - 1);
            Date date = new Date(parking.getDate());
            now.setTime(date);
            textViewDate.setText(dateFormat.format(date));
            imageViewRemove.setVisibility(View.VISIBLE);
            imageViewAdd.setVisibility(View.GONE);
            linearLayoutGroupTask.setVisibility(View.VISIBLE);
            ArrayList mDataTask = db.getListTask(parking.getId());
            adapter = new TaskAdapter(mDataTask, this);
            recyclerViewTask.setAdapter(adapter);
        } else {
            Log.e("Tag", "--- create new parking");
            textViewTitleScreen.setText(R.string.title_add_parking_screen);
            imageViewRemove.setVisibility(View.GONE);
            imageViewAdd.setVisibility(View.VISIBLE);
            linearLayoutGroupTask.setVisibility(View.GONE);
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
            String email = editTextEmail.getText().toString().trim();
            String description = editTextDescription.getText().toString().trim();
            String location = editTextLocation.getText().toString().trim();
            String height = editTextLength.getText().toString().trim();
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(location) || TextUtils.isEmpty(height) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(ParkingActivity.this, R.string.validate_form_input_parking, Toast.LENGTH_LONG).show();
            } else {
                parking = new Parking();
                parking.setName(name);
                parking.setEmail(email);
                parking.setDescription(description);
                parking.setLocation(location);
                parking.setLength(Double.valueOf(height));
                parking.setAvailable(checkBoxAvailable.isChecked());
                parking.setLevel(spinner.getSelectedItemPosition() + 1);
                parking.setDate(now.getTimeInMillis());
                if (position != -1) {
                    db.updateParking(parking);
                    intent.setAction(UPDATE_PARKING);
                    intent.putExtra(ParkingActivity.DATA_POSITION, position);
                    intent.putExtra(DATA_PARKING, parking);
                } else {
                    db.insertParking(parking);
                    intent.setAction(ADD_PARKING);
                    intent.putExtra(DATA_PARKING, parking);
                }
                setResult(Activity.RESULT_OK, intent);
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
                    String email = editTextEmail.getText().toString().trim();
                    String description = editTextDescription.getText().toString().trim();
                    String location = editTextLocation.getText().toString().trim();
                    String height = editTextLength.getText().toString().trim();
                    if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(location) || TextUtils.isEmpty(height) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        finish();
                    } else {
                        onBackPressed();
                    }
                }
                break;
            }
            case R.id.image_view_remove: {
                Log.e("Tag", "--- remove task");
                db.deleteParking(parking);
                Intent intent = new Intent();
                intent.setAction(REMOVE_PARKING);
                intent.putExtra(ParkingActivity.DATA_POSITION, position);
                intent.putExtra(DATA_PARKING, parking);
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
            }
            case R.id.image_view_add: {
                String name = editTextUserName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String description = editTextDescription.getText().toString().trim();
                String location = editTextLocation.getText().toString().trim();
                String height = editTextLength.getText().toString().trim();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(location) || TextUtils.isEmpty(height) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(ParkingActivity.this, R.string.validate_form_input_task, Toast.LENGTH_LONG).show();
                } else {
                    parking = new Parking();
                    parking.setName(name);
                    parking.setEmail(email);
                    parking.setDescription(description);
                    parking.setLocation(location);
                    parking.setLength(Double.valueOf(height));
                    parking.setAvailable(checkBoxAvailable.isChecked());
                    parking.setLevel(spinner.getSelectedItemPosition() + 1);
                    parking.setDate(now.getTimeInMillis());
                    long id = db.insertParking(parking);
                    Parking parking = db.getParking(id);
                    if (parking != null) {
                        Log.e("Tag", "--- add parking: " + parking);
                        Intent intent = new Intent();
                        intent.setAction(ADD_PARKING);
                        intent.putExtra(DATA_PARKING, parking);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                }
                break;
            }
            case R.id.image_view_edit: {
                Intent intent = new Intent(ParkingActivity.this, TaskActivity.class);
                Task task = new Task();
                task.setUID(parking.getId());
                intent.putExtra(Task.DATA_TASK, task);
                mStartForResultTask.launch(intent);
                break;
            }
            case R.id.text_view_date: {
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance((mView, year, monthOfYear, dayOfMonth) -> {
                    now.set(Calendar.YEAR, year);
                    now.set(Calendar.MONTH, monthOfYear);
                    now.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    textViewDate.setText(dateFormat.format(now.getTime()));
                }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show(getSupportFragmentManager(), "DatePickerDialog");
                break;
            }
            default: {
                Log.e("Tag", "--- unknown define action");
            }
        }
    }

    @Override
    public void onItemClickListener(int position, Task task) {
        Log.e("Tag", "--- navigate to edit task: " + task.getId());
        Intent intent = new Intent(ParkingActivity.this, TaskActivity.class);
        intent.putExtra(TaskActivity.DATA_POSITION, position);
        intent.putExtra(Task.DATA_TASK, task);
        mStartForResultTask.launch(intent);
    }
}
