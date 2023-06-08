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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


public class SolicitarCubiculoActivity extends AppCompatActivity {

    ActivitySolicitarCubiculoBinding binding;
    private TextView editText2;
    private Button btnEnviar;
    private FirebaseFirestore mFirestore;
    private List<String> nombresCubiculos;
    private Spinner spinnerCubiculos;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitar_cubiculo);
        editText2 = findViewById(R.id.editText2);

        mFirestore = FirebaseFirestore.getInstance();
        nombresCubiculos = new ArrayList<>();
        spinnerCubiculos = findViewById(R.id.spinnerCubiculos);
        obtenerNombresCubiculos();

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
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
                //uploadDataToFirestore();
                validarAsignacion();
            }
        });

    }

    public void validarAsignacion() {

        Spinner hora=findViewById(R.id.spinnerHora);
        Spinner cubiculo=findViewById(R.id.spinnerCubiculos);
        Spinner CantCompa=findViewById(R.id.spinnerCantidad);
        TextView fecha = findViewById(R.id.editText2);

        String horaSoli = hora.getSelectedItem().toString();
        String horaSalida="Sin definir";
        String CubiSoli = cubiculo.getSelectedItem().toString();
        String CantSoli = CantCompa.getSelectedItem().toString();
        String FechaSoli = fecha.getText().toString();

        if (CubiSoli.equals("Sin disponibilidad")) {
            Toast.makeText(SolicitarCubiculoActivity.this, "Sin cubículos disponibles", Toast.LENGTH_SHORT).show();
        } else if (FechaSoli.equals("")){
            Toast.makeText(SolicitarCubiculoActivity.this, "Seleccione una fecha", Toast.LENGTH_SHORT).show();
        } else {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference asignacionesRef = db.collection("Asignacion");

            asignacionesRef.whereEqualTo("cubiculo", CubiSoli)
                    .whereEqualTo("fecha", FechaSoli)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            String horaEntradaExistente = document.getString("hora");

                            // Validar si hay una superposición de horarios
                            if (tiempoChoca(horaSoli, horaEntradaExistente)) {
                                // Hay una superposición de asignaciones
                                Toast.makeText(SolicitarCubiculoActivity.this, "El cubiculo seleccionado se encuentra ocupado en la hora y fecha indicada", Toast.LENGTH_SHORT).show();

                                return;
                            }
                        }
                        // No hay choque de asignaciones
                        CollectionReference usersCollection = mFirestore.collection("Asignacion");
                        Asignacion asignacion = new Asignacion(horaSoli, CubiSoli,user.getCarnet(), CantSoli, FechaSoli, horaSalida);
                        usersCollection.add(asignacion)
                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(SolicitarCubiculoActivity.this, "Solicitud realizada exitosamente", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(SolicitarCubiculoActivity.this, "Solicitud fallida", Toast.LENGTH_SHORT).show();
                                        }
                                        OpenMainE();
                                        return;
                                    }
                                });


                    });

        }
    }

    // Función para verificar si dos intervalos de tiempo se superponen
    public boolean tiempoChoca(String horaEntrada1, String horaEntrada2) {
        // Convierte las horas a un formato adecuado para comparar, por ejemplo, "HH:mm"
        // Implementa la lógica de comparación adecuada según tus necesidades
        // Aquí tienes un ejemplo básico de comparación de cadenas de tiempo
        if (horaEntrada1.compareTo(horaEntrada2) > 0) {
            // Hay una superposición de tiempo
            return true;
        }

        // No hay superposición de tiempo
        return false;
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
                                String nombreCubiculo = document.getString("idCubiculo");
                                String idEstadoCubiculo = document.getString("idEstadoC");
                                if (idEstadoCubiculo.equals("Libre")) {
                                    nombresCubiculos.add(nombreCubiculo);
                                }
                            }
                            if (nombresCubiculos.isEmpty()){
                                return;
                            }
                            else{
                                configurarSpinner();
                            }

                        } else {
                            Log.e("Firestore", "Error al obtener los datos", task.getException());
                        }
                        return;
                    }
                });
    }
    private void configurarSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresCubiculos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCubiculos.setAdapter(adapter);
    }

    public void OpenMainE() {
        Intent intent = new Intent(this, MainEstudiante.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

}