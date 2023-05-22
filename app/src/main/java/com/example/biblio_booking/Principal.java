package com.example.biblio_booking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Principal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        Button button = (Button) findViewById(R.id.iniciarsesion);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OpenLogin();
            }
        });

        Button button2 = (Button) findViewById(R.id.registrarse);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OpenRegistro();
            }
        });
    }

    public void OpenLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }


    public void OpenRegistro() {
        Intent intent = new Intent(this, Registro.class);
        startActivity(intent);
    }
}