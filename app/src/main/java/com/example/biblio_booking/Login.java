package com.example.biblio_booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.android.material.textfield.TextInputEditText;
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
        boolean EsEstudiante = false;


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
                    String correo = correoEditText.getText().toString();
                    String contraseña = contraseñaEditText.getText().toString();

                    /*if(tiene que ser un boleano) {
                        checkUser(correo, contraseña);
                    }
                    else{
                        checkAdmin(correo,contraseña);
                    }*/

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

    public void checkUser(String correo,String contraseña){
        Query query = studentsRef.whereEqualTo("correo", correo);
        Query query2 = studentsRef.whereEqualTo("contraseña", contraseña);
        if (query.equals(correo)) {
                    if (query2.equals(contraseña)) {

                        Intent intent = new Intent(Login.this, MainEstudiante.class);
                        startActivity(intent);

                    } else {
                        contraseñaEditText.setError("Datos Invalidos");
                        contraseñaEditText.requestFocus();
                    }
                } else {
                    correoEditText.setError("El Usuario no existe");
                    correoEditText.requestFocus();
                }

            }


    public void checkAdmin(String correo,String contraseña){
        Query query = adminRef.whereEqualTo("correo", correo);
        Query query2 = adminRef.whereEqualTo("contraseña", contraseña);
        if (query.equals(correo)) {
            if (query2.equals(contraseña)) {
                Intent intent = new Intent(Login.this, MainAdministrador.class);
                startActivity(intent);

            } else {
                contraseñaEditText.setError("Datos Invalidos");
                contraseñaEditText.requestFocus();
            }
        } else {
            correoEditText.setError("El Usuario no existe");
            correoEditText.requestFocus();
        }
        checkAdmin(correo,contraseña);
    }

    }

