package com.example.biblio_booking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Estudiantes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estudiantes);

        Button backButton = (Button) findViewById(R.id.volver);
        backButton.setOnClickListener (new View.OnClickListener() {
            public void onClick(View v) {
                reOpenMainAdministrador();
            }
        });

        Button button2 = (Button) findViewById(R.id.eliminar);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OpenEliminarEstudiante();
            }
        });

        Button button3 = (Button) findViewById(R.id.modificar);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OpenModificarEstudiante();
            }
        });

    }
    public void reOpenMainAdministrador(){
        Intent intent = new Intent(this, MainAdministrador.class);
        startActivity(intent);
    }

    public void OpenEliminarEstudiante() {
        Intent intent = new Intent(this, EliminarEstudianteActivity.class);
        startActivity(intent);
    }
    public void OpenModificarEstudiante() {
        Intent intent = new Intent(this, ModificarEstudianteActivity.class);
        startActivity(intent);
    }

}