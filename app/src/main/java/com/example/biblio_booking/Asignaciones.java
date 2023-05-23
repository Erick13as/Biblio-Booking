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



    }
    public void reOpenMainAdministrador(){
        Intent intent = new Intent(this, MainAdministrador.class);
        startActivity(intent);
    }


}