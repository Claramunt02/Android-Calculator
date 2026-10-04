package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView tvDisplay;
    private SwitchCompat swRadians;

    private String currentInput = "";
    private double accumulator = 0.0;
    private boolean hasAccumulator = false;
    private String pendingOp = null;
    private boolean replaceOnNextDigit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDisplay = findViewById(R.id.tvDisplay);
        swRadians = findViewById(R.id.swRadians);
    }

    public void ClickBtNum(View view) {
        Button button = (Button) view;
        String textButton = button.getText().toString();

        appendText(textButton);
    }

    public void ClickBtSum(View view) {
        onOperator("+");
    }

    public void ClickBtSubs(View view) {
        onOperator("-");
    }

    public void ClickBtMultip(View view) {
        onOperator("*");
    }

    public void ClickBtDiv(View view) {
        onOperator("/");
    }

    public void ClickBtIgual(View view) {
        onEquals();
    }

    public void ClickBtAC(View view) {
        onClear();
    }

    public void ClickBtSin(View view) {
        onTrig("sin");
    }

    public void ClickBtCos(View view) {
        onTrig("cos");
    }

    public void ClickBtTan(View view) {
        onTrig("tan");
    }

    private void appendText(String text) {
        if (replaceOnNextDigit) {
            currentInput = "";
            replaceOnNextDigit = false;
        }

        if (pendingOp == null && currentInput.isEmpty()) {
            hasAccumulator = false;
        }

        if (text.equals(".")) {
            if (currentInput.contains(".")) {
                return;
            }

            if (currentInput.isEmpty()) {
                currentInput = "0";
            }
        }

        if (currentInput.equals("0") && !text.equals(".")) {
            currentInput = text;
        } else {
            currentInput = currentInput + text;
        }

        tvDisplay.setText(currentInput);
    }

    private void onOperator(String operation) {
        if (!currentInput.isEmpty()) {
            double value = Double.parseDouble(currentInput);

            if (pendingOp != null && hasAccumulator) {
                if (!applyPendingOperation(value)) {
                    return;
                }
            } else {
                accumulator = value;
                hasAccumulator = true;
            }

            currentInput = "";
            replaceOnNextDigit = false;
        } else if (!hasAccumulator) {
            Toast.makeText(this, "Introduce un número", Toast.LENGTH_SHORT).show();
            return;
        }

        pendingOp = operation;
        tvDisplay.setText(formatNumber(accumulator));
    }

    private void onEquals() {
        if (pendingOp == null) {
            Toast.makeText(this, "No hay ninguna operación pendiente", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentInput.isEmpty()) {
            Toast.makeText(this, "Introduce el segundo número", Toast.LENGTH_SHORT).show();
            return;
        }

        double value = Double.parseDouble(currentInput);

        if (!applyPendingOperation(value)) {
            return;
        }

        pendingOp = null;
        currentInput = "";
        replaceOnNextDigit = true;

        tvDisplay.setText(formatNumber(accumulator));
    }

    private boolean applyPendingOperation(double value) {
        if (pendingOp.equals("+")) {
            accumulator = accumulator + value;
        } else if (pendingOp.equals("-")) {
            accumulator = accumulator - value;
        } else if (pendingOp.equals("*")) {
            accumulator = accumulator * value;
        } else if (pendingOp.equals("/")) {
            if (value == 0) {
                showError("No se puede dividir entre 0");
                return false;
            }

            accumulator = accumulator / value;
        }

        return true;
    }

    private void onTrig(String function) {
        double value;
        boolean valueComesFromInput = !currentInput.isEmpty();

        if (valueComesFromInput) {
            value = Double.parseDouble(currentInput);
        } else if (hasAccumulator && pendingOp == null) {
            value = accumulator;
        } else {
            Toast.makeText(this, "Introduce un número", Toast.LENGTH_SHORT).show();
            return;
        }

        double angle;

        if (swRadians.isChecked()) {
            angle = value;
        } else {
            angle = Math.toRadians(value);
        }

        double result;

        if (function.equals("sin")) {
            result = Math.sin(angle);
        } else if (function.equals("cos")) {
            result = Math.cos(angle);
        } else {
            if (Math.abs(Math.cos(angle)) < 0.000000000001) {
                showError("Tangente no definida");
                return;
            }

            result = Math.tan(angle);
        }

        result = Math.round(result * 10000000000.0) / 10000000000.0;

        if (valueComesFromInput) {
            currentInput = formatNumber(result);
            replaceOnNextDigit = true;
        } else {
            accumulator = result;
            replaceOnNextDigit = true;
        }

        tvDisplay.setText(formatNumber(result));
    }

    private void onClear() {
        currentInput = "";
        accumulator = 0.0;
        hasAccumulator = false;
        pendingOp = null;
        replaceOnNextDigit = false;

        tvDisplay.setText("0");
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        currentInput = "";
        accumulator = 0.0;
        hasAccumulator = false;
        pendingOp = null;
        replaceOnNextDigit = false;

        tvDisplay.setText("Error");
    }

    private String formatNumber(double value) {
        if (value == Math.rint(value) && Math.abs(value) < 1000000000000000.0) {
            return String.valueOf((long) value);
        }

        return String.valueOf(value);
    }
}