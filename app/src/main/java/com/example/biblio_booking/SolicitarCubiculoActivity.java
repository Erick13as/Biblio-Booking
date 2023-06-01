package com.example.biblio_booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.biblio_booking.databinding.ActivitySolicitarCubiculoBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class SolicitarCubiculoActivity extends AppCompatActivity {

    ActivitySolicitarCubiculoBinding binding;
    private TextView editText2;
    private FirebaseFirestore mFirestore;
    private List<String> nombresCubiculos;
    private Spinner spinnerCubiculos;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitar_cubiculo);
        editText2 = findViewById(R.id.editText2);

        mFirestore = FirebaseFirestore.getInstance();
        nombresCubiculos = new ArrayList<>();
        spinnerCubiculos = findViewById(R.id.spinnerCubiculos);
        obtenerNombresCubiculos();
        // Set click listener for the date button
        editText2.setOnClickListener(new View.OnClickListener() {
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
                editText2.setText(selectedDate);
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
    private void obtenerNombresCubiculos() {
        mFirestore.collection("Cubiculo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String nombreCubiculo = document.getString("nombre");
                                nombresCubiculos.add(nombreCubiculo);
                            }
                            configurarSpinner();
                        } else {
                            Log.e("Firestore", "Error al obtener los datos", task.getException());
                        }
                    }
                });
    }
    private void configurarSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresCubiculos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCubiculos.setAdapter(adapter);
    }

}