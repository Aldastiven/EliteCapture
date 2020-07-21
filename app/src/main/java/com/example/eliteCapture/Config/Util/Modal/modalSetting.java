package com.example.eliteCapture.Config.Util.Modal;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteCapture.Config.Util.Container.containerAdmin;
import com.example.eliteCapture.Config.Util.text.textAdmin;
import com.example.eliteCapture.Model.Data.Tab.onLineTab;
import com.example.eliteCapture.Model.Data.ionLine;
import com.example.eliteCapture.R;

public class modalSetting {

    Context context;
    String path;
    Dialog d;
    textAdmin ta;
    containerAdmin ca;

    ionLine ionLine;

    public modalSetting(Context context, String path) {
        this.context = context;
        this.path = path;

        ionLine = new ionLine(path);
        ta = new textAdmin(context);
        ca = new containerAdmin(context);
    }

    public Dialog modal(){
        try {
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ll.setMargins(20, 20, 20, 20);
            LinearLayout line = new LinearLayout(context);
            line.setOrientation(LinearLayout.VERTICAL);
            line.setLayoutParams(ll);

            line.addView(Line());

            ionLine = new ionLine(path);

            d = new Dialog(context, R.style.TransparentDialog);
            d.getWindow().setDimAmount(0);//sirve para quitar el fondo transparente
            d.setContentView(line);

            Window w = d.getWindow();
            w.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            w.setGravity(Gravity.RIGHT | Gravity.TOP);

            return d;
        }catch (Exception e){
            Toast.makeText(context, ""+e.toString(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public View Line() throws Exception{
        LinearLayout linearLayout = ca.container();

        TextView txt = (TextView) ta.textColor("Selecciona si quieres trabajar con conexión o de forma local", "negro", 15, "c");

        LinearLayout linearLayout1 = ca.container();
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout1.setWeightSum(3);

        LinearLayout.LayoutParams ll = ca.params();
        ll.weight = 1;
        ll.gravity = Gravity.CENTER;

        TextView txtL = (TextView) ta.textColor("offLine", "negro", 15, "c");
        txtL.setLayoutParams(ll);

        Switch sw = new Switch(context);
        sw.setLayoutParams(ll);
        sw.setGravity(Gravity.CENTER);

        estadoOnline(sw);

        TextView txtR = (TextView) ta.textColor("onLine", "negro", 15, "c");
        txtR.setLayoutParams(ll);

        linearLayout1.addView(txtR);
        linearLayout1.addView(sw);
        linearLayout1.addView(txtL);

        linearLayout.addView(txt);
        linearLayout.addView(linearLayout1);
        return linearLayout;
    }

    public void estadoOnline(final Switch sw) throws Exception{
        sw.setChecked(ionLine.all().equals("onLine") ? false : true);

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    ionLine.local(sw.isChecked() ? "offLine" : "onLine");
                    Toast.makeText(context, ionLine.all(), Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(context, ""+e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        ColorStateList buttonStates = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_enabled},
                        new int[]{android.R.attr.state_checked},
                        new int[]{}
                },
                new int[]{
                        Color.BLUE,
                        Color.RED,
                        Color.GREEN
                }
        );
        sw.getThumbDrawable().setTintList(buttonStates);
        sw.getTrackDrawable().setTintList(buttonStates);
    }
}
