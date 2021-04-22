package com.example.eliteCapture.Config.Util;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eliteCapture.Config.Util.Container.containerAdmin;
import com.example.eliteCapture.Config.Util.Controls.AUT_DES_CBX;
import com.example.eliteCapture.Config.Util.Controls.CAM;
import com.example.eliteCapture.Config.Util.Controls.CBE;
import com.example.eliteCapture.Config.Util.Controls.DPV;
import com.example.eliteCapture.Config.Util.Controls.ETN_ETA;
import com.example.eliteCapture.Config.Util.Controls.GPS;
import com.example.eliteCapture.Config.Util.Controls.JSO;
import com.example.eliteCapture.Config.Util.Controls.RB;
import com.example.eliteCapture.Config.Util.Controls.RS_RSE_RSC;
import com.example.eliteCapture.Config.Util.Controls.SCA_FIL;
import com.example.eliteCapture.Config.Util.Controls.TIM_FEC;
import com.example.eliteCapture.Model.Data.Admin;
import com.example.eliteCapture.Model.Data.Tab.ProcesoTab;
import com.example.eliteCapture.Model.Data.Tab.UsuarioTab;
import com.example.eliteCapture.Model.View.Tab.ContenedorTab;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.Model.View.iContenedor;
import com.example.eliteCapture.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class formAdmin {

    LinearLayout linearPrinc, linearBodypop;
    Context context;
    Activity act;
    String path;
    Boolean inicial;
    Dialog popregla;
    boolean JSO = false;

    iContenedor iCon;

    ContenedorTab contenedor;
    ProcesoTab pro = null;
    UsuarioTab usu = null;

    SharedPreferences sp;
    Admin adm = null;
    containerAdmin contAdmin;

    int estado = 1, consecutivo;

    public formAdmin(LinearLayout linearPrinc, LinearLayout linearBodypop, Context context, Activity act, String path, Boolean inicial, int estado, int consecutivo, boolean JSO) {
        try {
            this.linearPrinc = linearPrinc;
            this.linearBodypop = linearBodypop;
            this.context = context;
            this.path = path;
            this.inicial = inicial;
            this.estado = estado;
            this.consecutivo = consecutivo + 1;
            this.JSO = JSO;
            this.act = act;
            sp = context.getSharedPreferences("share", context.MODE_PRIVATE);
            adm = new Admin(null, path);//administra la conexion de las entidades

            contAdmin = new containerAdmin(context);

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
            List<RespuestasTab> lista = null;
            switch (ubicacion) {
                case "H":
                    lista = contenedor.getHeader();
                    break;
                case "Q":
                    lista = contenedor.getQuestions();
                    break;
                case "F":
                    lista = contenedor.getFooter();
                    break;
            }

            for (RespuestasTab r : lista) {
                View v;
                switch (r.getTipo().trim()) {
                    case "CBE":// desplegable con editor de texto numerico
                        v = new CBE(context, ubicacion, r, path, inicial).crear();
                        break;
                    case "RB":// radio buttons
                        v = new RB(context, ubicacion, r, path, inicial).crear();
                        break;
                    case "DPV":// campo que dependiente
                        v = new DPV(context, ubicacion, r, path, inicial).crear();
                        break;
                    case "ETN":// campo de texto numerico
                    case "ETA":// campo de texto alfanumerico
                        v = new ETN_ETA(context, ubicacion, r, path, inicial).crear();
                        break;
                    case "FIL":// campo busqueda con teclado alfanumerico
                    case "FIN":// campo busqueda con teclado numerico
                    case "SCA":// scanner busqueda con teclado alfanumerico
                    case "SCN":// scanner busqueda con teclado numerico
                        v = new SCA_FIL(context, ubicacion, r, path, inicial).crear();
                        break;
                    case "AUT":// autocompletable con teclado alfanumerico
                    case "AUN":// autocompletable con teclado numerico
                    case "DES":// combobox
                    case "CBX":// combobox
                        v = new AUT_DES_CBX(context, ubicacion, r, path, inicial).crear();
                        break;
                    case "RS"://campo de conteo suma resta
                    case "RSE"://campo de conteo suma resta (con editor de longitud)
                    case "RSC"://campo de conteo suma resta con multiseleccion
                        v = new RS_RSE_RSC(context, ubicacion, r, path, inicial).crear();
                        break;
                    case "TIM"://timepcker
                    case "FEC"://datepcker
                        v = new TIM_FEC(context, ubicacion, r, path, inicial).crear();
                        break;
                     case "JSO":// scanner, busqueda y navegacion de json
                        v = new JSO(context, ubicacion, r, path, inicial, JSO).crear();
                        break;
                    case "GPS"://georeferenciación
                        v = new GPS(context, ubicacion, r, path, inicial).crear();
                        break;
                    case "CAM"://captura de fotos
                        v = new CAM(context, ubicacion, r, path, inicial, usu, consecutivo).crear();
                        break;
                    default:
                        v = noCreate(r.getTipo());// El campo asignado en la base no existe
                        break;
                }

                if(ubicacion.equals("H")){
                    linearBodypop.addView(v);
                }else if (ubicacion.equals("Q")){
                    linearPrinc.addView(v);
                }else{
                    linearPrinc.addView(v);
                }
            }
        } catch (Exception ex) {
            Log.i("FORMULARIO","Error creacion : "+ex);
        }
    }


    //VALIDA SI HAY FORMULARIOS PENDIENTES DESDE EL JSON TEMP
    public ContenedorTab validarTemporal() throws Exception {
        ContenedorTab conTemp = iCon.optenerTemporal();

        if(conTemp == null){
            return contenedorLimipio();
        }else{
            return  conTemp.getIdProceso() == pro.getCodigo_proceso() ? conTemp : contenedorLimipio();
        }
    }

    public ContenedorTab contenedorLimipio() throws Exception {
        return iCon.generarContenedor(
                usu.getId_usuario(),
                getPhoneName(),
                adm.getDetalles()
                        .forDetalle(pro.getCodigo_proceso()));
    }

    public View noCreate(String campo){
        LinearLayout line = contAdmin.container();
        TextView txt = new TextView(context);
        txt.setText("No se pudo crear el campo : "+campo);
        txt.setTextSize(15);
        txt.setTextColor(Color.parseColor("#F1948A"));

        line.addView(txt);
        return line;
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
