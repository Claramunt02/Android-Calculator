package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class MainActivity extends AppCompatActivity {

    private TextView tvDisplay;
    private SwitchCompat swRadians;
    private String currentInput = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDisplay = findViewById(R.id.tvDisplay);
        swRadians = findViewById(R.id.swRadians);

        setupDigitButtons();
    }

    private void setupDigitButtons() {
        int[] ids = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
                R.id.btnDot
        };

        View.OnClickListener listener = v -> {
            Button b = (Button) v;
            appendText(b.getText().toString());
        };

        for (int id : ids) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    private void appendText(String t) {
        if (t.equals(".")) {
            if (currentInput.contains(".")) return;
            if (currentInput.isEmpty()) currentInput = "0";
        }
        if (currentInput.equals("0") && !t.equals(".")) {
            currentInput = t;
        } else {
            currentInput += t;
        }
        tvDisplay.setText(currentInput);
    }
}