package com.example.biblio_booking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ModificarAsignacionesActivity extends AppCompatActivity {

    Asignacion asignacion = new Asignacion("7:30","Cubiculo1","5","19/6/2023");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_asignaciones);
    }
}