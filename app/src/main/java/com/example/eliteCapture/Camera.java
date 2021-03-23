package com.example.eliteCapture;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eliteCapture.Config.Util.Controls.GIDGET;
import com.example.eliteCapture.Model.Data.Tab.DesplegableTab;
import com.example.eliteCapture.Model.Data.iDesplegable;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.Model.View.iContenedor;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class Camera extends AppCompatActivity implements Serializable {

    private ZBarScannerView vbc;

    int id, regla;
    String ubicacion, path, desplegable;
    GIDGET pp;
    RespuestasTab rt;

    SharedPreferences sp;

    ProgressDialog progress;

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
        this.desplegable = rt.getDesplegable();

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
                new CargarXmlTask().execute();

                String bc = rawResult.getContents();
                String format = rawResult.getBarcodeFormat().getName();
                //CODE128
                //QRCODE
                if (bc != null) {
                    Log.i("SCANNERBAR", bc);



                    String data = null;
                    if(format.equals("QRCODE")){
                        for(String d : bc.split(",")){
                            Log.i("SCANNERBAR", d);
                        }
                        data = bc.split(",")[0];
                    }

                    bc = data != null ? data : bc;

                    Intent i = new Intent(Camera.this, genated.class);
                    i.putExtra("codigo", bc);
                    i.putExtra("camera", true);
                    i.putExtra("ubicacion", ubicacion);
                    i.putExtra("respuestaTab", rt);

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
        iContenedor conTemp = new iContenedor(path);
        String valor = "";

        if (!StringUtils.isEmpty(desplegable)) {
            String cadena = "";
            String res = "";

            if (new Integer(regla) == null) {
                valor = Buscar(rta, desplegable);
            } else {
                cadena = quitCadena(rta, regla);

                DesplegableTab desp = pp.busqueda(cadena);
                res = desp != null ? desp.getOpcion() : "No se encontro el resultado";
                valor = desp != null ?  desp.getOpcion() : "No se encontro el resultado";

                SharedPreferences.Editor edit = sp.edit();
                edit.putString( "resDesp",res.isEmpty() ? "" : res);
                edit.apply();
            }
        conTemp.editarTemporal(ubicacion, id,  valor , !cadena.isEmpty() ? cadena : null,null ,regla);
        }else{
            conTemp.editarTemporal(ubicacion, id, "Sin desplegable", new Integer(regla) == null ? rta : quitCadena(rta, regla), null, regla);
        }
    }

    public String Buscar(String data, String desplegable) {
        try {
            iDesplegable iDesp = new iDesplegable(null, path);
            iDesp.nombre = desplegable;

            for (DesplegableTab desp : iDesp.all()) {
                if (desp.getCodigo().equals(data)) {
                    return desp.getOpcion();
                }
            }
            return "NO DATA SCAN";
        } catch (Exception ex) {
            return "NO DATA SCAN";
        }
    }

    //funcion que resta la cadena de texto con la regla
    public String quitCadena(String respuesta, int n) {
        String cadena;
        cadena = respuesta;
        return cadena.substring(0, cadena.length() - n);
    }

    private class CargarXmlTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            publishProgress();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(Camera.this);
            progress.setMessage("Analizando datos, espere un momento por favor...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(false);
            progress.setCancelable(false);
            progress.show();
        }

        protected void onProgressUpdate (String... strings) {
            Log.i("AsyncClass", strings[0]);
            progress.setMessage(strings[0]);
        }
    }

}
