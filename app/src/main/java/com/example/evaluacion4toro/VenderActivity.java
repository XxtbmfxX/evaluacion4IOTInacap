package com.example.evaluacion4toro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VenderActivity extends AppCompatActivity {

    Spinner spinnerProductos;
    EditText edtCantidadVenta;
    Button btnVender, btnAtrasVentaHaciaMain;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("productos");

    ArrayList<Producto> listaProductos = new ArrayList<>();
    ArrayAdapter<String> adapterSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vender);


        spinnerProductos = findViewById(R.id.spinner_productos);
        edtCantidadVenta = findViewById(R.id.edt_cantidad_venta);
        btnVender = findViewById(R.id.btn_vender);
        btnAtrasVentaHaciaMain = findViewById(R.id.btn_vender_hacia_main);


        adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
        spinnerProductos.setAdapter(adapterSpinner);

        cargarProductos();

        btnAtrasVentaHaciaMain.setOnClickListener(view -> {
            Intent intent = new Intent(VenderActivity.this, MainActivity.class);
            startActivity(intent);
        });

        btnVender.setOnClickListener(view -> realizarVenta());


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void cargarProductos() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                listaProductos.clear();
                adapterSpinner.clear();

                for (DataSnapshot data : snapshot.getChildren()) {
                    Producto producto = data.getValue(Producto.class);
                    if (producto != null) {
                        listaProductos.add(producto);
                        adapterSpinner.add(producto.getCodigo() + " - " + producto.getNombre());
                    }
                }

                adapterSpinner.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(VenderActivity.this, "Error al cargar productos {{{(>_<)}}} " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void realizarVenta() {
        int posicion = spinnerProductos.getSelectedItemPosition();
        String cantidadStr = edtCantidadVenta.getText().toString().trim();

        if (posicion == -1 || cantidadStr.isEmpty()) {
            Toast.makeText(this, "Seleccione un producto y una cantidad válida ≧ ﹏ ≦", Toast.LENGTH_SHORT).show();
            return;
        }

        Producto productoSeleccionado = listaProductos.get(posicion);
        int cantidadVenta = Integer.parseInt(cantidadStr);

        if (cantidadVenta > productoSeleccionado.getStock()) {
            Toast.makeText(this, "Stock insuficiente para la venta (⓿_⓿)", Toast.LENGTH_SHORT).show();
            return;
        }

        productoSeleccionado.setStock(productoSeleccionado.getStock() - cantidadVenta);
        myRef.child(productoSeleccionado.getCodigo()).setValue(productoSeleccionado).addOnSuccessListener(aVoid -> {
            Toast.makeText(this, "Venta realizada (⌐■_■)", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(VenderActivity.this, CheckoutActivity.class);
            intent.putExtra("codigo", productoSeleccionado.getCodigo());
            intent.putExtra("nombre", productoSeleccionado.getNombre());
            intent.putExtra("precio", productoSeleccionado.getPrecio());
            intent.putExtra("cantidad", cantidadVenta);
            startActivity(intent);
        }).addOnFailureListener(e -> Toast.makeText(this, "Error al actualizar el producto ☠️",
                Toast.LENGTH_SHORT).show());
    }

}