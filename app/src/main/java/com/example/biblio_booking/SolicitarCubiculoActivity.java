package com.example.biblio_booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biblio_booking.databinding.ActivitySolicitarCubiculoBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class SolicitarCubiculoActivity extends AppCompatActivity {

    ActivitySolicitarCubiculoBinding binding;
    private TextView editText2;
    private Button btnEnviar;
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

       btnEnviar = findViewById(R.id.btnEnviar);
       btnEnviar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                uploadDataToFirestore();
            }
        });

    }
    private void uploadDataToFirestore() {
        Spinner hora=findViewById(R.id.spinnerHora);
        Spinner cubiculo=findViewById(R.id.spinnerCubiculos);
        Spinner CantCompa=findViewById(R.id.spinnerCantidad);
        TextView Fecha = findViewById(R.id.editText2);

        String horaSoli = hora.getSelectedItem().toString();
        String CubiSoli = cubiculo.getSelectedItem().toString();
        String CantSoli = CantCompa.getSelectedItem().toString();
        String FechaSoli = Fecha.getText().toString();


        // Create a new User object
        Asignacion asignacion = new Asignacion(horaSoli,CubiSoli,CantSoli,FechaSoli);

        // Get a reference to the "users" collection in Firestore
        CollectionReference usersCollection = mFirestore.collection("Asignacion");

        // Upload the user data to Firestore
        usersCollection.add(asignacion)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            // Data successfully uploaded to Firestore
                            // You can perform any desired actions here
                            // For example, display a success message
                            Toast.makeText(SolicitarCubiculoActivity.this, "Data uploaded successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            // Failed to upload data to Firestore
                            // You can handle the error here
                            Toast.makeText(SolicitarCubiculoActivity.this, "Failed to upload data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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