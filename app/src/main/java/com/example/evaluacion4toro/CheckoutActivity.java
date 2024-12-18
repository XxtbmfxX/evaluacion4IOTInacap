package com.example.evaluacion4toro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CheckoutActivity extends AppCompatActivity {
    TextView tvCodigo, tvNombre, tvPrecioUnitario, tvCantidad, tvPrecioTotal;
    Button btnVolverMain;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);


        tvCodigo = findViewById(R.id.tv_codigo);
        tvNombre = findViewById(R.id.tv_nombre);
        tvPrecioUnitario = findViewById(R.id.tv_precio_unitario);
        tvCantidad = findViewById(R.id.tv_cantidad);
        tvPrecioTotal = findViewById(R.id.tv_precio_total);
        btnVolverMain = findViewById(R.id.btn_volver_main);

        Intent intent = getIntent();
        String codigo = intent.getStringExtra("codigo");
        String nombre = intent.getStringExtra("nombre");
        double precio = intent.getDoubleExtra("precio", 0.0);
        int cantidad = intent.getIntExtra("cantidad", 0);
        double total = precio * cantidad;

        tvCodigo.setText("Código del producto: " + codigo);
        tvNombre.setText("Nombre del producto: " + nombre);
        tvPrecioUnitario.setText("Precio unitario: $" + precio);
        tvCantidad.setText("Cantidad vendida: " + cantidad);
        tvPrecioTotal.setText("Total a pagar: $" + total);

        btnVolverMain.setOnClickListener(view -> {
            Intent intentMain = new Intent(CheckoutActivity.this, MainActivity.class);
            startActivity(intentMain);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}