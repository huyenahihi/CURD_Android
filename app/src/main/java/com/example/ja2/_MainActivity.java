//package com.example.ja2;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//
//public class MainActivity extends AppCompatActivity {
//
//    EditText editText;
//    Button btn;
//    TextView textView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        editText = findViewById(R.id.editText);
//        btn = findViewById(R.id.btn);
//        textView = findViewById(R.id.textview);
//
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String inputText = editText.getText().toString();
//
//// Check if the input is not empty
//                if (!inputText.isEmpty()) {
//                    try {
//                        // Try to parse the input into a double
//                        double kilos = Double.parseDouble(inputText);
//                        // Perform the conversion and display the result
//                        double pounds = makeConversion(kilos);
//                        textView.setText(String.valueOf(pounds));
//                    } catch (NumberFormatException e) {
//                        // Handle the case where the input is not a valid number
//                        textView.setText("Invalid input");
//                    }
//                } else {
//                    // Handle the case where the input is empty
//                    textView.setText("Please enter a value");
//                }
//
//            }
//        });
//    }
//
//    public double makeConversion(double kilos) {
//        // 1 kilo = 2.20462 pounds
//
//        return kilos * 2.20462;
//
//    }
//}