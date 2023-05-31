package com.example.biblio_booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Registro extends AppCompatActivity {

    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Initialize Firestore instance
        mFirestore = FirebaseFirestore.getInstance();

        Button button = findViewById(R.id.Registro);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                uploadDataToFirestore();
            }
        });

        Button backButton = findViewById(R.id.volver);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reOpenPrincipal();
            }
        });
    }

    private void uploadDataToFirestore() {
        TextInputEditText nombreEditText = findViewById(R.id.nombre);
        TextInputEditText apellidoEditText = findViewById(R.id.Apellido);
        TextInputEditText apellido2EditText = findViewById(R.id.Apellido2);
        TextInputEditText carnetEditText = findViewById(R.id.carnet);
        TextInputEditText fechaNacEditText = findViewById(R.id.fechaNac);
        TextInputEditText correoEditText = findViewById(R.id.correo);
        TextInputEditText contraseñaEditText = findViewById(R.id.contraseña);

        String nombre = nombreEditText.getText().toString();
        String apellido = apellidoEditText.getText().toString();
        String apellido2 = apellido2EditText.getText().toString();
        String carnet = carnetEditText.getText().toString();
        String fechaNac = fechaNacEditText.getText().toString();
        String correo = correoEditText.getText().toString();
        String contraseña = contraseñaEditText.getText().toString();

        // Create a new User object
        User user = new User(nombre, apellido, apellido2, carnet, fechaNac, correo, contraseña);

        // Get a reference to the "users" collection in Firestore
        CollectionReference usersCollection = mFirestore.collection("usuario");

        // Upload the user data to Firestore
        usersCollection.add(user)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            // Data successfully uploaded to Firestore
                            // You can perform any desired actions here
                            // For example, display a success message
                            Toast.makeText(Registro.this, "Data uploaded successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            // Failed to upload data to Firestore
                            // You can handle the error here
                            Toast.makeText(Registro.this, "Failed to upload data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void reOpenPrincipal() {
        Intent intent = new Intent(this, Principal.class);
        startActivity(intent);
    }
}


