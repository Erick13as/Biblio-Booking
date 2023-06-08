package com.example.biblio_booking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class EliminarEstudiante extends AppCompatActivity {

    private TextInputEditText carnetEditText;
    private Button eliminarButton;
    private Button volverButton;
    private FirebaseFirestore db;
    private CollectionReference studentsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_estudiante);

        carnetEditText = findViewById(R.id.carnet);
        eliminarButton = findViewById(R.id.eliminarEst);

        db = FirebaseFirestore.getInstance();
        studentsRef = db.collection("usuario");

        eliminarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String carnet = carnetEditText.getText().toString();
                eliminarEstudiante(carnet);
            }
        });



        volverButton = findViewById(R.id.volver); // Inicialización del botón volverButton

        volverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volverEstudiante();
            }
        });
    }

    public void volverEstudiante() {
        Intent intent = new Intent(this, Estudiantes.class);
        startActivity(intent);
    }
    private void eliminarEstudiante(String carnet) {
        Query query = studentsRef.whereEqualTo("carnet", carnet);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    // Match found, remove the data
                    querySnapshot.getDocuments().get(0).getReference().delete()
                            .addOnSuccessListener(aVoid -> {
                                // Data successfully removed
                                // Do any additional actions here if needed
                            })
                            .addOnFailureListener(e -> {
                                // Failed to remove data
                                // Handle the failure or show an error message
                            });
                } else {
                    // No match found, handle accordingly
                }
            } else {
                // Failed to execute the query
                // Handle the failure or show an error message
            }
        });
    }
}
