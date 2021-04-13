package com.example.eliteCapture;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteCapture.Config.Util.Container.containerAdmin;
import com.example.eliteCapture.Config.Util.Controls.GIDGET;
import com.example.eliteCapture.Config.Util.secondTaks.getTimeTaks;
import com.example.eliteCapture.Config.Util.text.textAdmin;
import com.example.eliteCapture.Model.Data.Tab.listFincasTab;
import com.example.eliteCapture.Model.Data.iJsonPlan;

import java.io.File;

public class downloadScreen extends AppCompatActivity {
    //Administración de datos multi-fincas

    LinearLayout lineFarmws;
    TextView timeText;
    containerAdmin ca;
    textAdmin ta;
    GIDGET gg;

    iJsonPlan ipl;

    String path, titulo, msg1, msg2;
    int sizeText, idFinca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_screen);
        path = getExternalFilesDir(null) + File.separator;
        insEntity();
        insViews();
        insVariables();
        paintFarms();

        new taskDownloader(this, timeText);
    }

    public void insEntity(){
        gg = new GIDGET(this, "", null, path);
        ca = new containerAdmin(this);
        ta = new textAdmin(this);
        ipl = new iJsonPlan(path);
    }

    public void insViews(){
        lineFarmws = findViewById(R.id.lineFarms);
        timeText = findViewById(R.id.timeText);
    }

    public void insVariables(){
        //instancia de las variables necesarias
        sizeText = 15;

        titulo = "Elige una finca para continuar el proceso de descarga";

        msg1 =  "¡Las fincas actuales no estan asociadas " +
                " al codigo de usuario, por favor actualiza las" +
                " fincas e intentalo nuevamente!";

        msg2 = "No se encontraron fincas Asociadas con el usuario";
    }

    public int getUser(){
        //obtiene el codigo del usuario
        try {
            Bundle b = getIntent().getExtras();
            return  b != null ? b.getInt("usuario") : null;
        }catch (Exception e){
            Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show();
            return 0000;
        }
    }

    public void paintFarms(){
        //obtiene la lista de fincas del usuario
        try{
            listFincasTab fincaplan = ipl.allListFincas().get(0);

            if (fincaplan != null) {
                if (fincaplan.getUsuario() == getUser()) {
                    for (listFincasTab.fincasTab finca : fincaplan.getFincas()) {
                        idFinca = finca.getIdFinca();
                        createFolder();

                        getItem(finca, lineFarmws);
                    }
                }else{
                    Toast.makeText(this, msg1, Toast.LENGTH_SHORT).show();
                }
            } else {
                lineFarmws.addView(
                        ta.textColor(msg2, "rojo", sizeText, "l")
                );
            }
        }catch (Exception e){
            Log.i("ErrorFarms", e.toString());
        }
    }

    public void getItem(listFincasTab.fincasTab nomBtn, LinearLayout line){
        //crea el item de finca
        try {
            LinearLayout.LayoutParams param = ca.params();
            param.setMargins(15,2,15,2);

            LinearLayout linePrincipal = ca.container();
            linePrincipal.setLayoutParams(param);

            linePrincipal.setOrientation(LinearLayout.HORIZONTAL);
            linePrincipal.setPadding(20, 0, 20, 0);
            linePrincipal.setWeightSum(2);

            gg.GradientDrawable(linePrincipal, "l");

            LinearLayout linearPanel1 = LinearPanel("V");

            linearPanel1.addView(ta.textColor(nomBtn.getNombreFinca(), "darkGray", 18, "l"));
            linearPanel1.addView(ta.textColor("notificacion", "rojo", 15, "l"));

            LinearLayout linearPanel2 = LinearPanel("H");

            linearPanel2.addView(gg.boton("Trabajar finca", "blue"));
            linearPanel2.addView(getCheck());

            linePrincipal.addView(linearPanel1);
            linePrincipal.addView(linearPanel2);

            line.addView(linePrincipal);
        }catch (Exception e){
            Log.i("itemFarm", "exception : "+e.toString());
        }
    }

    public LinearLayout LinearPanel(String ori){
        LinearLayout line = ca.container();
        line.setOrientation(
                ori.equals("V") ? LinearLayout.VERTICAL : LinearLayout.HORIZONTAL
        );
        line.setWeightSum(2);
        line.setLayoutParams(param());

        return line;
    }

    public CheckBox getCheck(){
        CheckBox cb = new CheckBox(this);
        return cb;
    }

    public LinearLayout.LayoutParams param(){
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        param.weight = 1;
        return param;
    }

    public void createFolder(){
        //crea la carpeta de cada finca si no existe
        File folderFarm = new File(path+"/listFarms", String.valueOf(idFinca));
        if(!folderFarm.exists()) {
            folderFarm.mkdirs();
        }
    }

    public static class taskDownloader {
        //Realiza la descarga de fiincas en segundo plano

        Context context;
        ProgressDialog dialog;

        public taskDownloader(Context context, TextView timeText) {
            this.context = context;
            dialog = new ProgressDialog(context);
            getTimeTaks gtt = new getTimeTaks();
            gtt.setTimeText(timeText);
            gtt.start();
        }
    }
}