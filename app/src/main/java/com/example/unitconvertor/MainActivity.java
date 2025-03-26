package com.example.unitconvertor;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private Spinner categorySpinner, sourceUnitSpinner, destinationUnitSpinner;
    private EditText inputValueEditText;
    private Button convertButton;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupSpinners();
        setupConvertButton();
    }

    private void initViews() {
        categorySpinner = findViewById(R.id.unitType);
        sourceUnitSpinner = findViewById(R.id.sourceUnit);
        destinationUnitSpinner = findViewById(R.id.destinationUnit);
        inputValueEditText = findViewById(R.id.inputValue);
        convertButton = findViewById(R.id.convertButton);
        resultTextView = findViewById(R.id.resultText);
    }

    private void setupSpinners() {

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = parent.getItemAtPosition(position).toString();
                setupUnitSpinners(selectedCategory);
                resultTextView.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sourceUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                resultTextView.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        destinationUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                resultTextView.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupUnitSpinners(String category) {
        ArrayAdapter<CharSequence> unitAdapter;

        switch (category) {
            case "Weight":
                unitAdapter = ArrayAdapter.createFromResource(this,
                        R.array.weight_units, android.R.layout.simple_spinner_item);
                break;
            case "Temperature":
                unitAdapter = ArrayAdapter.createFromResource(this,
                        R.array.temperature_units, android.R.layout.simple_spinner_item);
                break;
            default:
                unitAdapter = ArrayAdapter.createFromResource(this,
                        R.array.length_units, android.R.layout.simple_spinner_item);
                break;
        }

        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sourceUnitSpinner.setAdapter(unitAdapter);
        destinationUnitSpinner.setAdapter(unitAdapter);
    }

    private void setupConvertButton() {
        convertButton.setOnClickListener(v -> {
            String inputText = inputValueEditText.getText().toString();

            if (inputText.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter a value", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double inputValue = Double.parseDouble(inputText);
                String category = categorySpinner.getSelectedItem().toString();
                String sourceUnit = sourceUnitSpinner.getSelectedItem().toString();
                String destUnit = destinationUnitSpinner.getSelectedItem().toString();

                if(sourceUnit.equals(destUnit)){
                    Toast.makeText(MainActivity.this,
                            "Source and destination units are the same. Select different units for conversion.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                double result = performConversion(inputValue, category, sourceUnit, destUnit);
                DecimalFormat df = new DecimalFormat("#.####");
                resultTextView.setText(getString(R.string.result) + " " +
                        df.format(inputValue) + " " + sourceUnit + " = " +
                        df.format(result) + " " + destUnit);
            } catch (NumberFormatException e) {
                Toast.makeText(MainActivity.this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private double performConversion(double value, String category, String fromUnit, String toUnit) {
        if (fromUnit.equals(toUnit)) {
            return value;
        }

        switch (category) {
            case "Length":
                return convertLength(value, fromUnit, toUnit);
            case "Weight":
                return convertWeight(value, fromUnit, toUnit);
            case "Temperature":
                return convertTemperature(value, fromUnit, toUnit);
            default:
                return value;
        }
    }

    private double convertLength(double value, String fromUnit, String toUnit) {
        double cmValue;

        switch (fromUnit) {
            case "Inches":
                cmValue = value * 2.54;
                break;
            case "Feet":
                cmValue = value * 30.48;
                break;
            case "Yards":
                cmValue = value * 91.44;
                break;
            case "Miles":
                cmValue = value * 160934;
                break;
            case "Centimeters":
                cmValue = value;
                break;
            case "Meters":
                cmValue = value * 100;
                break;
            case "Kilometers":
                cmValue = value * 100000;
                break;
            default:
                return value;
        }

        switch (toUnit) {
            case "Inches":
                return cmValue / 2.54;
            case "Feet":
                return cmValue / 30.48;
            case "Yards":
                return cmValue / 91.44;
            case "Miles":
                return cmValue / 160934;
            case "Centimeters":
                return cmValue;
            case "Meters":
                return cmValue / 100;
            case "Kilometers":
                return cmValue / 100000;
            default:
                return value;
        }
    }

    private double convertWeight(double value, String fromUnit, String toUnit) {
        double gValue;

        switch (fromUnit) {
            case "Pounds":
                gValue = value * 453.592;
                break;
            case "Ounces":
                gValue = value * 28.3495;
                break;
            case "Tons":
                gValue = value * 907185;
                break;
            case "Kilograms":
                gValue = value * 1000;
                break;
            case "Grams":
                gValue = value;
                break;
            default:
                return value;
        }

        switch (toUnit) {
            case "Pounds":
                return gValue / 453.592;
            case "Ounces":
                return gValue / 28.3495;
            case "Tons":
                return gValue / 907185;
            case "Kilograms":
                return gValue / 1000;
            case "Grams":
                return gValue;
            default:
                return value;
        }
    }

    private double convertTemperature(double value, String fromUnit, String toUnit) {
        if (fromUnit.equals("Celsius") && toUnit.equals("Fahrenheit")) {
            return (value * 1.8) + 32;
        } else if (fromUnit.equals("Fahrenheit") && toUnit.equals("Celsius")) {
            return (value - 32) / 1.8;
        } else if (fromUnit.equals("Celsius") && toUnit.equals("Kelvin")) {
            return value + 273.15;
        } else if (fromUnit.equals("Kelvin") && toUnit.equals("Celsius")) {
            return value - 273.15;
        } else if (fromUnit.equals("Fahrenheit") && toUnit.equals("Kelvin")) {
            double celsius = (value - 32) / 1.8;
            return celsius + 273.15;
        } else if (fromUnit.equals("Kelvin") && toUnit.equals("Fahrenheit")) {
            double celsius = value - 273.15;
            return (celsius * 1.8) + 32;
        }

        return value;
    }
}