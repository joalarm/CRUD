package com.example.joalar.crud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.widget.Toast;

/**
 * Created by Joalar on 07/11/2015.
 */
public class DBManager {
    public final static String nombreTabla = "Aprendices";
    public final static String cnId = "_id";
    public final static String cnNombre = "Nombre";
    public final static String cnApellido = "Apellido";
    public final static String cnDirección = "Dirección";
    public final static String cnEdad = "Edad";

    /* create table Aprendices (
        _ID integer primary key autoincrement,
        Nombre text not null,
        Apellido text not null,
        Direccion text not null,
        Edad text );
     */

    public final static String crearTabla =
            "create table "+nombreTabla+" ("
                    +cnId+" integer primary key autoincrement,"
                    +cnNombre+" text not null,"
                    +cnApellido+" text not null,"
                    +cnDirección+" text not null,"
                    +cnEdad+" text);";

    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context)
    {
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    public long insertar(String Nombre, String Apellido, String Dirección, String Edad)
    {
        ContentValues values = getContentValues(Nombre, Apellido, Dirección, Edad);
        return db.insert(nombreTabla,null,values);
    }

    @NonNull
    private ContentValues getContentValues(String Nombre, String Apellido, String Dirección, String Edad) {
        ContentValues values = new ContentValues();
        values.put(cnNombre,Nombre);
        values.put(cnApellido,Apellido);
        values.put(cnDirección,Dirección);
        values.put(cnEdad,Edad);
        return values;
    }

    public Cursor cursorConsultaAprendices()
    {
        String[] columns = new String[]{cnId,cnNombre,cnApellido,cnDirección,cnEdad};
        return db.query(nombreTabla,columns,null,null,null,null,null);
    }

    public Cursor cursorConsultaAprendices(String parametro, String valor)
    {
        String[] columns = new String[]{cnId,cnNombre,cnApellido,cnDirección,cnEdad};
        return db.query(nombreTabla,columns,parametro+"=?",new String[]{valor},null,null,null);
    }

    public int eliminar(String parametro, String valor)
    {
        return db.delete(nombreTabla, parametro+"=?", new String[]{valor});
    }

    public int actualizar(String columnap, String parametro, String columnav, String valor)
    {
        ContentValues values = new ContentValues();
        values.put(columnav, valor);
        return db.update(nombreTabla,values,columnap+"=?",new String[]{parametro});
    }
}