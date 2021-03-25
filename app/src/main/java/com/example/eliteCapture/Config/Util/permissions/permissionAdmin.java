package com.example.eliteCapture.Config.Util.permissions;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.eliteCapture.Login;
import com.example.eliteCapture.R;

import java.util.ArrayList;
import java.util.List;

public class permissionAdmin {
    //En esta clase se realiza la administración de permisos
    Context context;
    Activity activity;
    Dialog d;
    LinearLayout linearLayout, containtModal;

    String camara = "Camara", almacenamiento = "Almacenamiento";

    public permissionAdmin(Activity activity) {
        this.context = activity.getApplicationContext();
        this.activity = activity;

         //containtModal = new widgets(context).container();
    }

    public boolean validarPermisos(){
        boolean b = false;
        List<String> solicitudes = validatePermissions();
        if(solicitudes.size() > 0) {
            for (String d : solicitudes) {
                /*Button btn = new widgets(context).button("Conceder permisoso de "+d);
                containtModal.addView(btn);
                functionButton(btn, d);*/
            }
            crear(containtModal);
            b = true;
        }
        return b;
    }
    public List<String> validatePermissions(){
        List<String> solicitudes = new ArrayList<>();
        int estadoDePermisoCamera = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        int estadoDePermisoStorage = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(estadoDePermisoCamera == -1) {
            solicitudes.add(camara);
        }

        if(estadoDePermisoStorage == -1) {
            solicitudes.add(almacenamiento);
        }
        return solicitudes;
    }

    public void crear(LinearLayout line){
        d = new Dialog(activity);
        //d.setContentView(R.layout.modal);

        linearLayout = d.findViewById(R.id.linearMod);

        linearLayout.addView(line);

        Window windowfinca = d.getWindow();
        windowfinca.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        windowfinca.getAttributes().gravity = Gravity.TOP;
        d.show();
    }



    //LLAMADO DE PEMISOS

    public void functionButton(Button btn, String type){
        btn.setOnClickListener(view -> {
            switch (type){
                case  "Camara" :
                    permissionGrantedCamera();
                    break;
                case  "Almacenamiento" :
                    permissionGrantedStorage();
                    break;
            }
        });
    }

    public int getPermissionCamera(){
        int p = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        Log.i("PermissionData", ""+p);
        return p;
    }

    public void permissionGrantedStorage(){
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 225);
    }

    public void permissionGrantedCamera(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
            Log.i("permission", "llego a ser verdadero");

        }else{
            Log.i("permission", "llego a ser falso");
            showDialogOK("SMS and Location Services Permission required for this app",
                    (dialog, which) -> {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //checkAndRequestPermissions();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                // proceed with logic by disabling the related features or quit the app.
                                break;
                        }
                    });
        }

        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, 226);
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }
}
