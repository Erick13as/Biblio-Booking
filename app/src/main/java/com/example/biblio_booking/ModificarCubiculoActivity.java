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
                //verificar que ninguno este vacio al presionar guardar
                updateInfoCubiculo(numcubiculo.getText().toString());
                resetEmpty(true);
                alerta.setTitle("Éxito");
                alerta.setMessage("El cubículo " + numcubiculo.getText().toString() + "ha sido modificado");
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
        //no me esta editando el spinner
        String strS = infoCubiculo.get(4).toUpperCase();
        String strE = infoCubiculo.get(5).toUpperCase();
        int posFinalS = 0;
        int posFinalE = 0;

        if(strS == spinS.getItemAtPosition(1)){
            posFinalS = 1;
        }

        if(strE == spinE.getItemAtPosition(1)){
            posFinalE = 1;
        }
        else if (strE == spinE.getItemAtPosition(2)){
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

    private void updateInfoCubiculo(String numcubiculo) {

    }
}