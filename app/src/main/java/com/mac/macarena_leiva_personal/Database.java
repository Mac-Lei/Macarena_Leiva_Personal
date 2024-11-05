package com.mac.macarena_leiva_personal;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

    private static final String Database_Name = "Mac_Lei.db";
    private static final String Table_name = "productos";
    private static final String Col_1 = "ID";
    private static final String Col_2 = "NOMBRE";
    private static final String Col_3 = "CANTIDAD";

    public Database(Context context) {
        super(context, Database_Name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + Table_name + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NOMBRE TEXT, CANTIDAD INTEGER)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Table_name);
        onCreate(db);
    }

    public boolean insertProducto(String nombre, int cantidad) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_2, nombre);
        contentValues.put(Col_3, cantidad);
        long result = db.insert(Table_name, null, contentValues);
        return result != -1;
    }

    public ArrayList<String> getAllProductos() {
        ArrayList<String> productos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + Table_name, null);
        if (res.moveToFirst()) {
            do {
                int id = res.getInt(res.getColumnIndexOrThrow(Col_1));
                String nombre = res.getString(res.getColumnIndexOrThrow(Col_2));
                int cantidad = res.getInt(res.getColumnIndexOrThrow(Col_3));
                productos.add("ID: " + id + ", Producto: " + nombre + ", Cantidad: " + cantidad);
            } while (res.moveToNext());
        }
        res.close();
        return productos;
    }

    public void deleteProducto(String nombre) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Table_name, "NOMBRE = ?", new String[]{nombre});
    }

    public void updateProducto(String oldNombre, String newNombre, int newCantidad) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_2, newNombre);
        contentValues.put(Col_3, newCantidad);
        db.update(Table_name, contentValues, "NOMBRE = ?", new String[]{oldNombre});
    }
}