package com.trivertex.sunmiprinter;

import android.content.SharedPreferences;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;
import com.sunmi.peripheral.printer.InnerPrinterCallback;
import com.sunmi.peripheral.printer.InnerPrinterException;
import com.sunmi.peripheral.printer.InnerPrinterManager;
import com.sunmi.peripheral.printer.SunmiPrinterService;

public class SunmiPrinterHelper {
    private static SunmiPrinterService printerService;
    private static InnerPrinterCallback printerCallback; // Guardar referencia

    // Inicializar la conexión con la impresora interna
    public static void initPrinterService(Context context) {
        try {
            printerCallback = new InnerPrinterCallback() {
                @Override
                public void onConnected(SunmiPrinterService service) {
                    printerService = service;
                }

                @Override
                public void onDisconnected() {
                    printerService = null;
                    Toast.makeText(context, "Impresora desconectada", Toast.LENGTH_SHORT).show();
                }
            };
            InnerPrinterManager.getInstance().bindService(context, printerCallback);
        } catch (InnerPrinterException e) {
            Toast.makeText(context, "Error al conectar con la impresora: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Metodo para imprimir texto
    public static void printText(Context context, String text) {
        try {
            if (printerService != null) {
                // Establecer la densidad al 130%
                // key=1 (densidad), value=3 (130%)
                printerService.setPrinterStyle(1, 3);
                printerService.printText(text + "\n", null);
                printerService.lineWrap(3, null);
            } else {
                Toast.makeText(context, "Servicio de impresora no disponible", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "Error al imprimir texto: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Metodo para imprimir una imagen
    public static void printImage(Context context, Bitmap bitmap) {
        try {
            if (printerService != null) {
                // Redimensionar la imagen al ancho de 384px (58mm)
                int targetWidth = 384;
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                if (width != targetWidth) {
                    float ratio = (float) targetWidth / width;
                    int targetHeight = Math.round(height * ratio);
                    bitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);
                }
                printerService.printBitmap(bitmap, null);
                printerService.lineWrap(3, null);
            } else {
                Toast.makeText(context, "Servicio de impresora no disponible", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "Error al imprimir imagen: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Metodo para imprimir los datos almacenados de demostración
    public static void printStoredData(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        String datos = prefs.getString("demo_text", "");
        if (datos == null || datos.trim().isEmpty()) {
            Toast.makeText(context, "No hay datos de demostración guardados", Toast.LENGTH_SHORT).show();
            return;
        }
        printText(context, datos);
    }

    // Liberar el servicio de la impresora
    public static void releasePrinterService(Context context) {
        try {
            if (printerCallback != null) {
                InnerPrinterManager.getInstance().unBindService(context, printerCallback);
                printerCallback = null;
            }
        } catch (InnerPrinterException e) {
            Toast.makeText(context, "Error al liberar la impresora: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
