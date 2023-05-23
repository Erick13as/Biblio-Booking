package com.example.biblio_booking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ConsultarAsignaciones extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_asignaciones);

        Button button = (Button) findViewById(R.id.volver);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reOpenAsignaciones();
            }
        });

    }

    public void reOpenAsignaciones() {
        Intent intent = new Intent(this, Asignaciones.class);
        startActivity(intent);
    }
}