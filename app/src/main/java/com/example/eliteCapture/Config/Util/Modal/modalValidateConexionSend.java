package com.example.eliteCapture.Config.Util.Modal;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteCapture.Config.Util.Container.containerAdmin;
import com.example.eliteCapture.Config.Util.Image.imageAdmin;
import com.example.eliteCapture.Config.Util.text.textAdmin;
import com.example.eliteCapture.R;

public class modalValidateConexionSend {

    Context context;
    Dialog d;

    containerAdmin ca;
    textAdmin ta;
    imageAdmin ia;
    TextView txt;


    public modalValidateConexionSend(Context context) {
        this.context = context;

        ca = new containerAdmin(context);
        ta = new textAdmin(context);
        ia = new imageAdmin(context);
    }

    public TextView getTxt() {
        return txt;
    }

    public void setTxt(TextView txt) {
        this.txt = txt;
    }

    public Dialog modal(){
        try {
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ll.setMargins(20, 20, 20, 20);
            LinearLayout line = new LinearLayout(context);
            line.setOrientation(LinearLayout.VERTICAL);
            line.setLayoutParams(ll);

            line.addView(Line());

            d = new Dialog(context, R.style.TransparentDialog);
            d.getWindow().setDimAmount(0);//sirve para quitar el fondo transparente
            d.setContentView(line);

            Window w = d.getWindow();
            w.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            w.setGravity(Gravity.BOTTOM | Gravity.CENTER);

            return d;
        }catch (Exception e){
            Toast.makeText(context, ""+e.toString(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public View Line(){
        LinearLayout linearLayout = ca.container();

        TextView txt = (TextView) ta.textColor("Validando conexion  ", "negro", 18, "c");
        setTxt(txt);

        ProgressBar prb = getProgress();

        LinearLayout linearLayout1 = ca.container();
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout1.setWeightSum(2);


        linearLayout1.addView(txt);
        linearLayout1.addView(prb);

        LinearLayout.LayoutParams ll = ca.params();
        ll.weight = 1;
        ll.gravity = (Gravity.CENTER | Gravity.BOTTOM);

        linearLayout.addView(linearLayout1);
        return linearLayout;
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
        pb.setScaleX(0.8f);
        pb.setScaleY(0.8f);
        pb.setLayoutParams(param);
        return pb;
    }


}
