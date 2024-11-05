package com.mac.macarena_leiva_personal;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Database database;
    EditText ingresarNombre, ingresarCantidad, editarNombre, editarCantidad;
    Button btnAgregar, btnEditar, btnEliminar;
    ListView listarProductos;
    ArrayList<String> productos;
    ArrayAdapter<String> adapter;
    String productoSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new Database(this);
        ingresarNombre = findViewById(R.id.editTextTask);
        ingresarCantidad = findViewById(R.id.editTextCantidad);
        editarNombre = findViewById(R.id.editTextEditTask);
        editarCantidad = findViewById(R.id.editTextEditCantidad);
        btnAgregar = findViewById(R.id.buttonAdd);
        btnEditar = findViewById(R.id.buttonEdit);
        btnEliminar = findViewById(R.id.buttonDelete);
        listarProductos = findViewById(R.id.listViewTasks);

        productos = database.getAllProductos();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productos);
        listarProductos.setAdapter(adapter);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = ingresarNombre.getText().toString();
                String cantidades = ingresarCantidad.getText().toString();
                if (!nombre.isEmpty() && !cantidades.isEmpty()) {
                    int cantidad = Integer.parseInt(cantidades);
                    database.insertProducto(nombre, cantidad);
                    ActualizarListadoProductos();
                    ingresarNombre.setText("");
                    ingresarCantidad.setText("");
                }
            }
        });

        listarProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
                productoSeleccionado = productos.get(position);
                String[] partes = productoSeleccionado.split(", ");
                editarNombre.setText(partes[1].split(": ")[1]);
                editarCantidad.setText(partes[2].split(": ")[1]);
                editarNombre.setVisibility(View.VISIBLE);
                editarCantidad.setVisibility(View.VISIBLE);
                btnEditar.setVisibility(View.VISIBLE);
                btnEliminar.setVisibility(View.VISIBLE);
            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nuevoNombre = editarNombre.getText().toString();
                String nuevaCantidades = editarCantidad.getText().toString();
                if (!nuevoNombre.isEmpty() && !nuevaCantidades.isEmpty()) {
                    int nuevaCantidad = Integer.parseInt(nuevaCantidades);
                    database.updateProducto(productoSeleccionado.split(", ")[1].split(": ")[1], nuevoNombre, nuevaCantidad);
                    ActualizarListadoProductos();
                    LimpiarCampos();
                }
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.deleteProducto(productoSeleccionado.split(", ")[1].split(": ")[1]);
                ActualizarListadoProductos();
                LimpiarCampos();
            }
        });
    }

    public void ActualizarListadoProductos() {
        productos.clear();
        productos.addAll(database.getAllProductos());
        adapter.notifyDataSetChanged();
    }

    public void LimpiarCampos() {
        editarNombre.setText("");
        editarCantidad.setText("");
        editarNombre.setVisibility(View.GONE);
        editarCantidad.setVisibility(View.GONE);
        btnEditar.setVisibility(View.GONE);
        btnEliminar.setVisibility(View.GONE);
    }
}
