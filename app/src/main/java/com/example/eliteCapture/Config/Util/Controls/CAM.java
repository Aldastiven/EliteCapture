package com.example.eliteCapture.Config.Util.Controls;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteCapture.Config.Util.Container.containerAdmin;
import com.example.eliteCapture.Config.ftpConect;
import com.example.eliteCapture.Model.Data.Tab.UsuarioTab;
import com.example.eliteCapture.Model.View.Interfaz.Respuestas;
import com.example.eliteCapture.Model.View.Tab.ContenedorTab;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.Model.View.iContenedor;

import org.w3c.dom.Text;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class CAM extends ContextWrapper {
    Context context;
    RespuestasTab rt;
    UsuarioTab usuario;
    String ubicacion, path, pathPhoto, date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
    int consecutivo;
    boolean vacio, initial;

    TextView respuestaPonderado, txtCantidadPhoto;
    LinearLayout contenedorCamp, noti;
    containerAdmin ca;
    GIDGET pp;

    iContenedor icon;

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
        icon = new iContenedor(path);
        respuestaPonderado = (TextView) pp.resultadoPonderado();
        respuestaPonderado.setText(vacio ? "Resultado : " + rt.getPonderado() : "Resultado :");

        noti = new LinearLayout(context);
        noti.setOrientation(LinearLayout.VERTICAL);
        validateFiles();
    }

    public String getPathPhoto() {

        return Environment.getExternalStorageDirectory()+ "/" + Environment.DIRECTORY_DCIM;// + "/Camera/Elite/";
        //           return pathPhoto;
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

    public LinearLayout campo(){
        try {
            LinearLayout line = ca.container();
            line.setOrientation(LinearLayout.VERTICAL);

            txtCantidadPhoto = (TextView) pp.campoEdtable("TextView", "#000000");
            txtCantidadPhoto.setText(vacio ? "Cantidad de fotos : "+rt.getValor() : "");

            Button btn = (Button) pp.boton("Tomar foto", "verde");
            btn.setLayoutParams(ca.params());

            btn.setOnClickListener(V -> {
                getPolicy();
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
                String respuesta = "EC-" +rt.getIdProceso() +"-"+ UUID.randomUUID().toString()+ ".jpg";
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getPhoto(respuesta)));
                startActivity(i);
            });

            line.addView(txtCantidadPhoto);
            line.addView(btn);
            return line;
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
    public File getPhoto(String respuesta){
        try {
            validateFiles();

            String dataRespuesta = null;
            int dataValor = 0;

            dataRespuesta =  dataRespuesta != null ? dataRespuesta+respuesta+";" : respuesta+";";
            String valor = "" + (dataValor + 1);

            registro(dataRespuesta, valor);

            txtCantidadPhoto.setText("Cantidad de fotos: "+valor);

            File f = new File(getPathPhoto() + "/fotos", respuesta);

            Toast.makeText(context, "fotografia creada : "+f.getAbsolutePath(), Toast.LENGTH_LONG).show();

            return f;
        }catch (Exception e) {
            Log.i("camPhoto", ""+e.toString());
            return new File("prueba");
        }
    }

    public int validateFiles(){
        try{
            String pathImage = getPathPhoto()+"/fotos/";
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


    public void registro(String rta, String valor) {//REGISTRO
        new iContenedor(path).editarTemporal(ubicacion, rt.getId().intValue(), rta, valor, null, rt.getReglas());
    }
}


