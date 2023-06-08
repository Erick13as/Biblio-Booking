package com.example.biblio_booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AgregarCubiculoActivity extends AppCompatActivity {
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_cubiculo);
        mFirestore = FirebaseFirestore.getInstance();
        Button btnAgregarCubiculo = findViewById(R.id.btnAgregarCubiculo);
        btnAgregarCubiculo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Holaa1");
                agregarCubiculo();

            }
        });
    }
    private void agregarCubiculo() {

        TextInputEditText InputNombreCub = findViewById(R.id.InputNombreCub);
        TextInputEditText InputNumeroCub = findViewById(R.id.InputNumeroCub);
        TextInputEditText InputUbicacionCub = findViewById(R.id.InputUbicacionCub);
        TextInputEditText InputCapacidadCub = findViewById(R.id.InputCapacidadCub);
        Spinner ServiciosEspe=findViewById(R.id.spinnerServiciosEspe);
        Spinner Estado=findViewById(R.id.spinnerEstado);

        String nombreCub = InputNombreCub.getText().toString();
        String numeroCub = InputNumeroCub.getText().toString();
        String UbicacionCub = InputUbicacionCub.getText().toString();
        String CapacidadCub = InputCapacidadCub.getText().toString();
        String ServiciosE = ServiciosEspe.getSelectedItem().toString();
        String EstadoC = Estado.getSelectedItem().toString();
        String NewidCubiculo="cub"+numeroCub;


        if (TextUtils.isEmpty(nombreCub) || TextUtils.isEmpty(numeroCub) || TextUtils.isEmpty(UbicacionCub) || TextUtils.isEmpty(CapacidadCub) ) {
            Toast.makeText(this, "Debes completar todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            System.out.println("Holaa");
            CollectionReference cubiculoRef = mFirestore.collection("Cubiculo");
            // Verificar si ya existe un cubículo con el mismo nombre o número
            cubiculoRef.whereEqualTo("nombre", nombreCub)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                        @Override
                        public void onComplete(Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                boolean nombreCubExistente = false;
                                boolean numeroCubExistente = false;

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String idCubiculoExist = document.getString("idCubiculo");
                                    String nombreExist = document.getString("nombre");
                                    if (idCubiculoExist.equals(NewidCubiculo)) {
                                        numeroCubExistente = true;
                                        break;
                                    }
                                    nombreCubExistente = true;
                                }

                                if (nombreCubExistente) {
                                    Toast.makeText(AgregarCubiculoActivity.this, "Ya existe un cubículo con el mismo nombre", Toast.LENGTH_SHORT).show();
                                } else if (numeroCubExistente) {
                                    Toast.makeText(AgregarCubiculoActivity.this, "Ya existe un cubículo con el mismo número", Toast.LENGTH_SHORT).show();
                                } else {
                                    cubiculoRef.whereEqualTo("idCubiculo", NewidCubiculo)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                                                                       @Override
                                                                       public void onComplete(Task<QuerySnapshot> task) {

                                                                           if (task.isSuccessful()) {
                                                                               boolean idCubExistente = false;
                                                                               for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                   String idCubiculoExist = document.getString("idCubiculo");
                                                                                   if (idCubiculoExist.equals(NewidCubiculo)) {
                                                                                       idCubExistente = true;
                                                                                       break;
                                                                                   }
                                                                               }

                                                                               if (idCubExistente) {
                                                                                   Toast.makeText(AgregarCubiculoActivity.this, "Ya existe un cubículo con el mismo numero", Toast.LENGTH_SHORT).show();
                                                                               } else {
                                                                                   agregarCubiculoFirestore(nombreCub, numeroCub, UbicacionCub, CapacidadCub, ServiciosE, EstadoC);
                                                                                     }

                                                                           }
                                                                       }
                                                                   });
                                    // No existe un cubículo con el mismo nombre o número, agregar a Firestore

                                };
                            } else {
                                Toast.makeText(AgregarCubiculoActivity.this, "Error al verificar la existencia del cubículo", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }
    private void agregarCubiculoFirestore(String nombre, String numeroCub,String ubicacion,String capacidad,String servicioE,String idEstadoC){
        CollectionReference cubiculosCollection = mFirestore.collection("Cubiculo");
        String idCubiculo= "cub"+numeroCub;
        String tiempoMaxUso="0";
        Cubiculo cubiculo = new Cubiculo( capacidad, idCubiculo, idEstadoC, nombre, servicioE, tiempoMaxUso, ubicacion);
        cubiculosCollection.add(cubiculo)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AgregarCubiculoActivity.this, "Data uploaded successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AgregarCubiculoActivity.this, "Failed to upload data", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }
                });
    }
}