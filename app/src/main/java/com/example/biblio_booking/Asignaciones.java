package com.example.biblio_booking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Asignaciones extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignaciones);

        Button button = (Button) findViewById(R.id.volver);
        button.setOnClickListener (new View.OnClickListener() {
            public void onClick(View v) {
                reOpenMainAdministrador();
            }
        });

        Button button2 = (Button) findViewById(R.id.Consultar);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OpenConsultarAsignaciones();
            }
        });

        /*Button button3 = (Button) findViewById(R.id.modificar);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OpenModificarAsignaciones();
            }
        });*/


    }
    public void reOpenMainAdministrador(){
        Intent intent = new Intent(this, MainAdministrador.class);
        startActivity(intent);
    }

    public void OpenConsultarAsignaciones() {
        Intent intent = new Intent(this, ConsultarAsignaciones.class);
        startActivity(intent);
    }

    /*public void OpenModificarAsignaciones() {
        Intent intent = new Intent(this, ModificarAsignaciones.class);
        startActivity(intent);
    }*/


}