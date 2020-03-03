package com.example.eliteCapture;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.example.eliteCapture.Config.Util.ControlViews.Cconteos;
import com.example.eliteCapture.Config.Util.ControlViews.Cscanner;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.Model.View.iContenedor;

import java.util.ArrayList;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class Camera extends AppCompatActivity {

    private ZBarScannerView vbc;

    int id, regla;
    String ubicacion, path, desplegable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

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

                    registro(bc, null);

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
    public void registro(String rta, String valor) throws Exception {
        Bundle();

        Cscanner cscanner = new Cscanner(path);
        iContenedor conTemp = new iContenedor(path);

        if (!desplegable.isEmpty()) {

            String cadena = "";

            Integer b = new Integer(regla);
            if (b == null) {
                valor = cscanner.Buscar(rta, desplegable);
            } else {
                cadena = quitCadena(rta, regla);
                valor = cscanner.Buscar(cadena, desplegable);
            }

            if (!valor.equals("NO DATA SCAN")) {
                conTemp.editarTemporal(ubicacion, id, cadena, valor);
            } else {
                conTemp.editarTemporal(ubicacion, id, null, null);
            }
        }else{
            conTemp.editarTemporal(ubicacion, id, rta, valor);
        }
    }

    //funcion que resta la cadena de texto con la regla
    public String quitCadena(String respuesta, int n) {
        String cadena;
        cadena = respuesta;
        return cadena.substring(0, cadena.length() - n);
    }

    //funcion que devuelve los datos pasados por Cscanner
    public void Bundle() {
        Bundle bundle = getIntent().getExtras();
        if (!bundle.isEmpty()) {
            id = bundle.getInt("id", 0);
            ubicacion = bundle.getString("ubi", "");
            path = bundle.getString("path", "");
            desplegable = bundle.getString("desplegable", "");
            regla = bundle.getInt("regla", 0);
        }
    }
}
