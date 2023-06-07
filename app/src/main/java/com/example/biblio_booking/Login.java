package com.example.biblio_booking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class Login extends AppCompatActivity {

    private TextInputEditText correoEditText;
    private TextInputEditText contraseñaEditText;
    private Button ingresarButton;

    private List<String> infoEstudiante;
    private FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        correoEditText = findViewById(R.id.correo);
        contraseñaEditText = findViewById(R.id.contraseña);
        ingresarButton = findViewById(R.id.Ingresar);

        db = FirebaseFirestore.getInstance();

        Button backButton = findViewById(R.id.volver);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reOpenPrincipal();
            }
        });

        ingresarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateUsername() && validatePassword()) {
                    String correo = correoEditText.getText().toString();
                    String contraseña = contraseñaEditText.getText().toString();
                    checkUser(correo, contraseña);
                }
            }
        });
    }

    public boolean validateUsername() {
        String val = correoEditText.getText().toString();
        if (val.isEmpty()) {
            correoEditText.setError("Debe ingresar su correo");
            return false;
        } else {
            correoEditText.setError(null);
            return true;
        }
    }

    public boolean validatePassword() {
        String val = contraseñaEditText.getText().toString();
        if (val.isEmpty()) {
            contraseñaEditText.setError("Debe ingresar su contraseña");
            return false;
        } else {
            contraseñaEditText.setError(null);
            return true;
        }
    }

    public void reOpenPrincipal() {
        Intent intent = new Intent(this, Principal.class);
        startActivity(intent);
    }

    private void checkUser(String correo, String contraseña) {
        Query query = db.collection("usuario");
        if (correo != null && !correo.isEmpty()) {
                query = query.whereEqualTo("correo", correo);
            if (contraseña != null && !contraseña.isEmpty()) {
                query = query.whereEqualTo("contraseña", contraseña);
            } else {
                contraseñaEditText.setError("Datos Inválidos");
                contraseñaEditText.requestFocus();
                return;
            }
        } else {
            correoEditText.setError("Contraseña o Correo Invalido");
            correoEditText.requestFocus();
            return;
        }

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                infoEstudiante = new ArrayList<>();
                String idTipo = "";
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    infoEstudiante.add(documentSnapshot.getString("carnet"));
                    idTipo = documentSnapshot.getString("idTipo");
                }

                if (!infoEstudiante.isEmpty()) {
                    if (idTipo.equals("Estudiante")){
                        Intent intent = new Intent(Login.this, MainEstudiante.class);
                        startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(Login.this, MainAdministrador.class);
                        startActivity(intent);
                    }
                } else {
                    correoEditText.setError("Contraseña o Correo Invalido");
                    correoEditText.requestFocus();
                }
            }
        });
    }
}


