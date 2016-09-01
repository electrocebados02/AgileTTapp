package com.example.owencaminos.agilett;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by owen.caminos on 01/09/2016.
 */
public class home extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
    }

    @Override
    protected void onResume() {
        super.onResume();


//PRIMERO PREGUNTAMOS SI DESEA CONECTar bm
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Desea conectar el dispositivo ahora?")
                .setTitle("Conexión")
                .setCancelable(false)
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setPositiveButton("Conectar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                               // TransferirDinero(); // metodo que se debe implementar
                                Toast toast = Toast.makeText(getApplicationContext(), "Conectando el dispositivo...", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();

    }
}
