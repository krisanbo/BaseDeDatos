package com.example.basededatos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String NombreBaseDeDatos ="administracion";
    private EditText txt_codigo,txt_descripcion,txt_precio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_codigo=(EditText) findViewById(R.id.txt_codigo);
        txt_descripcion=(EditText) findViewById(R.id.txt_descripcion);
        txt_precio=(EditText) findViewById(R.id.txt_precio);

    }
    // metodo para dar de alta los productos
    public void Registrar(View view){
        // instanciamos la clase de la base de datos, ( meter los cuatro atributos: donde, como se va a llamar,tipo bdatos=null,version)
        AdminSQLiteOpenHelper admin= new AdminSQLiteOpenHelper(this, NombreBaseDeDatos, null, 1);
        // abrir bd en modo lectura y escritura
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();
        // traer los codigos de los datos qye queremos meter en la tabla
        String codigo= txt_codigo.getText().toString();
        String descripcion= txt_descripcion.getText().toString();
        String precio= txt_precio.getText().toString();
        // condicional para asegurame que existan datos en los campos(!nombre.isEmpty, si esta lleno)
        if(!codigo.isEmpty() && !descripcion.isEmpty() &&!precio.isEmpty() ){
            // ejecutar el codigo, creamos un metodo para contener los datos
            ContentValues registro = new ContentValues();

            // metemos los datos
            registro.put("codigo", codigo);
            registro.put("descripcion", descripcion);
            registro.put("precio", precio);
            // insertar dentro de la tabla articulos los registros que hemos creado
            BaseDeDatos.insert("articulos", null, registro);
            // cerrar la base de datos
            BaseDeDatos.close();
            //limpiamos cambios
            txt_codigo.setText("");
            txt_descripcion.setText("");
            txt_precio.setText("");
            Toast.makeText(this, "el producto de ha grabado correctamente", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(this, "debes rellenar todos los campos", Toast.LENGTH_SHORT).show();
        }

    }

    // metodo buscar articulos
    public void Buscar(View view){
        //instanciar bd
        AdminSQLiteOpenHelper admin= new AdminSQLiteOpenHelper(this,NombreBaseDeDatos, null,1);
        //abrir base da datos modo lectura/escritura
        SQLiteDatabase BaseDeDatos= admin.getWritableDatabase();
        //traer el dato con el que voy a buscar
        String codigo= txt_codigo.getText().toString();
        // consdiconal para que codigo venga con datos
        if(!codigo.isEmpty()){
            // buscame y traeme lo que haya
            Cursor fila= BaseDeDatos.rawQuery("select descripcion, precio from articulos where codigo="+ codigo,null );
            //revisamos si la consulta tienes valores, cuantos valores hay
            //creamos condicional en caso de qye hay o no encontrado datos
            if(fila.moveToFirst()){
                //cogemos los datos qye van a venir en un array fil[0,1], 0=descrip y 1= precio
                txt_descripcion.setText(fila.getString(0));
                txt_precio.setText(fila.getString(1));
               // cerrar bd
                BaseDeDatos.close();
            }else{
                txt_descripcion.setText("");
                txt_precio.setText("");
                Toast.makeText(this, "El articulo no se ha encontrado", Toast.LENGTH_SHORT).show();
                BaseDeDatos.close();
            }


        }else{
            Toast.makeText(this, "debes meter un codigo", Toast.LENGTH_SHORT).show();
        }
    }

    //metodo para eliminar producto
    public void Eliminar (View view){
        //instanciar bd
        AdminSQLiteOpenHelper admin= new AdminSQLiteOpenHelper(this,NombreBaseDeDatos,null,1);
        //abrir bd
        SQLiteDatabase BaseDeDatos= admin.getWritableDatabase();
        // crear variable del codigo por el que vamos a buscar
        String codigo= txt_codigo.getText().toString();
        //condicional si esta lleno el campo
        if(!codigo.isEmpty()){
            //variable que nos cuenten los codigos que vamos a borrar
            int cantidad= BaseDeDatos.delete("articulos","codigo=" + codigo,null);
            BaseDeDatos.close();
            //para cpmprobar que se ha borrado creo un if para que me devuelva 1 si ha borrado algo
            if(cantidad==1){
                txt_descripcion.setText("");
                txt_precio.setText("");
                txt_codigo.setText("");
                Toast.makeText(this, "el articulo se ha borrado", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "el articulo no existe", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this, "debes introducir el codigo", Toast.LENGTH_SHORT).show();
        }

    }
    //metodo modificar articulos
    public void Modificar(View view){
        // instanciar bd
        AdminSQLiteOpenHelper admin= new AdminSQLiteOpenHelper(this,NombreBaseDeDatos,null,1);
        //abrir bd
        SQLiteDatabase BaseDeDatos= admin.getWritableDatabase();
        // traer todos los datos
        String codigo= txt_codigo.getText().toString();
        String descripcion= txt_descripcion.getText().toString();
        String precio= txt_precio.getText().toString();
        // nos aseguramos que los 3 datos tienen que tener datos
        if(!codigo.isEmpty() && !descripcion.isEmpty() && !precio.isEmpty()){
            // crear espacio en memoria para guardar los datos nuevos
            ContentValues registro = new ContentValues();
            // para meter los datos crear registros
            registro.put("codigo",codigo);
            registro.put("descripcion",descripcion);
            registro.put("precio",precio);
            // utilizamos el update para sobreescribir la linea
            //creamos variable int para ver los datos modificados
            int cantidad= BaseDeDatos.update("articulos",registro,"codigo =" + codigo,null);
            // cerramos base de datos
            BaseDeDatos.close();
            if(cantidad==1 ){
                Toast.makeText(this, "el articulo se ha modificado correctamente", Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(this, "el articulo no existe", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this, "tienes que rellenar todos los campos", Toast.LENGTH_SHORT).show();
        }

    }
    // metodo limpiar
    public void Limpiar(View view){

        txt_descripcion.setText("");
        txt_precio.setText("");
        txt_codigo.setText("");


    }

}