package com.example.biblio_booking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;

public class ConsultarAsignaciones extends AppCompatActivity {

    private FirebaseFirestore mFirestore;
    private Spinner spinnerhora;
    private TextInputEditText cubiculoCubEditText;
    private Button Buscar;
    private Button Verinfo;
    private TextView editText2;
    private TextInputEditText carnetCubEditText;
    boolean siexiste = false;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private Asignacion asignacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_asignaciones);

        mFirestore = FirebaseFirestore.getInstance();
        Buscar = findViewById(R.id.buscar3);
        Verinfo = findViewById(R.id.Verinfo);
        spinnerhora = findViewById(R.id.spinnerhora);
        cubiculoCubEditText = findViewById(R.id.cubiculo);
        carnetCubEditText = findViewById(R.id.carnet);
        editText2 = findViewById(R.id.editText2);

        Button backButton = (Button) findViewById(R.id.volver);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reOpenMainAdministrador();
            }
        });

        Verinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenVerAsignaciones();
            }
        });

        Buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String horaingresada = spinnerhora.getSelectedItem().toString();
                String cubiculoingresado = cubiculoCubEditText.getText().toString();
                String carnetingresado = carnetCubEditText.getText().toString();
                String Fechaingresada = editText2.getText().toString();

                checkAsignacion(horaingresada, "cub" + cubiculoingresado, carnetingresado, Fechaingresada);
            }
        });

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

    private void checkAsignacion(String horaingresada, String cubiculoingresado, String carnetingresado, String Fechaingresada) {
        Query query = mFirestore.collection("Asignacion");

        if (carnetingresado != null && !carnetingresado.isEmpty()) {
            query = query.whereEqualTo("carnet", carnetingresado);
            if (cubiculoingresado != null && !cubiculoingresado.isEmpty()) {
                query = query.whereEqualTo("cubiculo", cubiculoingresado);
                if (Fechaingresada != null && !Fechaingresada.isEmpty()) {
                    query = query.whereEqualTo("fecha", Fechaingresada);
                    if (horaingresada != null && !horaingresada.isEmpty()) {
                        query = query.whereEqualTo("hora", horaingresada);
                    } else {
                        cubiculoCubEditText.setError("La asignacion no existe");
                        cubiculoCubEditText.requestFocus();
                        return;
                    }
                } else {
                    cubiculoCubEditText.setError("La asignacion no existe");
                    cubiculoCubEditText.requestFocus();
                    return;
                }
            } else {
                cubiculoCubEditText.setError("La asignacion no existe");
                cubiculoCubEditText.requestFocus();
                return;
            }
        } else {
            cubiculoCubEditText.setError("La asignacion no existe");
            cubiculoCubEditText.requestFocus();
            return;
        }

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    // Retrieve other user information
                    String cubiculo = documentSnapshot.getString("cubiculo");
                    String carnet = documentSnapshot.getString("carnet");
                    String fecha = documentSnapshot.getString("fecha");
                    String hora = documentSnapshot.getString("hora");
                    String cantidad = documentSnapshot.getString("cantidad");
                    String horaSali = documentSnapshot.getString("horaSali");

                    // Create User object with retrieved data
                    asignacion = new Asignacion(hora, cubiculo, carnet, cantidad, fecha, horaSali);
                    siexiste = true;
                }

                if (siexiste) {
                    OpenModAsignaciones();
                } else {
                    cubiculoCubEditText.setError("La asignacion no existe");
                    cubiculoCubEditText.requestFocus();
                }
            }
        });
    }

    public void reOpenMainAdministrador() {
        Intent intent = new Intent(this, MainAdministrador.class);
        startActivity(intent);
    }

    public void OpenVerAsignaciones() {
        Intent intent = new Intent(this, verAsignaciones.class);
        startActivity(intent);
    }

    public void OpenModAsignaciones() {
        Intent intent = new Intent(this, ModificarAsignacionesActivity.class);
        intent.putExtra("asignacion", asignacion); // Pass the user object
        startActivity(intent);
    }

}
