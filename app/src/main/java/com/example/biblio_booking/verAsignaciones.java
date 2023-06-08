package com.example.biblio_booking;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class verAsignaciones extends AppCompatActivity {

    private FirebaseFirestore db;
    private LinearLayout layoutAsignaciones;
    private EditText editTextCubiculo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_asignaciones);

        db = FirebaseFirestore.getInstance();
        layoutAsignaciones = findViewById(R.id.layoutAsignaciones);
        editTextCubiculo = findViewById(R.id.editTextCubiculo);

        Button backButton = (Button) findViewById(R.id.buttonVolver);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reOpenConsultarAsigancaiones();
            }
        });
        editTextCubiculo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No action needed
            }

            @Override
            public void afterTextChanged(Editable s) {
                String cubiculo = "Cubiculo"+s.toString();
                updateAssignmentsList(cubiculo);
            }
        });

        // Initially, display all assignments
        updateAssignmentsList(null);
    }

    private void updateAssignmentsList(String cubiculo) {
        layoutAsignaciones.removeAllViews(); // Clear previous assignments

        Query query = db.collection("Asignacion");
        if (cubiculo != null && !cubiculo.isEmpty()) {
            query = query.whereEqualTo("cubiculo", cubiculo);
        }

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String cubiculoAsignado = documentSnapshot.getString("cubiculo");
                    String fechaAsignada = documentSnapshot.getString("fecha");
                    String horaAsignada = documentSnapshot.getString("hora");
                    String cantidadAsignada = documentSnapshot.getString("cantidad");

                    // Create a TextView for each assignment and add it to the LinearLayout
                    TextView textViewAssignment = new TextView(verAsignaciones.this);
                    textViewAssignment.setText("Cubiculo: " + cubiculoAsignado
                            + "\nFecha: " + fechaAsignada
                            + "\nHora: " + horaAsignada
                            + "\nCantidad de estudiantes: " + cantidadAsignada + "\n");
                    layoutAsignaciones.addView(textViewAssignment);
                }
            }
        });
    }
    public void reOpenConsultarAsigancaiones() {
        Intent intent = new Intent(this, ConsultarAsignaciones.class);
        startActivity(intent);
    }
}

