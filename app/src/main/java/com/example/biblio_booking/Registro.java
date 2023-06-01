package com.example.biblio_booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class Registro extends AppCompatActivity {

    private FirebaseFirestore mFirestore;
    private TextView editTextFecha;
    private DatePickerDialog.OnDateSetListener dateSetListener;
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
        editTextFecha = findViewById(R.id.editTextFecha);

        // Set click listener for the date button
        editTextFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Initialize the date picker listener
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Handle the selected date here
                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                editTextFecha.setText(selectedDate);
            }
        };
    }
    private void showDatePickerDialog() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Create and show the date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                dateSetListener,
                year,
                month,
                dayOfMonth);
        datePickerDialog.show();
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


