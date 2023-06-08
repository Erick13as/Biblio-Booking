package com.example.biblio_booking;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.ktx.Firebase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModificarCubiculoActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private TextInputEditText numcubiculo;
    private TextInputEditText nombre;
    private TextInputEditText ubicacion;
    private TextInputEditText capacidad;
    private TextInputEditText rangomax;
    private Spinner servicios;
    private Spinner estado;
    private List<String> infoCubiculo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_cubiculo);

        db = FirebaseFirestore.getInstance();
        numcubiculo = findViewById(R.id.cubiculo);
        nombre = findViewById(R.id.nombre2);
        capacidad = findViewById(R.id.capacidad);
        ubicacion = findViewById(R.id.ubicacion);
        rangomax = findViewById(R.id.rangomax);
        servicios = findViewById(R.id.spinnerservicios);
        estado = findViewById(R.id.estado2);
        infoCubiculo = new ArrayList<>();

        Button buscar = findViewById(R.id.buscar2);
        Button guardar = findViewById(R.id.guardar2);

        AlertDialog.Builder alerta = new AlertDialog.Builder(ModificarCubiculoActivity.this);
        alerta.setCancelable(true);

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validTextInput(numcubiculo)){
                    getInfoCubiculo(numcubiculo.getText().toString());
                    Handler handler = new Handler();
                    Runnable r = new Runnable() {
                        public void run() {
                            if(infoCubiculo.isEmpty()){
                                //no encuentra cubiculo
                                alerta.setTitle("Error");
                                alerta.setMessage("No se encontró ningún cubículo");
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
                                //encuentra cubiculo y edita campos en la pantalla
                                nombre.setText(infoCubiculo.get(0));
                                ubicacion.setText(infoCubiculo.get(1));
                                capacidad.setText(infoCubiculo.get(2));
                                rangomax.setText(infoCubiculo.get(3));
                                editSpinnerSeleccionados(servicios, estado);
                            }
                        }
                    };
                    handler.postDelayed(r, 2000);
                    resetEmpty(false);
                }
                infoCubiculo.clear();
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validTextInput(numcubiculo) && validTextInput(nombre) && validTextInput(ubicacion) && validTextInput(capacidad) && validTextInput(rangomax)) {
                    updateInfoCubiculo(numcubiculo.getText().toString(), alerta);
                }
            }
        });
    }

    private boolean validTextInput(TextInputEditText txtinput) {
        String result = txtinput.getText().toString();
        if(result.isEmpty()){
            txtinput.setError("Debe ingresar número de cubículo");
            return false;
        }
        else{
            txtinput.setError(null);
            return true;
        }
    }
    private void resetEmpty(boolean e){
        if(e){ numcubiculo.setText(""); }
        nombre.setText("");
        ubicacion.setText("");
        capacidad.setText("");
        rangomax.setText("");
        servicios.setSelection(0);
        estado.setSelection(0);
    }
    private void editSpinnerSeleccionados(Spinner spinS, Spinner spinE){
        String strS = infoCubiculo.get(4).toUpperCase();
        String strE = infoCubiculo.get(5).toUpperCase();

        int posFinalS = 0;
        int posFinalE = 0;

        if(strS.equalsIgnoreCase(spinS.getItemAtPosition(1).toString())){
            posFinalS = 1;
        }

        if(strE.equalsIgnoreCase(spinE.getItemAtPosition(1).toString())){
            posFinalE = 1;
        }
        else if (strE.equalsIgnoreCase(spinE.getItemAtPosition(2).toString())){
            posFinalE = 2;
        }

        spinS.setSelection(posFinalS);
        spinE.setSelection(posFinalE);
    }
    private void getInfoCubiculo(String numcubiculo){
        Query query = db.collection("Cubiculo");
        if (numcubiculo != null && !numcubiculo.isEmpty()) {
            query = query.whereEqualTo("idCubiculo", "cub" + numcubiculo);

            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        infoCubiculo.add(documentSnapshot.getString("nombre"));
                        infoCubiculo.add(documentSnapshot.getString("ubicacion"));
                        infoCubiculo.add(documentSnapshot.getString("capacidad"));
                        infoCubiculo.add(documentSnapshot.getString("tiempoMaxUso"));
                        infoCubiculo.add(documentSnapshot.getString("servicioE"));
                        infoCubiculo.add(documentSnapshot.getString("idEstadoC"));
                    }
                }
            });
        }
    }
    private void updateInfoCubiculo(String numcubiculo, AlertDialog.Builder alerta) {
        CollectionReference collectionRef = db.collection("Cubiculo");

        collectionRef.whereEqualTo("idCubiculo", "cub" + numcubiculo).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                    DocumentReference docRef = documentSnapshot.getReference();

                    docRef.update("nombre", nombre.getText().toString());
                    docRef.update("ubicacion", ubicacion.getText().toString());
                    docRef.update("capacidad", capacidad.getText().toString());
                    docRef.update("tiempoMaxUso", rangomax.getText().toString());
                    docRef.update("idEstadoC", estado.getSelectedItem().toString().substring(0, 1).toUpperCase() + estado.getSelectedItem().toString().substring(1).toLowerCase());
                    docRef.update("servicioE", servicios.getSelectedItem().toString().substring(0, 1).toUpperCase() + servicios.getSelectedItem().toString().substring(1).toLowerCase());
                }

                alerta.setTitle("Éxito");
                alerta.setMessage("Cubículo " + numcubiculo + " ha sido modificado");
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
                alerta.setTitle("Error");
                alerta.setMessage("Hubo un problema al modificar, intente de nuevo");
                alerta.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alerta.show();
                resetEmpty(false);
            }
        });
    }
}