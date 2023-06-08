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

public class EliminarCubiculo extends AppCompatActivity {
    private TextInputEditText numeroEditText;
    private Button eliminarButton;
    private Button buscarButton;
    private Button volverButton;
    private FirebaseFirestore db;
    private CollectionReference cubiculosRef;
    private CollectionReference studentsRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_cubiculo);
        numeroEditText = findViewById(R.id.numero);
        eliminarButton = findViewById(R.id.eliminarCub);
        buscarButton = findViewById(R.id.buscar);
        db = FirebaseFirestore.getInstance();
        volverButton = findViewById(R.id.volver);
        cubiculosRef = db.collection("Cubiculo");

        volverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volverCubiculo();
            }
        });
        eliminarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numero = numeroEditText.getText().toString();
                eliminarCubiculo("cub"+numero);
            }
        });
        buscarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numero = numeroEditText.getText().toString();
                buscarCub("cub"+numero);
            }
        });

    }
    public void volverCubiculo() {
        Intent intent = new Intent(this, Cubiculos.class);
        startActivity(intent);
    }
    private void eliminarCubiculo(String numero) {
        Query query = cubiculosRef.whereEqualTo("idCubiculo", numero);
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
    private void buscarCub(String numero) {
        Query query = cubiculosRef.whereEqualTo("idCubiculo", numero);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    // Match found, get the data
                    String nombre = querySnapshot.getDocuments().get(0).getString("nombre");
                    String ubicacion = querySnapshot.getDocuments().get(0).getString("ubicacion");

                    // Mostrar el nombre en el ScrollView
                    ScrollView scrollViewCub = findViewById(R.id.scrollCub);
                    LinearLayout linearLayoutCub = findViewById(R.id.linearLayout);

                    // Crear TextView para mostrar el nombre
                    TextView textViewCub = new TextView(this);
                    textViewCub.setText("Nombre: " + nombre + "\nUbicación: " + ubicacion + "\n");
                    // Agregar el TextView al LinearLayout dentro del ScrollView
                    linearLayoutCub.addView(textViewCub);

                    // Hacer scroll hasta el final del ScrollView para mostrar el contenido agregado
                    scrollViewCub.post(() -> {
                        scrollViewCub.fullScroll(ScrollView.FOCUS_DOWN);
                    });
                } else {
                    // No match found, handle accordingly
                    Toast.makeText(this, "Cubículo no encontrado", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Failed to execute the query
                // Handle the failure or show an error message
            }
        });
    }
    public void OpenMainA() {
        Intent intent = new Intent(this, MainAdministrador.class);
        startActivity(intent);
    }
}