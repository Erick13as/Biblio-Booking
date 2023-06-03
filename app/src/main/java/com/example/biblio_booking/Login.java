package com.example.biblio_booking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
public class Login extends AppCompatActivity {

    private TextInputEditText correoEditText;;
    private TextInputEditText contraseñaEditText;;
    private Button IngresarButton;

    private FirebaseFirestore db;
    private CollectionReference studentsRef;
    //private CollectionReference Ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        correoEditText = findViewById(R.id.correo);
        contraseñaEditText = findViewById(R.id.contraseña);
        IngresarButton = findViewById(R.id.Ingresar);

        db = FirebaseFirestore.getInstance();
        studentsRef = db.collection("usuario");

        Button backButton = findViewById(R.id.volver);
        backButton.setOnClickListener (new View.OnClickListener() {
            public void onClick(View v) {
                reOpenPrincipal();
            }
        });

    }



    public void reOpenPrincipal(){
        Intent intent = new Intent(this, Principal.class);
        startActivity(intent);
    }
}