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
    private double accumulator = 0;
    private boolean hasAccumulator = false;
    private String pendingOp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDisplay = findViewById(R.id.tvDisplay);
        swRadians = findViewById(R.id.swRadians);

        setupDigitButtons();
        setupOperatorButtons();
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

    private void setupOperatorButtons() {
        findViewById(R.id.btnAdd).setOnClickListener(v -> onOperator("+"));
        findViewById(R.id.btnSub).setOnClickListener(v -> onOperator("-"));
        findViewById(R.id.btnMul).setOnClickListener(v -> onOperator("*"));
        findViewById(R.id.btnDiv).setOnClickListener(v -> onOperator("/"));
        findViewById(R.id.btnEq).setOnClickListener(v -> onEquals());
        findViewById(R.id.btnClear).setOnClickListener(v -> onClear());
    }

    private void appendText(String t) {
        if (pendingOp == null && currentInput.isEmpty()) {
            hasAccumulator = false;
        }
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

    private void onOperator(String op) {
        if (!currentInput.isEmpty()) {
            double value = Double.parseDouble(currentInput);
            if (pendingOp != null && hasAccumulator) {
                if (!applyPending(value)) return;
            } else {
                accumulator = value;
                hasAccumulator = true;
            }
            currentInput = "";
        } else if (!hasAccumulator) {
            return;
        }
        pendingOp = op;
        tvDisplay.setText(format(accumulator));
    }

    private void onEquals() {
        if (pendingOp == null || currentInput.isEmpty()) return;
        double value = Double.parseDouble(currentInput);
        if (!applyPending(value)) return;
        pendingOp = null;
        currentInput = "";
        tvDisplay.setText(format(accumulator));
    }

    private boolean applyPending(double value) {
        switch (pendingOp) {
            case "+": accumulator += value; break;
            case "-": accumulator -= value; break;
            case "*": accumulator *= value; break;
            case "/":
                if (value == 0) {
                    showError();
                    return false;
                }
                accumulator /= value;
                break;
        }
        return true;
    }

    private void onClear() {
        currentInput = "";
        accumulator = 0;
        hasAccumulator = false;
        pendingOp = null;
        tvDisplay.setText("0");
    }

    private void showError() {
        currentInput = "";
        accumulator = 0;
        hasAccumulator = false;
        pendingOp = null;
        tvDisplay.setText("Error");
    }

    private String format(double value) {
        if (value == Math.rint(value) && Math.abs(value) < 1e15) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }
}