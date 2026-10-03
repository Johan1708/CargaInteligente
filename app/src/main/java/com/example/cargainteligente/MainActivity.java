package com.example.cargainteligente;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView; // Faltaba importar TextView

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen; // Importación correcta de SplashScreen para AndroidX

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    // Se eliminó la basura de texto copiada ("2 usages", "InitialValue: false")
    private final AtomicBoolean cargaInicialTerminada = new AtomicBoolean(false);

    // Se corrigió la inicialización del ExecutorService
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private View panelContenido;
    private View panelCarga;
    private TextView txtEstado;
    private Button btnActualizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // La API de SplashScreen debe llamarse antes de super.onCreate
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);

        // Se corrige la condición para mantener la SplashScreen
        splashScreen.setKeepOnScreenCondition(() -> !cargaInicialTerminada.get());

        setContentView(R.layout.activity_main);

        panelContenido = findViewById(R.id.panelContenido);
        panelCarga = findViewById(R.id.panelCarga);
        txtEstado = findViewById(R.id.txtEstado);
        btnActualizar = findViewById(R.id.btnActualizar);

        // Sintaxis lambda correcta en Java para el OnClickListener
        btnActualizar.setOnClickListener(v -> actualizarDatos());

        prepararDatosIniciales();
    }

    private void prepararDatosIniciales() {
        executor.execute(() -> {
            // Se corrigió la sintaxis del delay y el método esperado
            if (!esperarSoloParaDemostracion(900)) {
                return;
            }

            runOnUiThread(() -> {
                // Era isFinishing(), no isFishing()
                if (isFinishing() || isDestroyed()) {
                    return;
                }

                txtEstado.setText(R.string.mensaje_inicial);
                cargaInicialTerminada.set(true);
            });
        });
    }

    // Faltaban estos dos métodos para que el código compile:

    private boolean esperarSoloParaDemostracion(long milisegundos) {
        try {
            Thread.sleep(milisegundos);
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }

    private void actualizarDatos() {
        // Aquí puedes agregar la lógica para actualizar tus datos cuando el usuario presione el botón
        txtEstado.setText("Actualizando datos...");

        // Simulación básica de actualización
        panelContenido.setVisibility(View.GONE);
        panelCarga.setVisibility(View.VISIBLE);

        executor.execute(() -> {
            esperarSoloParaDemostracion(1500); // Simulamos carga de 1.5s

            runOnUiThread(() -> {
                if (isFinishing() || isDestroyed()) return;

                txtEstado.setText("Datos actualizados exitosamente");
                panelCarga.setVisibility(View.GONE);
                panelContenido.setVisibility(View.VISIBLE);
            });
        });
    }
}