package com.example.eliteCapture.Config.Util.notification;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class notificationAdmin {
    Context context;
    String mensaje;
    String tipo;
    int duracion;
    LinearLayout linear;

    LinearLayout linearLayout;
    TextView txt;

    ProgressDialog progress;

    public notificationAdmin(Context context, String mensaje, String tipo, int duracion, LinearLayout linear) {
        this.context = context;
        this.mensaje = mensaje;
        this.tipo = tipo;
        this.duracion = duracion;
        this.linear = linear;
    }

    public void crear(){
        if(linear.getChildCount() < 1) {
            linear.addView(getContainer());
            colored(linearLayout, txt);
            temporizador();
        }
    }

    public LinearLayout getContainer(){
        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT );
        ll.setMargins(10,10,10,10);

        linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(ll);
        linearLayout.setPadding(5,0,5,0);

        linearLayout.addView(getText());

        return linearLayout;
    }

    public View getText(){
        LinearLayout.LayoutParams ll2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT );
        ll2.setMargins(0,20,0,20);

        txt = new TextView(context);
        txt.setText(mensaje);
        txt.setTextSize(15);
        txt.setTextColor(Color.parseColor("#000000"));
        txt.setGravity(View.TEXT_ALIGNMENT_CENTER);
        txt.setTypeface(null, Typeface.BOLD);
        txt.setLayoutParams(ll2);

        return txt;
    }

    public void colored(LinearLayout container, TextView txt){
        switch (tipo){
            case "good" :
                container.setBackgroundColor(Color.parseColor("#2ecc71"));
                txt.setTextColor(Color.parseColor("#ffffff"));
                break;
            case "bad" :
                container.setBackgroundColor(Color.parseColor("#E46472"));
                txt.setTextColor(Color.parseColor("#ffffff"));
                break;
            case "clear" :
                container.setBackgroundColor(Color.parseColor("#3498DB"));
                txt.setTextColor(Color.parseColor("#ffffff"));
                break;
            case "dark" :
                container.setBackgroundColor(Color.parseColor("#2e2d33"));
                txt.setTextColor(Color.parseColor("#ffffff"));
                break;
            case "darkGreen" :
                container.setBackgroundColor(Color.parseColor("#2e2d33"));
                txt.setTextColor(Color.parseColor("#2ecc71"));
                break;
            case "yellow" :
                container.setBackgroundColor(Color.parseColor("#D4AC0D"));
                txt.setTextColor(Color.parseColor("#2ecc71"));
                break;
        }
    }

    public void temporizador(){
        if(duracion > 0 ) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    linear.removeAllViews();
                }
            }, duracion);
        }
    }


    private class CargarXmlTask extends AsyncTask<String, Void, Void> {

        String dataText;

        public CargarXmlTask(String dataText) {
            this.dataText = dataText;
        }

        @Override
        protected Void doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPreExecute() {

            //"Cargando datos, espere un momento por favor..."
            progress.setMessage(dataText);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(false);
            progress.setCancelable(false);
            progress.show();
        }

        protected void onProgressUpdate (String... strings) {
            Log.i("AsyncClass", strings[0]);
            progress.setMessage(strings[0]);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            progress.dismiss();
        }
    }
}
