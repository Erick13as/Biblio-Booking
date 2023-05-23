package com.example.biblio_booking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainEstudiante extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_estudiante);

        Button button = (Button) findViewById(R.id.cerrarsesión);
        button.setOnClickListener (new View.OnClickListener() {
            public void onClick(View v) {
                reOpenPrincipal();
            }
        });

        Button button2 = (Button) findViewById(R.id.solicitar);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OpenSolicitarCubiculo();
            }
        });

        Button button3 = (Button) findViewById(R.id.ordenes);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OpenApartados();
            }
        });
    }

    public void OpenSolicitarCubiculo() {
        Intent intent = new Intent(this, SolicitarCubiculoActivity.class);
        startActivity(intent);
    }

    public void OpenApartados() {
        Intent intent = new Intent(this, ListaApartadosActivity.class);
        startActivity(intent);
    }
    public void reOpenPrincipal(){
        Intent intent = new Intent(this, Principal.class);
        startActivity(intent);
    }
}