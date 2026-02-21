package com.trivertex.sunmiprinter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;

public class ConfigDemoTextActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "config";
    private static final String KEY_DEMO_TEXT = "demo_text";

    private TextInputEditText inputDemoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_demo_text);

        inputDemoText = findViewById(R.id.input_demo_text);
        Button btnGuardar = findViewById(R.id.btn_guardar_demo_text);
        Button btnVolver = findViewById(R.id.btn_volver);

        // Cargar datos guardados si existen
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String datosGuardados = prefs.getString(KEY_DEMO_TEXT, "");
        inputDemoText.setText(datosGuardados);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String datos = inputDemoText.getText() != null ? inputDemoText.getText().toString() : "";
                prefs.edit().putString(KEY_DEMO_TEXT, datos).apply();
                Toast.makeText(ConfigDemoTextActivity.this, "Datos guardados", Toast.LENGTH_SHORT).show();
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}