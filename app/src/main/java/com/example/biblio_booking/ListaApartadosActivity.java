package com.example.biblio_booking;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ListaApartadosActivity extends AppCompatActivity {

    private TextView editText;
    private TextView editText2;
    private LinearLayout linearLayout;

    private DatePickerDialog.OnDateSetListener dateSetListener;
    private List<DocumentSnapshot> allAssignments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_apartados);

        editText = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);
        linearLayout = findViewById(R.id.linearLayout);
        Button buttonB = findViewById(R.id.ButtonB); // Find the button with ID "ButtonB"

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

        // Set click listener for the "buscar" button
        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = editText.getText().toString();
                String selectedDate = editText2.getText().toString();

                // Check if both editText and editText2 have values
                if (!searchText.isEmpty() && !selectedDate.isEmpty()) {
                    filterAssignments("Cubiculo" + searchText, selectedDate);
                } else {
                    // Show a toast message indicating that information is required
                    Toast.makeText(ListaApartadosActivity.this, "Llene todos los espacios", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Fetch all the assignments from Firestore initially
        fetchAllAssignments();
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

    private void fetchAllAssignments() {
        // Get a reference to the Firestore database
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get a reference to the "assignments" collection in Firestore
        CollectionReference assignmentsRef = db.collection("Asignacion");

        // Fetch all the assignments from Firestore
        assignmentsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Retrieve the query snapshot containing the assignment documents
                    QuerySnapshot querySnapshot = task.getResult();

                    // Store all the assignments in a list
                    allAssignments = new ArrayList<>(querySnapshot.getDocuments());

                    // Show all the assignments
                    showAssignments(allAssignments);
                } else {
                    // Handle the error
                    Toast.makeText(ListaApartadosActivity.this, "Error fetching assignments", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void filterAssignments(String searchText, String selectedDate) {
        // Clear the existing views from the LinearLayout
        linearLayout.removeAllViews();

        boolean isMatchFound = false;

        // Iterate over the assignment documents
        for (DocumentSnapshot document : allAssignments) {
            // Assuming the assignment document has a "cubiculo" field, you can access it like this
            String cubiculoAsignado = document.getString("cubiculo");
            String fechaAsignada = document.getString("fecha");

            if (cubiculoAsignado.toLowerCase().contains(searchText.toLowerCase())
                    && fechaAsignada.equals(selectedDate)) {
                // Create a TextView to display the assignment details
                TextView assignmentTextView = new TextView(ListaApartadosActivity.this);
                assignmentTextView.setText("Cubiculo: " + cubiculoAsignado
                        + "\nFecha: " + fechaAsignada
                        + "\nHora: " + document.getString("hora")
                        + "\nCantidad de estudiantes: " + document.getString("cantidad"));

                // Add layout parameters to the TextView
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                layoutParams.setMargins(0, 0, 0, 16); // Add margin between TextViews
                assignmentTextView.setLayoutParams(layoutParams);

                // Add the TextView to the LinearLayout
                linearLayout.addView(assignmentTextView);

                isMatchFound = true;
            }
        }

        if (!isMatchFound) {
            // Show a message when no assignments match the search criteria
            TextView noAssignmentsTextView = new TextView(ListaApartadosActivity.this);
            noAssignmentsTextView.setText("Asignación no encontrada");
            linearLayout.addView(noAssignmentsTextView);
        }
    }


    private void showAssignments(List<DocumentSnapshot> assignments) {
        // Clear the existing views from the LinearLayout
        linearLayout.removeAllViews();

        // Iterate over the assignment documents
        for (DocumentSnapshot document : assignments) {
            // Assuming the assignment document has a "title" field, you can access it like this
            String cubiculoAsignado = document.getString("cubiculo");
            String fechaAsignada = document.getString("fecha");
            String horaAsignada = document.getString("hora");
            String cantidadAsignada = document.getString("cantidad");

            // Create a TextView to display the assignment details
            TextView assignmentTextView = new TextView(ListaApartadosActivity.this);
            assignmentTextView.setText("Cubiculo: " + cubiculoAsignado
                    + "\nFecha: " + fechaAsignada
                    + "\nHora: " + horaAsignada
                    + "\nCantidad de estudiantes: " + cantidadAsignada);

            // Add layout parameters to the TextView
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(0, 0, 0, 16); // Add margin between TextViews
            assignmentTextView.setLayoutParams(layoutParams);

            // Add the TextView to the LinearLayout
            linearLayout.addView(assignmentTextView);
        }
    }
}







