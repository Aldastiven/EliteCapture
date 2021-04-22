package com.example.eliteCapture.Config.Util.Controls;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.example.eliteCapture.Camera;
import com.example.eliteCapture.Config.Util.Container.containerAdmin;
import com.example.eliteCapture.Model.Data.Tab.UsuarioTab;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CAM extends ContextWrapper {
    Context context;
    RespuestasTab rt;
    UsuarioTab usuario;
    String ubicacion, path, pathPhoto;
    int consecutivo;
    boolean vacio, initial;

    TextView respuestaPonderado;
    LinearLayout contenedorCamp, noti;
    containerAdmin ca;
    GIDGET pp;

    public CAM(Context context, String ubicacion, RespuestasTab rt, String path, boolean initial, UsuarioTab usuario, int consecutivo) {
        super(context);
        this.context = context;
        this.rt = rt;
        this.ubicacion = ubicacion;
        this.path = path;
        this.vacio = rt.getRespuesta() != null;
        this.initial = initial;
        this.usuario = usuario;
        this.consecutivo = consecutivo;
        ca = new containerAdmin(context);
        pp = new GIDGET(context, ubicacion, rt, path);

        respuestaPonderado = (TextView) pp.resultadoPonderado();
        respuestaPonderado.setText(vacio ? "Resultado : " + rt.getPonderado() : "Resultado :");

        noti = new LinearLayout(context);
        noti.setOrientation(LinearLayout.VERTICAL);
    }

    public String getPathPhoto() {
        return pathPhoto;
    }

    public void setPathPhoto(String pathPhoto) {
        this.pathPhoto = pathPhoto;
    }

    public View crear(){//GENERA EL CONTENEDOR DEL ITEM
        try {
            contenedorCamp = ca.container();
            contenedorCamp.setOrientation(LinearLayout.VERTICAL);
            contenedorCamp.setPadding(10, 0, 10, 0);
            contenedorCamp.setGravity(Gravity.CENTER_HORIZONTAL);

            contenedorCamp.addView(pp.Line(respuestaPonderado));//Crea la seccion de pregunta ponderado y resultado
            contenedorCamp.addView(campo());
            contenedorCamp.addView(noti);
            pp.validarColorContainer(contenedorCamp, vacio, initial);//pinta el contenedor del item si esta vacio o no

            return contenedorCamp;
        }catch (Exception e){
            return new GIDGET(context, "", null, path).problemCamp(rt.getTipo(), e.toString());
        }
    }

    public Button campo(){
        try {
            Button btn = (Button) pp.boton("Tomar foto", "verde");

            btn.setOnClickListener(V -> {
                getPolicy();
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getPhoto()));
                startActivity(i);
            });

            return btn;
        }catch (Exception e){
            Log.i("errorCam", "Error : "+e.toString());
            return null;
        }
    }

    public void getPolicy(){
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    @SuppressLint("SimpleDateFormat")
    public File getPhoto(){
        //Fecha, Formulario, Usuario, Consecutivo, idPregunta, # Imágen (Consecutivo)

        int countFiles = validateFiles();

        String  date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime()),
                dataItem = date+"_"+rt.getIdProceso()+"_"+usuario.getId_usuario()+"_"+consecutivo+"_"+rt.getId()+"_"+countFiles;

        return new File(getPathPhoto(), dataItem+".jpg");
    }

    public int validateFiles(){
        try{
            String pathImage = path+"/photos/"+rt.getIdProceso();
            int countFiles = 0;
            File file = new File(pathImage);

            if(file.exists()){
                String[] listFile = file.getAbsoluteFile().list();
                countFiles = listFile.length;
            }else{
                file.mkdirs();
            }
            setPathPhoto(pathImage);
            return countFiles;
        }catch (Exception e){
            Log.i("errorCam", "Error : "+e.toString());
            return 0;
        }
    }
}


