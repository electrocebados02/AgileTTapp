package com.example.owencaminos.agilett;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.os.Vibrator;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

@SuppressLint("HandlerLeak")


public class home extends Activity {


    //\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\
    // Debugging
    public static final String TAG = "LEDv0";
    public static final boolean D = true;
    // Tipos de mensaje enviados y recibidos desde el Handler de ConexionBT
    public static final int Mensaje_Estado_Cambiado = 1;
    public static final int Mensaje_Leido = 2;
    public static final int Mensaje_Escrito = 3;
    public static final int Mensaje_Nombre_Dispositivo = 4;
    public static final int Mensaje_TOAST = 5;
    public static final int MESSAGE_Desconectado = 6;
    public static final int REQUEST_ENABLE_BT = 7;

    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    //Nombre del dispositivo conectado
    private String mConnectedDeviceName = null;
    // Adaptador local Bluetooth
    private BluetoothAdapter AdaptadorBT = null;
    //Objeto miembro para el servicio de ConexionBT
    private ConexionBT Servicio_BT = null;
    //Vibrador
    private Vibrator vibrador;
    //variables para el Menu de conexion
    private boolean seleccionador=false;
    public int Opcion=R.menu.activity_main;
    //\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        //DEJAMOS LA PANTALLA VERTICAL...
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    //INTRODUCIMOS LA CONF DE BT
    public  void onStart() {
        super.onStart();
        ConfigBT();
    }

    @Override
    protected void onResume() {
        super.onResume();




        // Obtenemos el adaptador de bluetooth
        AdaptadorBT = BluetoothAdapter.getDefaultAdapter();
        if (AdaptadorBT.isEnabled()) {//Si el BT esta encendido,





        if (Servicio_BT.getState() == ConexionBT.STATE_CONNECTED) {//checa si estamos conectados a BT
            Log.e(TAG, "opcion 1");
           //MOSTRAMOS MENSAJE DE BIENVENIDA
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Por favor use los comandos con precaución!. El uso de este dispositivo es responsabilidad del usuario.")
                    .setTitle("Bienvenido!")
                    .setCancelable(false)
                    .setNeutralButton("Aceptar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
            //fin msj bienvenida
        } else{
            Log.e(TAG, "opcion 2");
       //SI NO ESTAMOS CONECTADOS PREGUNTAMOS
            //PRIMERO PREGUNTAMOS SI DESEA CONECTAr todook
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
                                    if(D) Log.e("conexion", "conectandonos");
                                    vibrador = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                                    vibrador.vibrate(1000);
                                    String address = "20:16:06:06:17:44";//Direccion Mac del  rn42
                                    BluetoothDevice device = AdaptadorBT.getRemoteDevice(address);
                                    Servicio_BT.connect(device);

                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
            //FIN PREG

        }

        }



    }



    ///////////////////////////////////////
    //ENTRE


    @Override
    public void onDestroy(){
        super.onDestroy();
        if (Servicio_BT != null) Servicio_BT.stop();//Detenemos servicio
    }

    public void ConfigBT(){
        // Obtenemos el adaptador de bluetooth
        AdaptadorBT = BluetoothAdapter.getDefaultAdapter();
        if (AdaptadorBT.isEnabled()) {//Si el BT esta encendido,
            if (Servicio_BT == null) {//y el Servicio_BT es nulo, invocamos el Servicio_BT
                Servicio_BT = new ConexionBT(this, mHandler);
            }
        }
        else{ if(D) Log.e("Setup", "Bluetooth apagado...");
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, REQUEST_ENABLE_BT);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Una vez que se ha realizado una actividad regresa un "resultado"...
        switch (requestCode) {

            case REQUEST_ENABLE_BT://Respuesta de intento de encendido de BT
                if (resultCode == Activity.RESULT_OK) {//BT esta activado,iniciamos servicio
                    ConfigBT();
                } else {//No se activo BT, salimos de la app
                    finish();}



        }//fin de switch case
    }//fin de onActivityResult



    @Override
    public boolean onPrepareOptionsMenu(Menu menux){
        //cada vez que se presiona la tecla menu  este metodo es llamado
        menux.clear();//limpiamos menu actual
        if (seleccionador==false)Opcion=R.menu.activity_main;//dependiendo las necesidades
        if (seleccionador==true)Opcion=R.menu.desconecta;  // crearemos un menu diferente
        getMenuInflater().inflate(Opcion, menux);
        return super.onPrepareOptionsMenu(menux);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Conexion:
                if(D) Log.e("conexion", "conectandonos");
                vibrador = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrador.vibrate(1000);
                String address = "20:16:06:06:17:44";//Direccion Mac del  rn42
                BluetoothDevice device = AdaptadorBT.getRemoteDevice(address);
                Servicio_BT.connect(device);
                return true;

            case R.id.desconexion:
                if (Servicio_BT != null) Servicio_BT.stop();//Detenemos servicio
                return true;
        }//fin de swtich de opciones
        return false;
    }//fin de metodo onOptionsItemSelected



    public  void sendMessage(String message) {
        if (Servicio_BT.getState() == ConexionBT.STATE_CONNECTED) {//checa si estamos conectados a BT
            if (message.length() > 0) {   // checa si hay algo que enviar
                byte[] send = message.getBytes();//Obtenemos bytes del mensaje
                if(D) Log.e(TAG, "Mensaje enviado:"+ message);
                Servicio_BT.write(send);     //Mandamos a escribir el mensaje
            }
        } else Toast.makeText(this, "No conectado", Toast.LENGTH_SHORT).show();
    }//fin de sendMessage



    final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                case Mensaje_Escrito:
                    byte[] writeBuf = (byte[]) msg.obj;//buffer de escritura...
                    // Construye un String del Buffer
                    String writeMessage = new String(writeBuf);
                    if(D) Log.e(TAG, "Message_write  =w= "+ writeMessage);
                    break;
                //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                case Mensaje_Leido:
                    byte[] readBuf = (byte[]) msg.obj;//buffer de lectura...
                    //Construye un String de los bytes validos en el buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    if(D) Log.e(TAG, "Message_read   =w= "+ readMessage);
                    break;
                //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                case Mensaje_Nombre_Dispositivo:
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME); //Guardamos nombre del dispositivo
                    Toast.makeText(getApplicationContext(), "Conectado con "+ mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    seleccionador=true;
                    break;
                //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                case Mensaje_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
                //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                case MESSAGE_Desconectado:
                    if(D) Log.e("Conexion","DESConectados");
                    seleccionador=false;
                    break;
                //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
            }//FIN DE SWITCH CASE PRIMARIO DEL HANDLER
        }//FIN DE METODO INTERNO handleMessage
    };//Fin de Handler

    //FENTE



    //fintodo
}
