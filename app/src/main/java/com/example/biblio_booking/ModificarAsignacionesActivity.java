package com.example.biblio_booking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ModificarAsignacionesActivity extends AppCompatActivity {

    private Asignacion asignacion;
    private TextInputEditText carnetEditText;
    private TextInputEditText horaSAEditText;
    private TextInputEditText horaENEditText;
    private TextInputEditText fechaEditText;
    private FirebaseFirestore db;
    private CollectionReference asignacionesCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_asignaciones);

        // Get the intent and retrieve the asignacion object
        Intent intent = getIntent();
        asignacion = (Asignacion) intent.getSerializableExtra("asignacion");

        // Initialize the EditText fields
        carnetEditText = findViewById(R.id.carnet3);
        horaSAEditText = findViewById(R.id.horaSA);
        horaENEditText = findViewById(R.id.horaEN2);
        fechaEditText = findViewById(R.id.fecha2);

        // Set the values from the asignacion object to the EditText fields
        carnetEditText.setText(asignacion.getCarnet());
        horaSAEditText.setText(asignacion.getHoraSali());
        horaENEditText.setText(asignacion.getHora());
        fechaEditText.setText(asignacion.getFecha());

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        asignacionesCollection = db.collection("Asignacion"); // Replace with your collection name

        // Modify the data when the "Modificar asignación" button is clicked
        AppCompatButton modificarButton = findViewById(R.id.modificar2);
        modificarButton.setOnClickListener(v -> searchAndModifyData());

        // Delete the data when the "Eliminar asignación" button is clicked
        AppCompatButton eliminarButton = findViewById(R.id.eliminar2);
        eliminarButton.setOnClickListener(v -> searchAndDeleteData());
    }

    private void searchAndModifyData() {
        String carnet = carnetEditText.getText().toString();
        String horaSA = horaSAEditText.getText().toString();
        String horaEN = horaENEditText.getText().toString();
        String fecha = fechaEditText.getText().toString();

        // Create a query to search for the matching asignacion data
        Query query = asignacionesCollection
                .whereEqualTo("carnet", asignacion.getCarnet())
                .whereEqualTo("horaSali", asignacion.getHoraSali())
                .whereEqualTo("hora", asignacion.getHora())
                .whereEqualTo("fecha", asignacion.getFecha());

        // Execute the query
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();

                // Check if any matching documents are found
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        // Get the document ID
                        String documentId = document.getId();

                        // Update the fields in the Firestore document
                        document.getReference().update(
                                        "horaSali", horaSA,
                                        "hora", horaEN,
                                        "carnet", carnet,
                                        "fecha", fecha
                                )
                                .addOnSuccessListener(aVoid -> {
                                    // Data updated successfully
                                    // You can show a success message or perform any other action
                                })
                                .addOnFailureListener(e -> {
                                    // Error updating data
                                    // Handle the error as per your requirement
                                });
                    }
                } else {
                    // No matching documents found
                    // Handle the case when no matching data is found
                }
            } else {
                // Error occurred while executing the query
                // Handle the error as per your requirement
            }
        });
        OpenMainA();
    }

    private void searchAndDeleteData() {
        // Create a query to search for the matching asignacion data
        Query query = asignacionesCollection
                .whereEqualTo("carnet", asignacion.getCarnet())
                .whereEqualTo("horaSali", asignacion.getHoraSali())
                .whereEqualTo("hora", asignacion.getHora())
                .whereEqualTo("fecha", asignacion.getFecha());

        // Execute the query
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();

                // Check if any matching documents are found
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        // Delete the Firestore document
                        document.getReference().delete()
                                .addOnSuccessListener(aVoid -> {
                                    // Data deleted successfully
                                    // You can show a success message or perform any other action
                                })
                                .addOnFailureListener(e -> {
                                    // Error deleting data
                                    // Handle the error as per your requirement
                                });
                    }
                } else {
                    // No matching documents found
                    // Handle the case when no matching data is found
                }
            } else {
                // Error occurred while executing the query
                // Handle the error as per your requirement
            }
        });
        OpenMainA();
    }
    public void OpenMainA() {
        Intent intent = new Intent(this, MainAdministrador.class);
        startActivity(intent);
    }
}


