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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteCapture.Config.Util.Container.containerAdmin;
import com.example.eliteCapture.Config.ftpConect;
import com.example.eliteCapture.Model.Data.Tab.UsuarioTab;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.Model.View.iContenedor;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CAM extends ContextWrapper {
    Context context;
    RespuestasTab rt;
    UsuarioTab usuario;
    String ubicacion, path, pathPhoto, date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
    int consecutivo;
    boolean vacio, initial;

    TextView respuestaPonderado;
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
        try {
            //Fecha, Formulario, Usuario, Consecutivo, idPregunta, # Imágen (Consecutivo)

            int countFiles = validateFiles();
            String dataItem = date + "_" + rt.getIdProceso() + "_" + usuario.getId_usuario() + "_" + consecutivo + "_" + rt.getId() + "_" + countFiles;

            String resAnt = "";
            for (RespuestasTab r : icon.all().get(0).getQuestions()) {
                if (r.getIdPregunta() == r.getIdPregunta()) {
                    resAnt = r.getRespuesta();
                    break;
                }
            }
            String respuesta = (resAnt + ";" + dataItem),
                    valor = "Cantidad : " + (!resAnt.isEmpty() ? resAnt.split(";").length : 0);

            Log.i("dataRespuesta", "respuesta : "+respuesta+", valor : "+valor);
            Toast.makeText(context, "respuesta : "+respuesta+", valor : "+valor, Toast.LENGTH_SHORT).show();
            //registro(, );
            return new File(getPathPhoto(), dataItem+".png");
        }catch (Exception e) {
            Log.i("camPhoto", ""+e.toString());
            return new File("prueba");
        }
    }

    public int validateFiles(){
        try{
            String pathImage = path+"/photos/eliteCapture/"+rt.getIdProceso();
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
        Log.i("registro", "respuesta : " + rta);
        new iContenedor(path).editarTemporal(ubicacion, rt.getId().intValue(), rta, valor, null, rt.getReglas());
    }
}


