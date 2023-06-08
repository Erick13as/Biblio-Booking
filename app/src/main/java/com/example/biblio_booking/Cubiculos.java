package com.example.biblio_booking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Cubiculos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cubiculos);
        Button backButton = (Button) findViewById(R.id.volver);
        backButton.setOnClickListener (new View.OnClickListener() {
            public void onClick(View v) {
                reOpenMainAdministrador();
            }
        });

        Button button2 = (Button) findViewById(R.id.eliminar);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OpenEliminarCubiculo();
            }
        });

        Button button3 = (Button) findViewById(R.id.modificar);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OpenModificarCubiculo();
            }
        });

        Button button4 = (Button) findViewById(R.id.agregar);
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OpenAgregarCubiculo();
            }
        });

    }
    public void reOpenMainAdministrador(){
        Intent intent = new Intent(this, MainAdministrador.class);
        startActivity(intent);
    }

    public void OpenEliminarCubiculo() {
        Intent intent = new Intent(this, EliminarCubiculo.class);
        startActivity(intent);
    }
    public void OpenModificarCubiculo() {
        Intent intent = new Intent(this, ModificarCubiculoActivity.class);
        startActivity(intent);
    }
    public void OpenAgregarCubiculo() {
        Intent intent = new Intent(this, AgregarCubiculoActivity.class);
        startActivity(intent);
    }

}