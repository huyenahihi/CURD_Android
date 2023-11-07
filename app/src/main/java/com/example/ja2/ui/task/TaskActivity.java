package com.example.ja2.ui.task;

import static com.example.ja2.db.entity.Task.DATA_TASK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ja2.R;
import com.example.ja2.db.DatabaseHelper;
import com.example.ja2.db.entity.Task;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/** @noinspection ALL*/
public class TaskActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String DATA_POSITION = "DATA_POSITION";
    public static final String REMOVE_TASK = "REMOVE_TASK";
    public static final String ADD_TASK = "ADD_TASK";
    public static final String UPDATE_TASK = "UPDATE_TASK";
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String TIME_FORMAT = "HH:ss";
    Calendar now = Calendar.getInstance();
    DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    DateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
    private int position = -1;
    private TextView textViewTitleScreen = null;
    private ImageView imageViewEdit = null;
    private ImageView imageViewDate = null;
    private ImageView imageViewTime = null;
    private EditText editTextDate = null;
    private EditText editTextTime = null;
    private EditText editTextNote = null;
    private Button buttonSave = null;
    private Task task = null;
    private DatabaseHelper db;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        textViewTitleScreen = findViewById(R.id.text_view_title_screen);
        imageViewEdit = findViewById(R.id.image_view_edit);
        imageViewDate = findViewById(R.id.image_view_date);
        imageViewTime = findViewById(R.id.image_view_time);
        editTextDate = findViewById(R.id.edit_text_date);
        editTextTime = findViewById(R.id.edit_text_time);
        editTextNote = findViewById(R.id.edit_text_note);
        buttonSave = findViewById(R.id.button_save);
        position = getIntent().getIntExtra(DATA_POSITION, -1);
        task = getIntent().getParcelableExtra(DATA_TASK, Task.class);
        Log.e("Tag", "--- task: " + task.toString());
        db = new DatabaseHelper(this);
        if (position == -1) {
            textViewTitleScreen.setText(R.string.title_add_task_screen);
            imageViewEdit.setSelected(true);
            imageViewDate.setEnabled(true);
            imageViewTime.setEnabled(true);
            buttonSave.setEnabled(true);
            editTextNote.setEnabled(true);
        } else {
            textViewTitleScreen.setText(R.string.title_update_task_screen);
            imageViewEdit.setSelected(false);
            imageViewDate.setEnabled(false);
            imageViewTime.setEnabled(false);
            editTextNote.setEnabled(false);
            buttonSave.setEnabled(false);
            now.setTime(new Date(task.getDateTime()));
            editTextDate.setText(dateFormat.format(now.getTime()));
            editTextTime.setText(timeFormat.format(now.getTime()));
            editTextNote.setText(task.getNote());
            editTextNote.setEnabled(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_view_back: {
                finish();
                break;
            }
            case R.id.image_view_edit: {
                Boolean isEnableEdit = !imageViewEdit.isSelected();
                Log.e("Tag", "--- status: " + isEnableEdit);
                imageViewEdit.setSelected(isEnableEdit);
                if (isEnableEdit) {
                    imageViewEdit.setSelected(true);
                    imageViewDate.setEnabled(true);
                    imageViewTime.setEnabled(true);
                    buttonSave.setEnabled(true);
                    editTextNote.setEnabled(true);
                } else {
                    imageViewEdit.setSelected(false);
                    imageViewDate.setEnabled(false);
                    imageViewTime.setEnabled(false);
                    editTextNote.setEnabled(false);
                    buttonSave.setEnabled(false);
                    editTextNote.setEnabled(false);
                }
                break;
            }
            case R.id.image_view_date: {
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance((mView, year, monthOfYear, dayOfMonth) -> {
                    now.set(Calendar.YEAR, year);
                    now.set(Calendar.MONTH, monthOfYear);
                    now.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    editTextDate.setText(dateFormat.format(now.getTime()));
                }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show(getSupportFragmentManager(), "DatePickerDialog");
                break;
            }
            case R.id.image_view_time: {
                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance((TimePickerDialog.OnTimeSetListener) (mView, hourOfDay, minute, second) -> {
                    now.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    now.set(Calendar.MINUTE, minute);
                    now.set(Calendar.SECOND, second);
                    editTextTime.setText(timeFormat.format(now.getTime()));
                }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true);
                timePickerDialog.show(getSupportFragmentManager(), "TimePickerDialog");
                break;
            }
            case R.id.button_save: {
                String date = editTextDate.getText().toString().trim();
                String time = editTextTime.getText().toString().trim();
                String note = editTextNote.getText().toString().trim();
                if (!TextUtils.isEmpty(date) && !TextUtils.isEmpty(time) && !TextUtils.isEmpty(note)) {
                    Intent intent = new Intent();
                    if (position == -1) {
                        task.setDateTime(now.getTimeInMillis());
                        task.setNote(note);
                        db.insertTask(task);
                        intent.setAction(ADD_TASK);
                        intent.putExtra(DATA_TASK, task);
                    } else {
                        task.setDateTime(now.getTimeInMillis());
                        task.setNote(note);
                        db.updateTask(task);
                        intent.setAction(UPDATE_TASK);
                        intent.putExtra(DATA_POSITION, position);
                        intent.putExtra(DATA_TASK, task);
                    }
                    setResult(Activity.RESULT_OK, intent);
                    onBackPressed();
                } else {
                    Toast.makeText(TaskActivity.this, R.string.validate_form_input_task, Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }
}
