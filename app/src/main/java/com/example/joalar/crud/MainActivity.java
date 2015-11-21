package com.example.joalar.crud;

import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public DBManager dbManager;
    public ListView lstViewAprendices;
    public Cursor cursorLstAprendices;
    public SimpleCursorAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init_tabhost();
        init_spinner();
        dbManager = new DBManager(this);
        /*dbManager.insertar("Jose","Arias","Calle x","24");
        dbManager.insertar("Pepito","Perez","Calle z","20");
        dbManager.insertar("Pedro","Pinto","Calle a","30");
        dbManager.insertar("Carlos","Arango","Calle m","29");
        dbManager.insertar("Jairo","Caramgo","Calle h","40");*/
        consultar();
    }

    public void init_tabhost() {
        //comienza inicializacion de tab host
        TabHost tabHost = (TabHost)findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec spec;

        spec = tabHost.newTabSpec("Insertar");
        spec.setContent(R.id.tab1);
        spec.setIndicator("", ContextCompat.getDrawable(this,R.drawable.add_user));
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("Eliminar");
        spec.setContent(R.id.tab2);
        spec.setIndicator("", ContextCompat.getDrawable(this,R.drawable.delete_user));
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("Actualizar");
        spec.setContent(R.id.tab3);
        spec.setIndicator("", ContextCompat.getDrawable(this,R.drawable.edit_profile));
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("Buscar");
        spec.setContent(R.id.tab4);
        spec.setIndicator("", ContextCompat.getDrawable(this,R.drawable.search_user));
        tabHost.addTab(spec);
        //termina inicializacion TabHost
    }

    public void init_spinner()
    {
        String [] items = new String[]{
                "Nombre",
                "Apellido",
                "Direccion",
                "Edad"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner)findViewById(R.id.spinnerDelParametro);
        spinner.setAdapter(adapter);
        spinner = (Spinner)findViewById(R.id.spinnerUp1Parametro);
        spinner.setAdapter(adapter);
        spinner = (Spinner)findViewById(R.id.spinnerUp2Parametro);
        spinner.setAdapter(adapter);
        spinner = (Spinner)findViewById(R.id.spinnerSeaParametro);
        spinner.setAdapter(adapter);
    }

    public void consultar() {
        cursorLstAprendices = dbManager.cursorConsultaAprendices();
        //arreglo con los campos de la bd
        String[] from = new String[]{DBManager.cnNombre,DBManager.cnEdad,DBManager.cnApellido, DBManager.cnDirección};
        //arreglo con los id del layout
        int[] to = new int[]{R.id.txtNombre,R.id.txtEdad,R.id.txtApellido,R.id.txtDireccion};
        simpleAdapter = new SimpleCursorAdapter(this,R.layout.lista,cursorLstAprendices,from,to,0);
        lstViewAprendices = (ListView)findViewById(R.id.listViewLstAprendices);
        lstViewAprendices.setAdapter(simpleAdapter);
    }

    public void insertar(View v) {
        String nombre = ((EditText)findViewById(R.id.edtTextAddNombre)).getText().toString();
        String apellido = ((EditText)findViewById(R.id.edtTextAddApellido)).getText().toString();
        String direccion = ((EditText)findViewById(R.id.edtTextAddDireccion)).getText().toString();
        String edad = ((EditText)findViewById(R.id.edtTextAddEdad)).getText().toString();
        if(!(nombre.equals("")&&apellido.equals("")&&direccion.equals(""))) {
            if (dbManager.insertar(nombre, apellido, direccion, edad) == -1) {
                Toast.makeText(this, "Se ha producido un error", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Se ha insertado el registro", Toast.LENGTH_SHORT).show();
            }
            consultar();
            ((EditText) findViewById(R.id.edtTextAddNombre)).setText("");
            ((EditText) findViewById(R.id.edtTextAddApellido)).setText("");
            ((EditText) findViewById(R.id.edtTextAddDireccion)).setText("");
            ((EditText) findViewById(R.id.edtTextAddEdad)).setText("");
        } else {
            Toast.makeText(this, "Debe ingresar Nombre, Apellido y Dirección", Toast.LENGTH_SHORT).show();
        }
    }

    public void eliminar(View v){
        String valor = ((EditText)findViewById(R.id.edtTextDelValor)).getText().toString();
        int parametro = ((Spinner)findViewById(R.id.spinnerDelParametro)).getSelectedItemPosition();
        int count = 0;
        switch(parametro)
        {
            case 0:
                count = dbManager.eliminar(DBManager.cnNombre,valor);
                break;
            case 1:
                count = dbManager.eliminar(DBManager.cnApellido,valor);
                break;
            case 2:
                count = dbManager.eliminar(DBManager.cnDirección,valor);
                break;
            case 3:
                count = dbManager.eliminar(DBManager.cnEdad,valor);
                break;
        }
        consultar();
        Toast.makeText(this, "Se ha(n) eliminado "+count+" registro(s)", Toast.LENGTH_SHORT).show();
        ((Spinner) findViewById(R.id.spinnerDelParametro)).setSelection(0);
        ((EditText) findViewById(R.id.edtTextDelValor)).setText("");
    }

    public void actualizar(View v){
        String valor1 = ((EditText)findViewById(R.id.edtTextUp1Valor)).getText().toString();
        int parametro1 = ((Spinner)findViewById(R.id.spinnerUp1Parametro)).getSelectedItemPosition();
        String valor2 = ((EditText)findViewById(R.id.edtTextUp2Valor)).getText().toString();
        int parametro2 = ((Spinner)findViewById(R.id.spinnerUp2Parametro)).getSelectedItemPosition();
        int count = 0;
        switch (parametro1)
        {
            case 0:
                switch (parametro2) {
                    case 0:
                        count = dbManager.actualizar(DBManager.cnNombre, valor1, DBManager.cnNombre,valor2);
                        break;
                    case 1:
                        count = dbManager.actualizar(DBManager.cnNombre, valor1, DBManager.cnApellido,valor2);
                        break;
                    case 2:
                        count = dbManager.actualizar(DBManager.cnNombre, valor1, DBManager.cnDirección,valor2);
                        break;
                    case 3:
                        count = dbManager.actualizar(DBManager.cnNombre, valor1, DBManager.cnEdad,valor2);
                        break;
                }
                break;
            case 1:
                switch (parametro2) {
                    case 0:
                        count = dbManager.actualizar(DBManager.cnApellido, valor1, DBManager.cnNombre,valor2);
                        break;
                    case 1:
                        count = dbManager.actualizar(DBManager.cnApellido, valor1, DBManager.cnApellido,valor2);
                        break;
                    case 2:
                        count = dbManager.actualizar(DBManager.cnApellido, valor1, DBManager.cnDirección,valor2);
                        break;
                    case 3:
                        count = dbManager.actualizar(DBManager.cnApellido, valor1, DBManager.cnEdad,valor2);
                        break;
                }
                break;
            case 2:
                switch (parametro2) {
                    case 0:
                        count = dbManager.actualizar(DBManager.cnDirección, valor1, DBManager.cnNombre,valor2);
                        break;
                    case 1:
                        count = dbManager.actualizar(DBManager.cnDirección, valor1, DBManager.cnApellido,valor2);
                        break;
                    case 2:
                        count = dbManager.actualizar(DBManager.cnDirección, valor1, DBManager.cnDirección,valor2);
                        break;
                    case 3:
                        count = dbManager.actualizar(DBManager.cnDirección, valor1, DBManager.cnEdad,valor2);
                        break;
                }
                break;
            case 3:
                switch (parametro2) {
                    case 0:
                        count = dbManager.actualizar(DBManager.cnEdad, valor1, DBManager.cnNombre,valor2);
                        break;
                    case 1:
                        count = dbManager.actualizar(DBManager.cnEdad, valor1, DBManager.cnApellido,valor2);
                        break;
                    case 2:
                        count = dbManager.actualizar(DBManager.cnEdad, valor1, DBManager.cnDirección,valor2);
                        break;
                    case 3:
                        count = dbManager.actualizar(DBManager.cnEdad, valor1, DBManager.cnEdad,valor2);
                        break;
                }
                break;
        }
        consultar();
        Toast.makeText(this, "Se ha(n) actualizado "+count+" registro(s)", Toast.LENGTH_SHORT).show();
        ((Spinner) findViewById(R.id.spinnerUp1Parametro)).setSelection(0);
        ((EditText) findViewById(R.id.edtTextUp1Valor)).setText("");
        ((Spinner) findViewById(R.id.spinnerUp2Parametro)).setSelection(0);
        ((EditText) findViewById(R.id.edtTextUp2Valor)).setText("");
    }

    public void consultar(View v)
    {
        String valor = ((EditText)findViewById(R.id.edtTextSeaValor)).getText().toString();
        int parametro = ((Spinner)findViewById(R.id.spinnerSeaParametro)).getSelectedItemPosition();

        switch(parametro)
        {
            case 0:
                cursorLstAprendices = dbManager.cursorConsultaAprendices(DBManager.cnNombre,valor);
                break;
            case 1:
                cursorLstAprendices = dbManager.cursorConsultaAprendices(DBManager.cnApellido,valor);
                break;
            case 2:
                cursorLstAprendices = dbManager.cursorConsultaAprendices(DBManager.cnDirección, valor);
                break;
            case 3:
                cursorLstAprendices = dbManager.cursorConsultaAprendices(DBManager.cnEdad, valor);
                break;
        }

        //arreglo con los campos de la bd
        String[] from = new String[]{DBManager.cnNombre,DBManager.cnEdad,DBManager.cnApellido, DBManager.cnDirección};
        //arreglo con los id del layout
        int[] to = new int[]{R.id.txtNombre,R.id.txtEdad,R.id.txtApellido,R.id.txtDireccion};
        simpleAdapter = new SimpleCursorAdapter(this,R.layout.lista,cursorLstAprendices,from,to,0);
        lstViewAprendices = (ListView)findViewById(R.id.listViewLstAprendices);
        lstViewAprendices.setAdapter(simpleAdapter);
    }
}
