package com.example.eliteCapture.Config.Util.secondTaks;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eliteCapture.Config.Util.Container.textAdmin;
import com.example.eliteCapture.Model.Data.Tab.listFincasTab;
import com.example.eliteCapture.Model.Data.iJsonPlan;

import java.sql.Connection;

public class downloadFarmsTaks {

    Activity act;
    TextView txtNotification;
    LinearLayout lineDrawable;

    long initialTime = System.currentTimeMillis(), timeMinutes = 0, timeSeconds;
    String  path;

    iJsonPlan ijp;
    listFincasTab.fincasTab farm;

    Connection cn;

    textAdmin ta;

    public downloadFarmsTaks(
            Activity act,
            TextView txtNotification,
            String path,
            listFincasTab.fincasTab farm,
            LinearLayout lineDrawable,
            Connection cn) {

        this.act = act;
        this.txtNotification = txtNotification;
        this.path = path;
        this.farm = farm;
        this.lineDrawable = lineDrawable;
        this.cn = cn;

        ta = new textAdmin(act);

        new getDataFarms().start();
    }



    public class getDataFarms extends Thread{
        @Override
        public void run() {
            super.run();
            ijp = new iJsonPlan(path, cn, farm);
            downloader();
        }

        public void downloader(){
            try{
                runEditNotification("Obteniendo información de finca..", "#154360");
                ijp.localListFincas(farm.getIdFinca());
                runEditNotification(
                        "Descarga y almacenamiento completado con exito \nTiempo de ejecución : "+calulateTime(),
                        "#2ecc71");
                runLineDrawbleStatusOk();
            }catch (Exception e){
                Log.i("Error","taksDownloadFarms : "+e.toString());
            }
        }

        public String calulateTime(){
            //obtiene el tiempo inicial de la tarea
            timeSeconds = (System.currentTimeMillis() - initialTime) / 1000;
            if (timeSeconds == 60) {
                timeMinutes = timeMinutes + 1;
            }

            String minutesChar = (timeMinutes > 0 ? timeMinutes + " min. , " : ""),
                    secondsChar = timeSeconds + " seg. ";

            return minutesChar+secondsChar;
        }

        public void runEditNotification(String data, String color){
            act.runOnUiThread(() -> {
                txtNotification.setTextColor(Color.parseColor(color));
                txtNotification.setText(data);
            });
        }

        public void runLineDrawbleStatusOk(){
            act.runOnUiThread(() -> {
                lineDrawable.removeAllViews();
                lineDrawable.addView(
                        ta.textColor("  ✓", "verde", 15, "l")
                );
            });
        }
    }
}
