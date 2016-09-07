package com.example.owencaminos.agilett;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

/**
 * Created by owen.caminos on 01/09/2016.
 */
public class opciones extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opciones);

        //DEJAMOS LA PANTALLA VERTICAL...
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
