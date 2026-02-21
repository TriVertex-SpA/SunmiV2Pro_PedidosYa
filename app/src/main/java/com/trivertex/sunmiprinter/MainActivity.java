package com.trivertex.sunmiprinter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar el servicio de impresión
        SunmiPrinterHelper.initPrinterService(this);

        Button btnDemoText = findViewById(R.id.btn_demo_text);
        btnDemoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Imprimir los datos almacenados
                SunmiPrinterHelper.printStoredData(MainActivity.this);
            }
        });

        Button btnDemoImage = findViewById(R.id.btn_demo_image);
        btnDemoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cargar e imprimir la imagen de demostración
                Bitmap etiqueta = BitmapFactory.decodeResource(getResources(), R.drawable.etiqueta_publicitaria);
                if (etiqueta != null) {
                    SunmiPrinterHelper.printImage(MainActivity.this, etiqueta);
                } else {
                    Toast.makeText(MainActivity.this, "No se pudo cargar la imagen", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, ConfigMenuActivity.class));
            return true;
        } else if (id == R.id.action_about) {
            Toast.makeText(this, "App SunmiPrinter v1.0 - TriVertex SpA", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Liberar el servicio de impresión
        SunmiPrinterHelper.releasePrinterService(this);
    }
}
