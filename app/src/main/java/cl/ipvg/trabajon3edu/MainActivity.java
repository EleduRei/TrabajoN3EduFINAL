package cl.ipvg.trabajon3edu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    // Lista para almacenar los objetos ListadoModel
    private List<ListadoModel> ListListado = new ArrayList<>();
    private List<String> ListListadoNombre = new ArrayList<>();

    // Adaptadores para mostrar los datos en la lista
    ArrayAdapter<ListadoModel> arrayAdapterListado;
    ArrayAdapter<String> arrayAdapterString;

    // Elementos de la interfaz de usuario
    EditText ETNOMBRE, ETPRODUCTOS;
    Button BTGUARDAR, BTIRALSEGUNDO;
    ListView LISTLISTADO;

    // Firebase Database
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicialización de los elementos de la interfaz
        ETNOMBRE = findViewById(R.id.ETNOMBRE);
        ETPRODUCTOS = findViewById(R.id.ETPRODUCTOS);
        BTGUARDAR = findViewById(R.id.BTGUARDAR);
        BTIRALSEGUNDO = findViewById(R.id.BTIRALSEGUNDO);
        LISTLISTADO = findViewById(R.id.LISTLISTADO);

        // Inicializar Firebase
        inicializarFireBase();
        listarDatos();

        // Evento para el botón de guardar
        BTGUARDAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = ETNOMBRE.getText().toString().trim();
                String producto = ETPRODUCTOS.getText().toString().trim();

                if (nombre.isEmpty() || producto.isEmpty()) {
                    // Mostrar un mensaje si los campos están vacíos
                    Toast.makeText(MainActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    // Crear el objeto ListadoModel y guardar en Firebase
                    ListadoModel listado = new ListadoModel();
                    listado.setIdAutor(UUID.randomUUID().toString());
                    listado.setNombre(nombre);
                    listado.setProducto(producto); // Cambiado a "producto"

                    // Guardar el objeto en Firebase
                    databaseReference.child("Listado").child(listado.getIdAutor()).setValue(listado)
                            .addOnSuccessListener(aVoid -> {
                                // Éxito al guardar
                                Toast.makeText(MainActivity.this, "Guardado correctamente", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                // Error al guardar
                                Toast.makeText(MainActivity.this, "Error al guardar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }
            }
        });

        // Evento para ir a la actividad "Listado"
        BTIRALSEGUNDO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListadoModel.class); // Asegúrate de que sea Listado
                startActivity(intent);
            }
        });
    }

    // Método para listar los datos desde Firebase y mostrarlos en el ListView
    private void listarDatos() {
        databaseReference.child("Listado").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ListListado.clear();
                ListListadoNombre.clear();
                for (DataSnapshot objs : snapshot.getChildren()) {
                    // Convertir los datos de Firebase en objetos ListadoModel
                    ListadoModel listado = objs.getValue(ListadoModel.class);
                    ListListado.add(listado);
                    ListListadoNombre.add(listado.getNombre() + " - " + listado.getProducto()); // Cambiado a "producto"
                }
                // Crear adaptadores para mostrar los datos en el ListView
                arrayAdapterString = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, ListListadoNombre);
                LISTLISTADO.setAdapter(arrayAdapterString);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejo de error de Firebase
                Toast.makeText(MainActivity.this, "Error al cargar los datos.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Inicializar Firebase
    private void inicializarFireBase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
}
