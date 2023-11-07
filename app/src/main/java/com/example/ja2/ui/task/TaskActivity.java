package com.example.ja2.ui.task;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ja2.R;
import com.example.ja2.db.entity.Task;

public class TaskActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String DATA_POSITION = "DATA_POSITION";
    public static final String REMOVE_TASK = "REMOVE_TASK";
    public static final String ADD_TASK = "ADD_TASK";
    public static final String UPDATE_TASK = "UPDATE_TASK";

    private TextView textViewTitleScreen = null;
    private ImageView imageViewEdit = null;
    private int position = -1;
    private Task task = new Task();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        textViewTitleScreen = findViewById(R.id.text_view_title_screen);
        imageViewEdit = findViewById(R.id.image_view_edit);
        if(position == -1) {
            textViewTitleScreen.setText(R.string.title_add_task_screen);
            imageViewEdit.setSelected(true);
        } else {
            textViewTitleScreen.setText(R.string.title_update_task_screen);
            imageViewEdit.setSelected(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.image_view_back: {
                finish();
                break;
            }
            case R.id.image_view_edit: {
                break;
            }
        }
    }
}
