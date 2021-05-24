package com.example.eliteCapture;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteCapture.Config.Util.Container.containerAdmin;
import com.example.eliteCapture.Config.Util.Controls.GIDGET;
import com.example.eliteCapture.Config.Util.secondTaks.downloadFarmsTaks;
import com.example.eliteCapture.Config.Util.secondTaks.getConexion;
import com.example.eliteCapture.Config.Util.text.textAdmin;
import com.example.eliteCapture.Model.Data.Tab.farmSelectDownloadTab;
import com.example.eliteCapture.Model.Data.Tab.listFincasTab;
import com.example.eliteCapture.Model.Data.iJsonPlan;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiresApi(api = Build.VERSION_CODES.Q)
public class downloadScreen extends AppCompatActivity {
    //Administración de datos multi-fincas

    LinearLayout lineFarmws, linearDownload;
    TextView timeText, textNotificationFarmSelected;
    CheckBox checkFamsAll;
    containerAdmin ca;
    textAdmin ta;
    GIDGET gg;

    iJsonPlan ipl;

    List<listFincasTab.fincasTab> farmsCheck = new ArrayList<>();
    List<CheckBox> listChecks = new ArrayList<>();
    List<itemFamrTab> listItemFarm = new ArrayList<>();

    String path, titulo, msg1, msg2, msg3;
    int sizeText, idFinca;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_download_screen);
        path = getExternalFilesDir(null) + File.separator;
        insEntity();
        insViews();
        insVariables();
        //validateFarmMod();
        getUser();
        paintFarms();
    }

    public void insEntity(){
        gg = new GIDGET(this, "", null, path);
        ca = new containerAdmin(this);
        ta = new textAdmin(this);
        ipl = new iJsonPlan(path);
        sp = getBaseContext().getSharedPreferences("share", MODE_PRIVATE);
    }

    public void insViews(){
        lineFarmws = findViewById(R.id.lineFarms);
        timeText = findViewById(R.id.timeText);
        checkFamsAll = findViewById(R.id.checkFamsAll);
        linearDownload = findViewById(R.id.linearDownload);

        selectedAllFarms();
    }

    public void selectedAllFarms(){
        //REALIZA LA DES/SELECCION DE TODAS LAS CAMAS

        checkFamsAll.setOnCheckedChangeListener((compoundButton, b) -> {
            if(listChecks.size() > 0){
                for(CheckBox cb : listChecks){
                    if(cb.isEnabled()) {
                        cb.setChecked(checkFamsAll.isChecked());
                    }
                }
            }
        });
    }

    public void insVariables(){
        //instancia de las variables necesarias
        sizeText = 15;

        titulo = "Elige una finca para continuar el proceso de descarga";

        msg1 =  "¡Las fincas actuales no estan asociadas " +
                " al codigo de usuario, por favor actualiza las" +
                " fincas e intentalo nuevamente!";

        msg2 = "No se encontraron fincas Asociadas con el usuario";

        msg3 = "No tienes datos de fincas descargadas";
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
            List<listFincasTab> listFincas = ipl.allListFincas();

            if(listFincas.size() > 0) {
                for (listFincasTab finca : listFincas) {
                    if (finca.getUsuario() == getUser()) {
                        for (listFincasTab.fincasTab f : finca.getFincas()) {
                            if (f.getIdFinca() != 0) {
                                createFolder(f.getIdFinca());
                                getItem(f, lineFarmws);
                            }
                        }
                    } else {
                        //los datos de finca no son del usuario
                        lineFarmws.addView(ta.textColor("Las fincas no corresponden con el usuario", "rojo", 15, "l"));
                    }
                }
            }else{
                //no tiene fincas descargadas
                lineFarmws.addView(ta.textColor("No tienes fincas descargadas, descarga datos y vuelve a intentarlo", "rojo", 15, "l"));
            }

            Log.i("farmsDownload", "cantidad de fincas creadas : "+listItemFarm.size());

            if(listItemFarm.size() > 0){
                validateFarmMod();
            }

        }catch (Exception e){
            Log.i("ErrorFarms", e.toString());
        }
    }

    public void getItem(listFincasTab.fincasTab farm, LinearLayout line){
        //crea el item de finca
        try {

            boolean downloaded = new File(path+"/listFarms/"+farm.getIdFinca()+"/Finca_"+farm.getIdFinca()+".json").exists();
            Log.i("farmDownload", farm.getIdFinca()+""+downloaded);

            LinearLayout.LayoutParams param = ca.params();
            param.setMargins(15,10,15,10);

            LinearLayout linePrincipal = ca.container();
            linePrincipal.setLayoutParams(param);

            linePrincipal.setOrientation(LinearLayout.HORIZONTAL);
            linePrincipal.setPadding(20, 0, 20, 0);
            linePrincipal.setWeightSum(2);

            gg.GradientDrawable(linePrincipal, "l", downloaded ? "verde" : "gris");

            LinearLayout linearPanel1 = LinearPanel("V");

            linearPanel1.addView(ta.textColor(farm.getNombreFinca(), "darkGray", 18, "l"));


            TextView txtNotification = (TextView)  ta.textColor(
                    downloaded  ? "descargada" : "Finca sin descargar",
                    downloaded ? "verde" : "rojo",
                    15,
                    "l"
            );
            linearPanel1.addView(txtNotification);


            LinearLayout linearPanel2 = LinearPanel("H");
            linearPanel2.setGravity(Gravity.RIGHT);

            gg.setSizeText(15);
            gg.setParams("w_w");

            Button btnWorking = (Button) gg.boton(
                    " Trabajar ",
                    downloaded ? "verde" : "gris");

            //btnWorking.setEnabled(downloaded);

            linearPanel2.addView(btnWorking);

            btnWorking.setOnClickListener(v -> {
                if(!downloaded){
                    Toast.makeText(this, "por favor descarga la finca", Toast.LENGTH_SHORT).show();
                }else{
                    SharedPreferences.Editor edit = sp.edit();
                    String pathFarm = path+"/listFarms/"+farm.getIdFinca()+"/Finca_"+farm.getIdFinca()+".json";
                    edit.putString("farmWorkingPath", pathFarm);
                    edit.putString("farmWorkingPathName", farm.getNombreFinca());
                    edit.commit();
                    edit.apply();

                    txtNotification.setText("Trabajando con la finca");

                    for(itemFamrTab f : listItemFarm){
                        if(f.farm.getIdFinca() != farm.getIdFinca()){
                            boolean downloaded2 = new File(path+"/listFarms/"+farm.getIdFinca()+"/Finca_"+farm.getIdFinca()+".json").exists();
                            f.txtNotification.setText(downloaded2  ? "descargada" : "Finca sin descargar");
                            f.txtNotification.setTextColor(Color.parseColor(downloaded2 ? "#2ecc71" : "#E74C3C"));
                        }
                    }
                }
            });

            CheckBox cb = new CheckBox(this);
            listChecks.add(cb);
            linearPanel2.addView(cb);

            functionCheckFarm(cb, farm);

            linePrincipal.addView(linearPanel1);
            linePrincipal.addView(linearPanel2);


            listItemFarm.add(
                   new itemFamrTab(
                        farm,
                        linePrincipal,
                        txtNotification,
                        btnWorking,
                        cb
                   )
            );

            line.addView(linePrincipal);
        }catch (Exception e){
            Log.i("itemFarm", "exception : "+e.toString());
        }
    }

    public void validateFarmMod(){
        try{
            new Thread(()->{
                valideConexion(
                        new getConexion(this, timeText, 1)
                );
            }).start();

        }catch (Exception e){
            Log.i("itemFarm", "exception : "+e.toString());
        }
    }

    public void valideConexion(getConexion conexion){
        try {
            Thread.sleep(1000);
            if(conexion.getCn() == null) {
                valideConexion(conexion);

                downloadScreen.this.runOnUiThread(() -> {
                    for(itemFamrTab item : listItemFarm){
                        item.check.setEnabled(false);
                    }
                });

            }else {

                final int[] iSincronizado = {0};
                for(itemFamrTab f : listItemFarm){
                    new Thread(() -> {
                        try {
                            Boolean actualizada = !ipl.validateDateFile(f.farm.getIdFinca(), conexion.getCn());

                             downloadScreen.this.runOnUiThread(() -> {
                                    f.txtNotification.setText((actualizada ? "Actualizada" : "¡Desactualizada!"));
                                    f.txtNotification.setTextColor(Color.parseColor(actualizada ? "#27AE60" : "#E74C3C"));
                                    gg.GradientDrawable(f.linePrincipal, "l",actualizada ? "verde" : "rojo");
                                    f.btnWork.setEnabled(actualizada);
                                    f.btnWork.setBackgroundColor(Color.parseColor(actualizada ? "#3498DB" : "#154360"));
                                    iSincronizado[0] = iSincronizado[0] +1;
                                    timeText.setText(" Sincronizadas "+iSincronizado[0]+" de "+listItemFarm.size()+" fincas");

                                    f.check.setEnabled(!actualizada);
                                }
                             );

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
            }
        }catch (Exception e){
            //Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            Log.i("taskDownloader", "validateConexion : "+e.toString());
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

    public void functionCheckFarm(CheckBox cb, listFincasTab.fincasTab farm){
        cb.setOnCheckedChangeListener((CompoundButton, b) -> {

            if(cb.isChecked()) {
                if(!validateFarmSelected(farm)){
                    farmsCheck.add(farm);
                }
            }else{
                List<listFincasTab.fincasTab> farmsCheckSelectedRenovated = new ArrayList<>();
                for(listFincasTab.fincasTab farmi : farmsCheck){
                    if(farm.getIdFinca() != farmi.getIdFinca()){
                        farmsCheckSelectedRenovated.add(farmi);
                    }
                }
                farmsCheck = farmsCheckSelectedRenovated;
            }
            messageFarmList();
        });

    }

    public boolean validateFarmSelected(listFincasTab.fincasTab farm){
        boolean validate = false;
        for(listFincasTab.fincasTab farmi : farmsCheck){
            if(farmi.getIdFinca() == farm.getIdFinca()){
                validate = true;
                break;
            }
        }
        return validate;
    }

    public LinearLayout.LayoutParams param(){
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        param.weight = 1;
        return param;
    }

    public void createFolder(int idFinca){
        //crea la carpeta de cada finca si no existe
        File folderFarm = new File(path+"/listFarms", String.valueOf(idFinca));
        //Toast.makeText(this, "creando carpeta : "+idFinca, Toast.LENGTH_SHORT).show();
        if(!folderFarm.exists()) {
            folderFarm.mkdirs();
        }
    }

    public void messageFarmList(){
        String msg = "Fincas seleccionadas : "+farmsCheck.size();
        if(linearDownload.getChildCount() > 0 && textNotificationFarmSelected != null) {
            textNotificationFarmSelected.setText(msg);
        }else if(linearDownload.getChildCount() == 0 && farmsCheck.size() > 0){

            LinearLayout line = ca.container();
            line.setOrientation(LinearLayout.HORIZONTAL);
            line.setBackgroundResource(R.drawable.border_gray);

            textNotificationFarmSelected = (TextView) ta.textColor(
                    msg, "darkGray", 15, "c"
            );
            textNotificationFarmSelected.setLayoutParams(param());

            Button btnDownload = (Button) gg.boton("Realizar descarga", "verde");
            btnDownload.setLayoutParams(param());

            btnDownload.setOnClickListener(v ->{
                new taskDownloader(this, farmsCheck, path, this);
            });

            line.addView(textNotificationFarmSelected);
            line.addView(btnDownload);
            linearDownload.addView(line);
        }

        if(farmsCheck.size() == 0){
            linearDownload.removeAllViews();
        }

        //adiciona un padding al final para visualizar el ultimo item Finca
        lineFarmws.setPadding(0,0,0, farmsCheck.size() > 0 ? 106 : 0);
    }

    public static class taskDownloader {
        //Realiza la descarga de fiincas en segundo plano

        Activity act;
        Context context;
        Dialog dialog;
        LinearLayout lineFarms;
        String path;

        List<listFincasTab.fincasTab> farms;

        containerAdmin ca;
        textAdmin ta;

        List<farmSelectDownloadTab> listFamsSelected = new ArrayList<>();

        Connection conexion;

        @SuppressLint("ResourceType")
        public taskDownloader(Context context, List<listFincasTab.fincasTab> farms, String path, Activity act) {
            try {
                this.act = act;
                this.context = context;
                this.farms = farms;
                this.path = path;
                ca = new containerAdmin(context);
                ta = new textAdmin(context);

                this.lineFarms = ca.container();
                lineFarms.setLayoutParams(ca.params());

                dialog = new Dialog(context);
                dialog.setContentView(R.layout.modal_admin);

                Window windowfinca = dialog.getWindow();
                windowfinca.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                windowfinca.getAttributes().gravity = Gravity.CENTER;

                LinearLayout lineDialog = dialog.findViewById(R.id.linearMod);
                lineDialog.addView(ca.scrollv(lineFarms));

                //dialog.setCancelable(false);
                dialog.show();

                paintConexion();
            }catch (Exception e){
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                Log.i("taskDownloader", e.toString());
            }
        }

        public Connection getConexion() {
            return conexion;
        }

        public void setConexion(Connection conexion) {
            this.conexion = conexion;
        }

        public void paintConexion() {
            TextView txtConexionStatus = (TextView) ta.textColor(
                    "Obteniendo conexion ...",
                    "darkGray",
                    13,
                    "c"
            );

            lineFarms.addView(txtConexionStatus);

            new Thread(()->{
                valideConexion(
                    new getConexion(act, txtConexionStatus, 3)
                );
            }).start();
        }

        public void paintFarms() {

            listFamsSelected.clear();

            for(listFincasTab.fincasTab farm : farms){

                LinearLayout principal = ca.borderGradient("#154360");
                principal.setOrientation(LinearLayout.VERTICAL);
                principal.setPadding(10,10,10,10);

                LinearLayout.LayoutParams paramPrincipal = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                paramPrincipal.setMargins(10,10,10,10);

                principal.setLayoutParams(paramPrincipal);

                TextView txtNotification = (TextView) ta.textColor(
                        "Consultando...",
                        "darkGray",
                        13,
                        "l"
                );

                LinearLayout lineItem = ca.container();
                lineItem.setLayoutParams(ca.params());
                lineItem.setOrientation(LinearLayout.HORIZONTAL);
                lineItem.setGravity(Gravity.CENTER);

                TextView txt = (TextView) ta.textColor(
                        farm.getNombreFinca(),
                        "darkGray",
                        18,
                        "l"
                );

                LinearLayout lineDrawable = ca.container();

                ProgressBar prb = getProgress();
                lineDrawable.addView(prb);

                lineItem.addView(txt);
                lineItem.addView(lineDrawable);


                principal.addView(lineItem);
                principal.addView(txtNotification);


                String pathFolderFarm = path+"/listFarms/"+farm.getIdFinca()+"/";

                listFamsSelected.add(
                        new farmSelectDownloadTab(
                                act,
                               txtNotification,
                                pathFolderFarm,
                                farm,
                               lineDrawable,
                                this.conexion
                        )
                );

                List<farmSelectDownloadTab> listClear = listFamsSelected.stream().distinct().collect(Collectors.toList());
                listFamsSelected.clear();
                listFamsSelected = listClear;

                paintInLayoutDialog(principal);
            }

            for(farmSelectDownloadTab f : listFamsSelected){
                new downloadFarmsTaks(
                        f.getAct(),
                        f.getTxtNotification(),
                        f.getFolderPath(),
                        f.getFarm(),
                        f.getLineDrawable(),
                        f.getConexion()
                );
            }
        }

        public void paintInLayoutDialog(LinearLayout principal){
            act.runOnUiThread(() -> {
                lineFarms.addView(principal);
            });
        }

        public void valideConexion(getConexion conexion){
            try {
                Thread.sleep(1000);
                if(conexion.getCn() == null) {
                    valideConexion(conexion);
                }else {
                    setConexion(conexion.getCn());
                    paintFarms();
                }
            }catch (Exception e){
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                Log.i("taskDownloader", "validateConexion : "+e.toString());
            }
        }

        public ProgressBar getProgress() {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
              ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            );
            param.setMargins(-10,-10,-10,-10);

            ProgressBar pb = new ProgressBar(context);
            Drawable draw = context.getResources().getDrawable(R.drawable.my_progressbar);
            pb.setProgressDrawable(draw);
            pb.setPadding(-8,-8,-8,-8);
            pb.setScaleX(0.5f);
            pb.setScaleY(0.5f);
            pb.setLayoutParams(param);
            return pb;
        }
    }

    public class itemFamrTab{
        listFincasTab.fincasTab farm;
        LinearLayout linePrincipal;
        TextView txtNotification;
        Button btnWork;
        CheckBox check;

        public itemFamrTab(listFincasTab.fincasTab farm, LinearLayout linePrincipal, TextView txtNotification, Button btnWork, CheckBox check) {
            this.farm = farm;
            this.linePrincipal = linePrincipal;
            this.txtNotification = txtNotification;
            this.btnWork = btnWork;
            this.check = check;
        }
    }
}