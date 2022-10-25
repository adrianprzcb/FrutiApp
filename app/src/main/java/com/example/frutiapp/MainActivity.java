package com.example.frutiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText et_nombre;
    private ImageView iv_personaje;
    private TextView tv_bestScore;
    private MediaPlayer mp;

    int num_aleatorio = (int) (Math.random() * 4);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_nombre = findViewById(R.id.txtNombre);
        iv_personaje = findViewById(R.id.personaje);
        tv_bestScore = findViewById(R.id.bestScore);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

         imagenInicio();
         showBestScore();

         mp = MediaPlayer.create(this, R.raw.alphabet_song);
         mp.start();
         mp.setLooping(true);
    }

    public void showBestScore() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this , "BD" , null , 1);
        SQLiteDatabase BD = admin.getWritableDatabase();
        Cursor consulta = BD.rawQuery(
                "select * from puntaje where score = (select max(score) from puntaje)", null);
        if(consulta.moveToFirst()){
            String temp_nombre = consulta.getString(0);
            String temp_score = consulta.getString(1);
            tv_bestScore.setText("Record: " + temp_score + " de " + temp_nombre);
            BD.close();
        }else{
            Toast.makeText(this, "Nothing to show", Toast.LENGTH_SHORT).show();
            BD.close();
        }

    }

    public void imagenInicio(){
        int id;
        if(num_aleatorio == 0){
            id= getResources().getIdentifier("mango" , "drawable" , getPackageName());
            iv_personaje.setImageResource(id);
        }   else if(num_aleatorio == 1){
            id= getResources().getIdentifier("fresa" , "drawable" , getPackageName());
            iv_personaje.setImageResource(id);
        }
        else if(num_aleatorio == 2){
            id= getResources().getIdentifier("manzana" , "drawable" , getPackageName());
            iv_personaje.setImageResource(id);
        }
        else if(num_aleatorio == 3){
            id= getResources().getIdentifier("sandia" , "drawable" , getPackageName());
            iv_personaje.setImageResource(id);
        }
        else if(num_aleatorio == 4){
            id= getResources().getIdentifier("uva" , "drawable" , getPackageName());
            iv_personaje.setImageResource(id);
        }
    }


    public void jugar(View view){
        String nombre  = et_nombre.getText().toString();

        if(!nombre.equals("")){
            mp.stop();
            mp.release();
           Intent intent = new Intent(this, Nivel1.class);
           intent.putExtra("jugador" , nombre);
           startActivity(intent);
           finish();
        }else{
            Toast.makeText(this, "Debes introducir un nombre para jugar.", Toast.LENGTH_SHORT).show();

            et_nombre.requestFocus();
            InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            imm.showSoftInput(et_nombre , InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    public void onBackPressed(){

    }
}