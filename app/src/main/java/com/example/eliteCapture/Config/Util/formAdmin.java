package com.example.eliteCapture.Config.Util;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.eliteCapture.Config.Util.ControlViews.CVariedadJson;
import com.example.eliteCapture.Config.Util.ControlViews.Cconteos;
import com.example.eliteCapture.Config.Util.ControlViews.CconteosCheck;
import com.example.eliteCapture.Config.Util.ControlViews.CconteosEditar;
import com.example.eliteCapture.Config.Util.ControlViews.CdespelgableQ;
import com.example.eliteCapture.Config.Util.ControlViews.Cdesplegable;
import com.example.eliteCapture.Config.Util.ControlViews.Cetalf;
import com.example.eliteCapture.Config.Util.ControlViews.Cetnum;
import com.example.eliteCapture.Config.Util.ControlViews.CfilAuto;
import com.example.eliteCapture.Config.Util.ControlViews.Cfiltro;
import com.example.eliteCapture.Config.Util.ControlViews.CradioButton;
import com.example.eliteCapture.Config.Util.ControlViews.Cscanner;
import com.example.eliteCapture.Config.Util.ControlViews.Ctextview;
import com.example.eliteCapture.Model.Data.Admin;
import com.example.eliteCapture.Model.Data.Tab.ProcesoTab;
import com.example.eliteCapture.Model.Data.Tab.UsuarioTab;
import com.example.eliteCapture.Model.View.Interfaz.Contenedor;
import com.example.eliteCapture.Model.View.Tab.ContenedorTab;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.Model.View.iContenedor;
import com.example.eliteCapture.Model.View.iRespuestas;
import com.example.eliteCapture.R;
import com.example.eliteCapture.genated;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class formAdmin {

    LinearLayout linearPrinc;
    Context context;
    String path;
    Boolean inicial;
    Dialog popregla;

    iContenedor iCon;

    ContenedorTab contenedor;
    ProcesoTab pro = null;
    UsuarioTab usu = null;

    SharedPreferences sp;
    Admin adm = null;

    boolean temporal;
    int estado = 1;

    public formAdmin(LinearLayout linearPrinc, Context context, String path, Boolean inicial, int estado) {
        try {
            this.linearPrinc = linearPrinc;
            this.context = context;
            this.path = path;
            this.inicial = inicial;
            this.estado = estado;
            sp = context.getSharedPreferences("share", context.MODE_PRIVATE);
            adm = new Admin(null, path);//administra la conexion de las entidades

            inicializarPopRegla(); //inicicaliza el Dialog para el campo de RSE que edita la regla del conteo
            iCon = new iContenedor(path);

            getcodProceso_Usuario();

            contenedor = validarTemporal();//valida si hay datos temporales de los formularios
        }catch (Exception e){
            Log.i("formAdmin", e.toString());
        }
    }

    public void inicializarPopRegla(){
        popregla = new Dialog(context);
        popregla.setContentView(R.layout.popreglaconteos);
        Window window = popregla.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    //CREA CONTROLES DEL FORMULARIO
    public void CrearForm(String ubicacion) {
        try {
            linearPrinc.removeAllViews();
            for (RespuestasTab r : contenedor.getQuestions()) {
                switch (r.getTipo()) {
                    case "RS":
                        linearPrinc.addView(new Cconteos(context, path, ubicacion, r, inicial).Cconteo());
                        break;
                    case "RSE":
                        linearPrinc.addView(new CconteosEditar(context, path, ubicacion, r, inicial, popregla).CconteoEditar());
                        break;
                    case "RSC":
                        linearPrinc.addView(new CconteosCheck(context, path, ubicacion, r, inicial).Cconteo());
                        break;
                    case "RB":
                        linearPrinc.addView(new CradioButton(context, path, ubicacion, r, inicial).Tradiobtn());
                        break;
                    case "CBX":
                        linearPrinc.addView(new CradioButton(context, path, ubicacion, r, inicial).Tradiobtn());
                        break;
                    case "DES":
                        linearPrinc.addView(new CdespelgableQ(context, r, path, inicial, ubicacion, linearPrinc, contenedor).Cdesp());
                        break;
                    case "DPV":
                        linearPrinc.addView(new CVariedadJson(context, path, inicial, r, ubicacion).Cvariedad());
                        break;

                }
            }
        } catch (Exception ex) {
            Log.i("ADMIN","Error : "+ex);
        }
    }


    //VALIDA SI HAY FORMULARIOS PENDIENTES DESDE EL JSON TEMP
    public ContenedorTab validarTemporal() throws Exception {

        ContenedorTab conTemp = iCon.optenerTemporal();
        try {
            if (conTemp != null) {
                if (conTemp.getIdProceso() == pro.getCodigo_proceso()) {

                    temporal = true;
                    return conTemp;

                } else {
                    temporal = false;
                    return contenedorLimipio();
                }
            }

            return contenedorLimipio();
        } catch (Exception e) {
            Log.i("Contenedor_Error", e.toString());
            temporal = false;
        }
        return contenedorLimipio();
    }

    public ContenedorTab contenedorLimipio() throws Exception {
        return iCon.generarContenedor(
                usu.getId_usuario(),
                getPhoneName(),
                adm.getDetalles()
                        .forDetalle(pro.getCodigo_proceso()));
    }


    /*============================
    SHARED PREFENCES & DATA PHONE
    =============================*/
    public void getcodProceso_Usuario() {
        // metodo para extraer del SharedPreferences el id del proceso
        pro = new Gson().fromJson(sp.getString("proceso", ""), new TypeToken<ProcesoTab>() {}.getType());
        // metodo para extraer del SharedPreferences el id del usuario
        usu = new Gson().fromJson(sp.getString("usuario", ""), new TypeToken<UsuarioTab>() {}.getType());
    }

    public String getPhoneName() {//metodo para obtener el nombre del telefono
        try {
            BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
            return myDevice.getName();
        } catch (Exception e) {
            return "EmulatorDevice";
        }
    }

}
