package com.example.biblio_booking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ModificarEstudianteActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private TextInputEditText carnet;
    private TextInputEditText nombre;
    private TextInputEditText apellido;
    private TextInputEditText fechaNacimiento;
    private TextInputEditText correo;
    private TextInputEditText contrasena;
    private Spinner estado;
    private List<String> infoEstudiante;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_estudiante);

        db = FirebaseFirestore.getInstance();
        carnet = findViewById(R.id.carnet);
        nombre = findViewById(R.id.nombre3);
        apellido = findViewById(R.id.apellidos);
        fechaNacimiento = findViewById(R.id.fechaNac);
        correo = findViewById(R.id.correo);
        contrasena = findViewById(R.id.contrasena);
        estado = findViewById(R.id.estado);
        infoEstudiante = new ArrayList<>();

        Button buscar = findViewById(R.id.buscar4);
        Button guardar = findViewById(R.id.guardar);

        AlertDialog.Builder alerta = new AlertDialog.Builder(ModificarEstudianteActivity.this);
        alerta.setCancelable(true);

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Handle the selected date here
                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                fechaNacimiento.setText(selectedDate);
            }
        };

        fechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fechaNacimiento.setText("");
                showDatePickerDialog();
            }
        });

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validTextInput(carnet)){
                    getInfoEstudiante(carnet.getText().toString());
                    Handler handler = new Handler();
                    Runnable r = new Runnable() {
                        public void run() {
                            if(infoEstudiante.isEmpty()){
                                //no encuentra estudiante
                                alerta.setTitle("Error");
                                alerta.setMessage("No se encontró ningún estudiante");
                                alerta.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                                alerta.show();
                                resetEmpty(true);
                            }
                            else{
                                //encuentra estudiante y edita campos en la pantalla
                                nombre.setText(infoEstudiante.get(0));
                                apellido.setText(infoEstudiante.get(1));
                                fechaNacimiento.setText(infoEstudiante.get(2));
                                correo.setText(infoEstudiante.get(3));
                                contrasena.setText(infoEstudiante.get(4));
                                editSpinnerSeleccionados(estado);
                            }
                        }
                    };
                    handler.postDelayed(r, 2500);
                    resetEmpty(false);
                }
                infoEstudiante.clear();
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //verificar que ninguno este vacio al presionar guardar
                //verificar que existan dos apellidos
                updateInfoEstudiante(carnet.getText().toString());
                resetEmpty(true);
                alerta.setTitle("Éxito");
                alerta.setMessage("Estudiante " + carnet.getText().toString() + "ha sido modificado");
                alerta.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alerta.show();
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

    private boolean validTextInput(TextInputEditText txtinput) {
        String result = txtinput.getText().toString();
        if(result.isEmpty()){
            txtinput.setError("Debe ingresar el carnet");
            return false;
        }
        else{
            txtinput.setError(null);
            return true;
        }
    }

    private void resetEmpty(boolean e){
        if(e){ carnet.setText(""); }
        nombre.setText("");
        apellido.setText("");
        fechaNacimiento.setText("");
        correo.setText("");
        contrasena.setText("");
        estado.setSelection(0);
    }

    private void editSpinnerSeleccionados(Spinner spinE){
        //no me esta editando el spinner
        String strE = infoEstudiante.get(5).toUpperCase();
        System.out.println("string:::::" + strE);
        System.out.println("item:::::" + spinE.getItemAtPosition(1).toString());
        int posFinalE = 0;

        if(strE == spinE.getItemAtPosition(1).toString()){
            System.out.println("entra");
            posFinalE = 1;
        }

        spinE.setSelection(posFinalE);
    }

    private void getInfoEstudiante(String carnet){
        Query query = db.collection("usuario");
        if (carnet != null && !carnet.isEmpty()) {
            query = query.whereEqualTo("carnet", carnet);

            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        infoEstudiante.add(documentSnapshot.getString("nombre"));
                        infoEstudiante.add(documentSnapshot.getString("apellido")+" "+documentSnapshot.getString("apellido2"));
                        infoEstudiante.add(documentSnapshot.getString("fechaNac"));
                        infoEstudiante.add(documentSnapshot.getString("correo"));
                        infoEstudiante.add(documentSnapshot.getString("contraseña"));
                        infoEstudiante.add(documentSnapshot.getString("idEstado"));
                    }
                    System.out.println(infoEstudiante);
                }
            });
        }
    }

    private void updateInfoEstudiante(String carnet){

    }
}