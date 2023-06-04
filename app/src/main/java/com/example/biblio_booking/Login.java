package com.example.biblio_booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class Login extends AppCompatActivity {

    private TextInputEditText correoEditText;
    private TextInputEditText contraseñaEditText;
    private Button IngresarButton;
    private FirebaseFirestore db;
    private CollectionReference studentsRef;
    private CollectionReference adminRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        correoEditText = findViewById(R.id.correo);
        contraseñaEditText = findViewById(R.id.contraseña);
        IngresarButton = findViewById(R.id.Ingresar);

        db = FirebaseFirestore.getInstance();
        studentsRef = db.collection("usuario");
        adminRef = db.collection("adminref");



        Button backButton = findViewById(R.id.volver);
        backButton.setOnClickListener (new View.OnClickListener() {
            public void onClick(View v) {
                reOpenPrincipal();
            }
        });


        IngresarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateUsername() | !validatePassword()) {
                } else {
                    if(/*Debe ser un boolean Algo asi como if usuario=estudiante*/){
                        OpenMainEstudiante();
                    }
                    else{
                        OpenMainAdministrador();
                    }

                }
            }
        });

    }



    public Boolean validateUsername() {
        String val = correoEditText.getText().toString();
        if (val.isEmpty()) {
            correoEditText.setError("Debe ingresar su correo");
            return false;
        } else {
            correoEditText.setError(null);
            return true;
        }
    }

    public Boolean validatePassword(){
        String val = contraseñaEditText.getText().toString();
        if (val.isEmpty()) {
            contraseñaEditText.setError("Debe ingresar su contraseña");
            return false;
        } else {
            contraseñaEditText.setError(null);
            return true;
        }
    }
    public void reOpenPrincipal(){
        Intent intent = new Intent(this, Principal.class);
        startActivity(intent);
    }
    public void OpenMainAdministrador(){
        Intent intent = new Intent(this, MainAdministrador.class);
        startActivity(intent);
    }
    public void OpenMainEstudiante(){
        Intent intent = new Intent(this, MainEstudiante.class);
        startActivity(intent);
    }
boolean EsEstudiante=false;
    public void checkUser(){
        String userUsername = correoEditText.getText().toString().trim();
        String userPassword = contraseñaEditText.getText().toString().trim();
        Query checkUser = studentsRef.whereEqualTo("correo", correoEditText);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    correoEditText.setError(null);
                    String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);
                    if (passwordFromDB.equals(userPassword)) {
                        EsEstudiante=true;
                    } else {
                        contraseñaEditText.setError("Datos Invalidos");
                        contraseñaEditText.requestFocus();
                    }
                } else {
                    correoEditText.setError("El Usuario no existe");
                    correoEditText.requestFocus();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}