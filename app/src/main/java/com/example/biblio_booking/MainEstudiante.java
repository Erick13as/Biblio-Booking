package com.example.biblio_booking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainEstudiante extends AppCompatActivity {

    private User user; // Declare User variable to receive the object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_estudiante);

        // Retrieve the User object from the intent
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        Button button = findViewById(R.id.cerrarsesión);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reOpenPrincipal();
            }
        });

        Button button2 = findViewById(R.id.solicitar);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OpenSolicitarCubiculo();
            }
        });

        Button button3 = findViewById(R.id.ordenes);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OpenApartados();
            }
        });
    }

    public void OpenSolicitarCubiculo() {
        Intent intent = new Intent(this, SolicitarCubiculoActivity.class);
        intent.putExtra("user", user); // Pass the User object to the next activity
        startActivity(intent);
    }

    public void OpenApartados() {
        Intent intent = new Intent(this, ListaApartadosActivity.class);
        intent.putExtra("user", user); // Pass the User object to the next activity
        startActivity(intent);
    }

    public void reOpenPrincipal() {
        Intent intent = new Intent(this, Principal.class);
        startActivity(intent);
    }
}
