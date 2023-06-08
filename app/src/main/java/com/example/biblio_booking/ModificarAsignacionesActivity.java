package com.example.biblio_booking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class ModificarAsignacionesActivity extends AppCompatActivity {

    //private Asignacion asignacion;
    Asignacion asignacion = new Asignacion( "7:30", "cub1", "2020091055", "5", "19/6/2023");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_asignaciones);
        /*Intent intent = getIntent();
        asignacion = (Asignacion) intent.getSerializableExtra("asignacion");*/
    }
}