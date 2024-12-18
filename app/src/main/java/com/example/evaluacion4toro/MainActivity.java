package com.example.evaluacion4toro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
Button btnIngresarProducto, btnVenderProducto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        btnIngresarProducto = findViewById(R.id.btn_ingresar_producto);
        btnVenderProducto = findViewById(R.id.btn_vender_producto);

        btnIngresarProducto.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, IngresarActivity.class);
            startActivity(intent);
        });

        btnVenderProducto.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, VenderActivity.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}