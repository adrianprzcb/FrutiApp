package com.example.frutiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Nivel1 extends AppCompatActivity {


    private TextView tvNombre, tvScore;
    private ImageView iv_Auno , iv_Ados , iv_vidas;
    private EditText etRespuesta;
    private MediaPlayer mp, mpGreat , mpBad;
    int score, numAleatorioUno , numAleatorioDos, resultado, vidas;
    String nombreJugador, string_score, string_vidas;

    String numero[] = {"cero" , "uno" , "dos" , "tres" , "cuatro", "cinco" , "seis", "siete", "ocho" , "nueve"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nivel1);

        tvScore = findViewById(R.id.tv_score);
        tvNombre = findViewById(R.id.tv_jugador);
        iv_vidas = findViewById(R.id.imageViewVidas);
        iv_Auno = findViewById(R.id.numUno);
        iv_Ados = findViewById(R.id.numDos);
        etRespuesta = findViewById(R.id.resultado);
        vidas =3;
        nombreJugador = getIntent().getStringExtra("jugador");
        tvNombre.setText("Jugador: " + nombreJugador);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        Toast.makeText(this, "Nivel 1 - Sumas Básicas", Toast.LENGTH_SHORT).show();

        //Creacion de sonidos
        mp = MediaPlayer.create(this, R.raw.goats);
        mp.start();
        mp.setLooping(true);
        mpGreat = MediaPlayer.create(this, R.raw.wonderful);
        mpBad = MediaPlayer.create(this, R.raw.bad);

        numAleatorio();

    }

    public void comparar(View view){
        String respuesta = etRespuesta.getText().toString();

        if(!respuesta.equals("")){
            int respuestaInt = Integer.parseInt(respuesta);
            if(resultado == respuestaInt){
                mpGreat.start();
                score++;
                tvScore.setText("Score:" + score);
                etRespuesta.setText("");
                baseDeDatos();
            } else{
                mpBad.start();
                vidas = vidas -1;

                switch (vidas){
                    case 3:
                        iv_vidas.setImageResource(R.drawable.tresvidas);
                        break;
                    case 2:
                        Toast.makeText(this, "Te quedan dos manzanas", Toast.LENGTH_SHORT).show();
                        iv_vidas.setImageResource(R.drawable.dosvidas);
                        break;
                    case 1:
                        Toast.makeText(this, "Te queda una manzana", Toast.LENGTH_SHORT).show();
                        iv_vidas.setImageResource(R.drawable.unavida);
                        break;
                    case 0:
                        Toast.makeText(this, "Has perdido todas tus manzanas", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        mp.stop();
                        mp.release();
                        break;
                }
                etRespuesta.setText("");

            }
            numAleatorio();
        }else{
            Toast.makeText(this, "¡Escribe tu respuesta!", Toast.LENGTH_SHORT).show();
        }
    }


    public void numAleatorio(){
        if(score <=9){
            numAleatorioUno = (int) (Math.random() * 10);
            numAleatorioDos = (int) (Math.random() * 10);

            resultado = numAleatorioUno + numAleatorioDos;

            if(resultado<= 10){
                for (int i=0; i<numero.length; i++){
                    int id = getResources().getIdentifier(numero[i] , "drawable" , getPackageName());
                    if(numAleatorioUno == i){
                        iv_Auno.setImageResource(id);
                    }
                    if(numAleatorioDos == i){
                        iv_Ados.setImageResource(id);
                    }
                }
            }else{
                    numAleatorio();
            }
        }else{
            Intent intent = new Intent(this, Nivel2.class);
            string_score = String.valueOf(score);
            string_vidas = String.valueOf(vidas);
            intent.putExtra("jugador" , nombreJugador);
            intent.putExtra("score" , string_score);
            intent.putExtra("vidas" , string_vidas);

            startActivity(intent);
            finish();
            mp.stop();
            mp.release();

        }
    }


    public void baseDeDatos(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "BD", null, 1);
        SQLiteDatabase BD = admin.getWritableDatabase();

        Cursor consulta = BD.rawQuery("select * from puntaje where score = (select max(score) from puntaje)", null);
        if(consulta.moveToFirst()){
            String temp_nombre = consulta.getString(0);
            String temp_score = consulta.getString(1);

            int bestScore = Integer.parseInt(temp_score);

            if(score > bestScore){
                ContentValues modificacion = new ContentValues();
                modificacion.put("name", nombreJugador);
                modificacion.put("score", score);

                BD.update("puntaje", modificacion, "score=" + bestScore, null);
            }

            BD.close();

        } else {
            ContentValues insertar = new ContentValues();

            insertar.put("name", nombreJugador);
            insertar.put("score", score);

            BD.insert("puntaje", null, insertar);
            BD.close();
        }
    }

@Override
    public void onBackPressed(){

  }

}