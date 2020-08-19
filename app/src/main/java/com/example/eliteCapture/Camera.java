package com.example.eliteCapture;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.eliteCapture.Config.Util.ControlViews.Cscanner;
import com.example.eliteCapture.Config.Util.Controls.GIDGET;
import com.example.eliteCapture.Model.Data.Tab.DesplegableTab;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.Model.View.iContenedor;

import java.io.Serializable;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class Camera extends AppCompatActivity implements Serializable {

    private ZBarScannerView vbc;

    int id, regla;
    String ubicacion, path, desplegable;
    GIDGET pp;
    RespuestasTab rt;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        sp = getBaseContext().getSharedPreferences("shareResultados",MODE_PRIVATE);

        this.path = getIntent().getStringExtra("path");
        this.ubicacion = getIntent().getStringExtra("ubicacion");
        this.rt = (RespuestasTab) getIntent().getSerializableExtra("respuestaTab");
        this.id = rt.getId().intValue();
        this.regla = rt.getReglas();
        this.desplegable = rt.getDesplegable() != null ? rt.getDesplegable() : "";

        pp = new GIDGET(this,ubicacion,rt,path);

        vbc = new ZBarScannerView(this);
        vbc.setResultHandler(new Camera.barcodeimp());
        setContentView(vbc);
    }

    @Override
    public void onResume() {
        super.onResume();
        vbc.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        vbc.stopCamera();
    }

    public class barcodeimp implements ZBarScannerView.ResultHandler {

        @Override
        public void handleResult(Result rawResult) {
            try {
                String bc = rawResult.getContents();
                if (bc != null) {
                    Intent i = new Intent(Camera.this, genated.class);
                    i.putExtra("codigo", bc);
                    i.putExtra("camera", true);
                    i.putExtra("ubicacion", ubicacion);

                    registro(bc);

                    startActivityForResult(i, 0);
                    vbc.stopCamera();
                } else {
                    Toast.makeText(Camera.this, "no hay resultado", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception ex) {
                Toast.makeText(Camera.this, "Exception al leer el codigo \n \n " + ex.toString(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Camera.this, genated.class);
                startActivity(i);
            }
        }
    }

    //funcion de registro en el tempóral
    public void registro(String rta) {

        Cscanner cscanner = new Cscanner(path);
        iContenedor conTemp = new iContenedor(path);
        String valor = "";
        if (rt.getDesplegable() != null) {

            Log.i("DESPLEGABLE","entro al if");

            String cadena = "";
            String res = "";

            if (new Integer(regla) == null) {
                valor = cscanner.Buscar(rta, desplegable);
            } else {
                cadena = quitCadena(rta, regla);

                DesplegableTab desp = pp.busqueda(cadena);
                res =  desp.getOpcion();
                valor = desp.getOpcion();

                SharedPreferences.Editor edit = sp.edit();
                edit.putString( "resDesp",res.isEmpty() ? "" : res);
                edit.apply();
            }
            conTemp.editarTemporal(ubicacion, id, !valor.isEmpty() ? valor : null , null ,!cadena.isEmpty() ? cadena : null,regla);

        }else{
            Log.i("DESPLEGABLE","no entro");
            conTemp.editarTemporal(ubicacion, id, valor, rta, null, regla);
        }
    }

    //funcion que resta la cadena de texto con la regla
    public String quitCadena(String respuesta, int n) {
        String cadena;
        cadena = respuesta;
        return cadena.substring(0, cadena.length() - n);
    }

}
