package com.example.biblio_booking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Button;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class EliminarEstudiante extends AppCompatActivity {

    private TextInputEditText carnetEditText;
    private Button eliminarButton;
    private Button buscarButton;
    private Button volverButton;
    private FirebaseFirestore db;
    private CollectionReference studentsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_estudiante);

        carnetEditText = findViewById(R.id.carnet);
        eliminarButton = findViewById(R.id.eliminarEst);
        buscarButton = findViewById(R.id.buscar);
        db = FirebaseFirestore.getInstance();
        volverButton = findViewById(R.id.volver); // Inicialización del botón volverButton
        studentsRef = db.collection("usuario");

        eliminarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String carnet = carnetEditText.getText().toString();
                eliminarEstudiante(carnet);
            }
        });

        volverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volverEstudiante();
            }
        });
        buscarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String carnet = carnetEditText.getText().toString();
                buscarEstudiante(carnet);
            }
        });
    }

    public void volverEstudiante() {
        Intent intent = new Intent(this, Estudiantes.class);
        startActivity(intent);
    }
    private void buscarEstudiante(String carnet) {
        Query query = studentsRef.whereEqualTo("carnet", carnet);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    // Match found, get the data
                    String nombre = querySnapshot.getDocuments().get(0).getString("nombre");
                    String apellido = querySnapshot.getDocuments().get(0).getString("apellido");
                    String apellido2 = querySnapshot.getDocuments().get(0).getString("apellido2");

                    // Mostrar el nombre y el apellido en el ScrollView
                    ScrollView scrollViewEstudiante = findViewById(R.id.scrollEstudiante);
                    LinearLayout linearLayoutEstudiante = findViewById(R.id.linearLayout);

                    // Crear TextView para mostrar el nombre y el apellido
                    TextView textViewEst = new TextView(this);
                    textViewEst.setText( nombre + " " + apellido+ " " +apellido2+"\n");

                    // Agregar el TextView al LinearLayout dentro del ScrollView
                    linearLayoutEstudiante.addView(textViewEst);

                    // Hacer scroll hasta el final del ScrollView para mostrar el contenido agregado
                    scrollViewEstudiante.post(() -> {
                        scrollViewEstudiante.fullScroll(ScrollView.FOCUS_DOWN);
                    });
                } else {
                    // No match found, handle accordingly
                    Toast.makeText(this, "Estudiante no encontrado", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Failed to execute the query
                // Handle the failure or show an error message
            }
        });
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
        OpenMainA();
    }
    public void OpenMainA() {
        Intent intent = new Intent(this, MainAdministrador.class);
        startActivity(intent);
    }
}
