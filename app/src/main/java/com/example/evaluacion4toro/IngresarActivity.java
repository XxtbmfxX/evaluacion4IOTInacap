package com.example.evaluacion4toro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;


public class IngresarActivity extends AppCompatActivity {

    Button btnAtrasVentaHaciaMain, btnIngresar;
    EditText edtCodigo, edtNombre, edtPrecio, edtStock;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("productos");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ingresar);


        btnIngresar = findViewById(R.id.btn_ingresar);
        btnAtrasVentaHaciaMain = findViewById(R.id.btn_venta_hacia_main);
        edtCodigo = findViewById(R.id.edt_codigo);
        edtNombre = findViewById(R.id.edt_nombre);
        edtPrecio = findViewById(R.id.edt_precio);
        edtStock = findViewById(R.id.edt_stock);



        btnAtrasVentaHaciaMain.setOnClickListener(view -> {
            Intent intent = new Intent(IngresarActivity.this, MainActivity.class);
            startActivity(intent);
        });

        btnIngresar.setOnClickListener(view -> ingresarProducto());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void ingresarProducto() {
        String codigo = edtCodigo.getText().toString().trim();
        String nombre = edtNombre.getText().toString().trim();
        String precio = edtPrecio.getText().toString().trim();
        String stock = edtStock.getText().toString().trim();

        if (codigo.isEmpty() || nombre.isEmpty() || precio.isEmpty() || stock.isEmpty()) {
            Toast.makeText(this, "Complete los campos porfis ≧ ﹏ ≦", Toast.LENGTH_SHORT).show();
            return;
        }

        try {

            Producto producto = new Producto(codigo, nombre, Double.parseDouble(precio), Integer.parseInt(stock));

            myRef.child(codigo).setValue(producto)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Producto ingresado con éxito.", Toast.LENGTH_SHORT).show();

                        edtCodigo.setText("");
                        edtNombre.setText("");
                        edtPrecio.setText("");
                        edtStock.setText("");
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Error al ingresar el producto ☠️☠️: " + e.getMessage(), Toast.LENGTH_SHORT).show());

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Revise los campos numéricos, algo no está bien ≧﹏≦", Toast.LENGTH_SHORT).show();
        }
    }



}