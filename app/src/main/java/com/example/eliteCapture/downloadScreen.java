//REALIZA LA DESCARGA MULTIFINCA FINCAS
package com.example.eliteCapture;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.eliteCapture.Config.Util.Container.containerAdmin;
import com.example.eliteCapture.Config.Util.Controls.GIDGET;
import com.example.eliteCapture.Config.Util.text.textAdmin;
import com.example.eliteCapture.Model.Data.Tab.jsonPlanTab;
import com.example.eliteCapture.Model.Data.Tab.listFincasTab;
import com.example.eliteCapture.Model.Data.iJsonPlan;

import java.io.File;
import java.util.List;

public class downloadScreen extends AppCompatActivity {

    LinearLayout lineFarmws;
    containerAdmin ca;
    textAdmin ta;
    GIDGET gg;

    iJsonPlan ipl;

    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_screen);
        path = getExternalFilesDir(null) + File.separator;
        insEntity();
        insViews();
        paintFarms();
    }

    public void insEntity(){
        gg = new GIDGET(this, "", null, path);
        ca = new containerAdmin(this);
        ta = new textAdmin(this);
        ipl = new iJsonPlan(path);
    }

    public void insViews(){
        lineFarmws = findViewById(R.id.lineFarms);
    }

    public int getUser(){
        try {
            Bundle b = getIntent().getExtras();
            return  b != null ? b.getInt("usuario") : null;
        }catch (Exception e){
            Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show();
            return 0000;
        }
    }

    public void paintFarms(){
        try{
            LinearLayout linePrincipal = ca.container();
            LinearLayout lineText = ca.container();
            LinearLayout line = ca.container();
            line.setBackgroundColor(Color.parseColor("#EAEDED"));

            lineText.addView(ta.textColor(" Elige una finca para continuar el proceso de descarga", "darkGray", 15, "l"));

            List<listFincasTab> listFarms = ipl.allListFincas();

            if (listFarms != null) {
                for (listFincasTab lf : listFarms) {

                    if (lf.getUsuario() == getUser()) {
                        for (listFincasTab.fincasTab finca : lf.getFincas()) {
                            File folderFarm = new File(path+"/listFarms", ""+finca.getIdFinca());
                            if(!folderFarm.exists()) {
                                folderFarm.mkdirs();//crea la carpeta de cada finca sino existe
                            }
                            getItem(finca, line);//crea el item de finca
                        }
                    }else{
                        lineText.removeAllViews();
                        lineText.addView(ta.textColor("  ¡Las fincas actuales no estan asociadas  al codigo de usuario, por favor actualiza las fincas e intentalo nuevamente!", "rojo", 15, "l"));
                        break;
                    }
                }
            } else {
                line.addView(ta.textColor(
                        "No se encontraron fincas Asociadas con el usuario",
                        "rojo",
                        15,
                        "c"
                ));
            }

            linePrincipal.addView(lineText);
            linePrincipal.addView(ca.scrollv(line));
            lineFarmws.addView(linePrincipal);
        }catch (Exception e){
            Log.i("ErrorFarms", e.toString());
        }
    }

    public void getItem(listFincasTab.fincasTab nomBtn, LinearLayout line){
        try {
            LinearLayout linePrincipal = ca.container();
            linePrincipal.setOrientation(LinearLayout.HORIZONTAL);
            linePrincipal.setPadding(10, 0, 0, 0);
            linePrincipal.setWeightSum(2);

            gg.GradientDrawable(linePrincipal, "l");

            LinearLayout linearPanel1 = LinearPanel("V");

            linearPanel1.addView(ta.textColor(nomBtn.getNombreFinca(), "darkGray", 18, "l"));
            linearPanel1.addView(ta.textColor("notificacion", "rojo", 15, "l"));

            LinearLayout linearPanel2 = LinearPanel("H");

            linearPanel2.addView(getCheck());
            linearPanel2.addView(gg.boton("Trabajar finca", "verde"));

            linearPanel1.setLayoutParams(param());
            linearPanel2.setLayoutParams(param());

            linePrincipal.addView(linearPanel1);
            linePrincipal.addView(linearPanel2);

            line.addView(linePrincipal);
        }catch (Exception e){
            Log.i("itemFarm", "exception : "+e.toString());
        }
    }

    public LinearLayout LinearPanel(String ori){
        LinearLayout line = ca.container();
        line.setOrientation(ori.equals("V") ? LinearLayout.VERTICAL : LinearLayout.HORIZONTAL);
        line.setWeightSum(2);

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
}