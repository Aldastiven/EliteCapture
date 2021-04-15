package com.example.eliteCapture.Config.Util.secondTaks;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.example.eliteCapture.Config.sqlConect;
import com.example.eliteCapture.Model.Data.Tab.listFincasTab;
import com.example.eliteCapture.Model.Data.iJsonPlan;

import java.sql.Connection;

public class downloadFarmsTaks {

    Activity act;
    TextView txtNotification;

    long initialTime = System.currentTimeMillis(), timeMinutes = 0, timeSeconds;
    String  path;

    iJsonPlan ijp;
    listFincasTab.fincasTab farm;

    Connection cn;

    public downloadFarmsTaks(Activity act, TextView txtNotification, String path, listFincasTab.fincasTab farm) {
        this.act = act;
        this.txtNotification = txtNotification;
        this.path = path;
        this.farm = farm;

        new getDataFarms().start();
    }

    public class getDataFarms extends Thread{
        @Override
        public void run() {
            super.run();

            txtNotification.setText("Realizando conexion al servidor..");
            cn = new sqlConect().getConexion();
            ijp = new iJsonPlan(path, cn, farm);
            downloader();
        }

        public void downloader(){
            try{
                Log.i("screenDownloader", "Comenzo a descargar al finca : "+farm.getIdFinca());
                runEditNotification("Obteniendo información de finca..");
                ijp.localListFincas(farm.getIdFinca());
                Log.i("Error","tERMINO LA DESCARGA");
                runEditNotification("Termino de descargar, tiempo : ");
            }catch (Exception e){
                Log.i("Error","taksDownloadFarms : "+e.toString());
            }
        }

        public void runEditNotification(String data){
            act.runOnUiThread(() -> {
                txtNotification.setText(data);
            });
        }
    }
}
