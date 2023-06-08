package com.example.biblio_booking;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ListaApartadosActivity extends AppCompatActivity {

    private User user;
    private TextView editText;
    private TextView editText2;
    private LinearLayout linearLayout;

    private DatePickerDialog.OnDateSetListener dateSetListener;
    private List<DocumentSnapshot> allAssignments;

    private boolean isSearchPerformed = false;
    private boolean isSearchSuccessful = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_apartados);

        // Retrieve the User object from the intent
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        editText = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);
        linearLayout = findViewById(R.id.linearLayout);
        Button buttonB = findViewById(R.id.ButtonB); // Find the button with ID "ButtonB"
        Button buttonE = findViewById(R.id.ButtonE); // Find the button with ID "ButtonE"
        Button buttonC = findViewById(R.id.ButtonC); // Find the button with ID "ButtonC"
        Button ButtonV = findViewById(R.id.ButtonV);

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
                    filterAssignments("cub" + searchText, selectedDate);
                    isSearchPerformed = true; // Set search performed flag
                } else {
                    // Show a toast message indicating that information is required
                    Toast.makeText(ListaApartadosActivity.this, "Llene todos los espacios", Toast.LENGTH_SHORT).show();
                    isSearchPerformed = false; // Reset search performed flag
                }
            }
        });

        buttonE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSearchPerformed && isSearchSuccessful) {
                    // Get the cubiculo and fecha values of the searched assignment
                    String searchText = editText.getText().toString();
                    String selectedDate = editText2.getText().toString();

                    // Iterate over the assignment documents
                    for (DocumentSnapshot document : allAssignments) {
                        String cubiculoAsignado = document.getString("cubiculo");
                        String fechaAsignada = document.getString("fecha");

                        if (cubiculoAsignado.toLowerCase().contains(searchText.toLowerCase())
                                && fechaAsignada.equals(selectedDate)) {
                            // Get the reference to the assignment document
                            String assignmentId = document.getId();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            CollectionReference assignmentsRef = db.collection("Asignacion");
                            DocumentReference assignmentDocRef = assignmentsRef.document(assignmentId);

                            // Delete the assignment document
                            assignmentDocRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Document successfully deleted
                                        Toast.makeText(ListaApartadosActivity.this, "Asignación eliminada", Toast.LENGTH_SHORT).show();

                                        // Clear the search text fields and update the UI
                                        editText.setText("");
                                        editText2.setText("");
                                        linearLayout.removeAllViews();
                                        isSearchPerformed = false;
                                        isSearchSuccessful = false;

                                        // Fetch all the assignments again to update the UI
                                        fetchAllAssignments();
                                    } else {
                                        // Failed to delete the document
                                        Toast.makeText(ListaApartadosActivity.this, "Error al eliminar la asignación", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            break; // Exit the loop after deleting the assignment
                        }
                    }
                } else {
                    // Show a toast message indicating that a successful search is required
                    Toast.makeText(ListaApartadosActivity.this, "Busque una asignación", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSearchPerformed && isSearchSuccessful) {
                    // Perform the "Confirmar" button functionality
                    generateQRCode();
                } else {
                    // Show a toast message indicating that a successful search is required
                    Toast.makeText(ListaApartadosActivity.this, "Busque una asignación", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ButtonV.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OpenMainE();
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

        // Create a query to filter the assignments by carnet
        Query query = assignmentsRef.whereEqualTo("carnet", user.getCarnet());

        // Fetch the assignments matching the query from Firestore
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Retrieve the query snapshot containing the assignment documents
                    QuerySnapshot querySnapshot = task.getResult();

                    // Store the assignments with matching carnet in a list
                    allAssignments = new ArrayList<>(querySnapshot.getDocuments());

                    // Show the filtered assignments
                    showAssignments(allAssignments);
                } else {
                    // Handle the error
                    Toast.makeText(ListaApartadosActivity.this, "Error fetching assignments", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void filterAssignments(String cubiculo, String fecha) {
        // Clear the existing assignments from the list
        allAssignments = new ArrayList<>();

        // Get a reference to the Firestore database
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get a reference to the "assignments" collection in Firestore
        CollectionReference assignmentsRef = db.collection("Asignacion");

        // Create a query to filter the assignments by cubiculo and fecha
        assignmentsRef.whereEqualTo("cubiculo", cubiculo)
                .whereEqualTo("fecha", fecha)
                .whereEqualTo("carnet", user.getCarnet()) // Compare with user's carnet
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Retrieve the query snapshot containing the filtered assignment documents
                            QuerySnapshot querySnapshot = task.getResult();

                            // Check if any assignments match the search criteria
                            if (!querySnapshot.isEmpty()) {
                                // Store the filtered assignments in the list
                                allAssignments.addAll(querySnapshot.getDocuments());
                                isSearchSuccessful = true; // Set search successful flag
                            } else {
                                // Show a toast message indicating no matching assignments found
                                Toast.makeText(ListaApartadosActivity.this, "No se encontraron asignaciones", Toast.LENGTH_SHORT).show();
                                isSearchSuccessful = false; // Reset search successful flag
                            }

                            // Show the filtered assignments
                            showAssignments(allAssignments);
                        } else {
                            // Handle the error
                            Toast.makeText(ListaApartadosActivity.this, "Error filtering assignments", Toast.LENGTH_SHORT).show();
                            isSearchSuccessful = false; // Reset search successful flag
                        }
                    }
                });
    }

    private void showAssignments(List<DocumentSnapshot> assignments) {
        // Clear the linear layout
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

    private void generateQRCode() {
        // Get the cubiculo, fecha, and hora values of the searched assignment
        String searchText = editText.getText().toString();
        String selectedDate = editText2.getText().toString();

        // Iterate over the assignment documents
        for (DocumentSnapshot document : allAssignments) {
            String cubiculoAsignado = document.getString("cubiculo");
            String fechaAsignada = document.getString("fecha");
            String horaAsignada = document.getString("hora");
            String carnet = document.getString("carnet");

            if (cubiculoAsignado.toLowerCase().contains(searchText.toLowerCase())
                    && fechaAsignada.equals(selectedDate)
                    && carnet.equals(user.getCarnet())) { // Compare with user's carnet
                // Get the document ID as the booking ID
                String bookingId = document.getId();

                // Prepare the data string to encode in the QR code
                String data = "ID de la Asignación: " + bookingId
                        + "\nCarnet: " + carnet
                        + "\nFecha: " + fechaAsignada
                        + "\nHora: " + horaAsignada;

                // Generate the QR code for the data
                Bitmap qrCodeBitmap = generateQRCodeBitmap(data);

                // Create a new ImageView and set the bitmap
                ImageView imageView = new ImageView(this);
                imageView.setImageBitmap(qrCodeBitmap);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recreateActivity(); // Reload the activity
                    }
                });

                // Set layout parameters for the ImageView
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                layoutParams.gravity = Gravity.CENTER; // Set gravity to center
                imageView.setLayoutParams(layoutParams);

                // Create a new LinearLayout to hold the ImageView
                LinearLayout fullscreenLayout = new LinearLayout(this);
                fullscreenLayout.setBackgroundColor(Color.BLACK);
                fullscreenLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                ));
                fullscreenLayout.setGravity(Gravity.CENTER);
                fullscreenLayout.addView(imageView);

                // Set the content view to the fullscreenLayout
                setContentView(fullscreenLayout);
                break; // Exit the loop after generating the QR code
            }
        }
    }

    private void recreateActivity() {
        // Recreate the activity to reload it
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void enterFullScreen() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void exitFullScreen() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    private boolean isFullScreen() {
        int uiVisibility = getWindow().getDecorView().getSystemUiVisibility();
        return (uiVisibility & View.SYSTEM_UI_FLAG_FULLSCREEN) != 0;
    }

    private Bitmap generateQRCodeBitmap(String data) {
        // Set the QR code parameters
        int width = 800;
        int height = 800;
        int margin = 1;

        // Create a BitMatrix object to encode the data as QR code
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, width, height, null);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }

        // Create an array to hold the pixels of the QR code bitmap
        int[] pixels = new int[width * height];

        // Set the pixel colors based on the bitMatrix
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE;
            }
        }

        // Create a bitmap from the pixel array
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        return bitmap;
    }

    public void OpenMainE() {
        Intent intent = new Intent(this, MainEstudiante.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }
}









