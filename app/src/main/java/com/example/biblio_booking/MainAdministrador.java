package com.example.biblio_booking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainAdministrador extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_administrador);

        Button button = (Button) findViewById(R.id.cerrarsesión);
        button.setOnClickListener (new View.OnClickListener() {
            public void onClick(View v) {
                reOpenPrincipal();
            }
        });

        Button button2 = (Button) findViewById(R.id.cubiculo);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OpenCubiculo();
            }
        });

        Button button3 = (Button) findViewById(R.id.estudiante);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OpenEstudiante();
            }
        });

        Button button4 = (Button) findViewById(R.id.asignaciones);
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OpenAsignaciones();
            }
        });


    }
    public void reOpenPrincipal(){
        Intent intent = new Intent(this, Principal.class);
        startActivity(intent);
    }

    public void OpenCubiculo() {
        Intent intent = new Intent(this, Cubiculos.class);
        startActivity(intent);
    }
    public void OpenEstudiante() {
        Intent intent = new Intent(this, Estudiantes.class);
        startActivity(intent);
    }
    public void OpenAsignaciones() {
        Intent intent = new Intent(this, Asignaciones.class);
        startActivity(intent);
    }
}