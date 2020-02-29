package com.example.eliteCapture.Config.Util.ControlViews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteCapture.Model.View.Tab.ContenedorTab;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.Model.View.iContenedor;
import com.example.eliteCapture.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import static java.lang.String.valueOf;

public class Cconteos {
    View ControlView;

    private Context context;
    private String path;
    private String ubicacion;
    private RespuestasTab rt;
    private boolean vacio;
    private Boolean inicial;

    ArrayList<Integer> idnull = new ArrayList<>();

    ControlGnr Cgnr;

    public Cconteos(Context context, String path, String ubicacion, RespuestasTab rt, boolean vacio, Boolean inicial) {
        this.context = context;
        this.path = path;
        this.ubicacion = ubicacion;
        this.rt = rt;
        this.vacio = vacio;
        this.inicial = inicial;
    }

    //metodo que crea el control del conteo
    public View Cconteo() {

        LinearLayout.LayoutParams llparamsText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        llparamsText.weight = (float) 2.3;

        LinearLayout.LayoutParams llparamsTextpo = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        llparamsText.weight = (float) 0.7;

        final TextView tvp = new TextView(context);
        tvp.setId(rt.getId().intValue());
        tvp.setText(rt.getPregunta() + "\nponderado: " + rt.getPonderado());
        tvp.setTextColor(Color.parseColor("#979A9A"));
        tvp.setPadding(5, 5, 5, 5);
        tvp.setTypeface(null, Typeface.BOLD);
        tvp.setLayoutParams(llparamsText);

        final TextView tvpor = new TextView(context);
        tvpor.setId(rt.getId().intValue());
        tvpor.setText((rt.getValor() == null ? "Resultado : \n" : "Resultado: \n"+rt.getValor()));
        tvpor.setTextColor(Color.parseColor("#979A9A"));
        tvpor.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        tvpor.setPadding(10, 10, 10, 10);
        tvpor.setTypeface(null, Typeface.BOLD);
        tvpor.setLayoutParams(llparamsTextpo);

        Cgnr = new ControlGnr(context, rt.getId(), pp(tvp, tvpor), Cbotones(tvpor), null, "vx2");
        ControlView = Cgnr.Contenedor(vacio, inicial);

        return ControlView;
    }


    //metodo que crea los botones de conteos
    public View Cbotones(final TextView tvpor) {
        View viewTotal;

        LinearLayout llbtn = new LinearLayout(context);
        llbtn.setWeightSum(4);
        llbtn.setOrientation(LinearLayout.HORIZONTAL);
        llbtn.setVerticalGravity(Gravity.CENTER);
        llbtn.setPadding(10, 0, 10, 0);
        llbtn.setBackgroundColor(Color.parseColor("#00FFFFFF"));

        LinearLayout.LayoutParams LLcmpweight = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LLcmpweight.weight = 3;

        final TextView tvr = new TextView(context);
        tvr.setText((rt.getRespuesta() == null) ? "-1" : rt.getRespuesta());
        tvr.setId(rt.getId().intValue());
        tvr.setTextSize(40);
        tvr.setTextColor(Color.parseColor("#5d6d7e"));
        tvr.setTypeface(null, Typeface.BOLD);
        tvr.setGravity(Gravity.CENTER);
        tvr.setLayoutParams(LLcmpweight);

        LinearLayout.LayoutParams LLbtnweight = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LLbtnweight.weight = (float) 0.5;

        Button bntres = new Button(context);
        bntres.setId(rt.getId().intValue());
        bntres.setText("-");
        bntres.setTextSize(20);
        bntres.setTextColor(Color.parseColor("#ffffff"));
        bntres.setBackgroundResource(R.drawable.btnres);
        bntres.setLayoutParams(LLbtnweight);

        Button bntsum = new Button(context);
        bntsum.setId(rt.getId().intValue());
        bntsum.setText("+");
        bntsum.setTextSize(20);
        bntsum.setTextColor(Color.parseColor("#ffffff"));
        bntsum.setBackgroundResource(R.drawable.btnsum);
        bntsum.setLayoutParams(LLbtnweight);

        llbtn.addView(bntres);
        llbtn.addView(tvr);
        llbtn.addView(bntsum);

        viewTotal = llbtn;

        int data = Integer.parseInt(tvr.getText().toString());
        ResSum(data, rt.getRespuesta() == null, tvr);


        bntsum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = Integer.parseInt(tvr.getText().toString());
                if (n < rt.getReglas()) {
                    int rta = n + 1;
                    String data = ResSum(rta, false, tvr);
                    tvr.setText(data);

                    String vlr = valor(rta);
                    tvpor.setText((rta < 0) ? "Resultado: " : "Resultado: \n" + vlr);

                    try {
                        registro(data, vlr);
                    } catch (Exception e) {
                        Log.i("Error_Crs", e.toString());
                    }
                }

            }
        });

        bntres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = Integer.parseInt(tvr.getText().toString());
                if (n >= 0) {
                    int rta = n - 1;
                    String data = ResSum(rta, false, tvr);
                    tvr.setText(data);

                    String vlr = valor(rta);
                    tvpor.setText((rta < 0) ? "Resultado: \n NA" : "Resultado: \n" + vlr);
                    try {
                        registro(data, (rta < 0) ? "-1" : vlr);
                    } catch (Exception e) {
                        Log.i("Error_Crs", e.toString());
                    }
                }
            }

        });


        return viewTotal;
    }

    public String ResSum(int rta, boolean inicial, TextView tvr) {
        String data = String.valueOf(rta);
        try {
            if (inicial && data.equals("-1")) {
                tvr.setVisibility(View.INVISIBLE);
            } else if (!inicial && rta > -1) {
                tvr.setVisibility(View.VISIBLE);
            } else if (!inicial && rta <= 0) {
                tvr.setVisibility(View.VISIBLE);
                registro("-1", "-1");
            }
        } catch (Exception ex) {
            Toast.makeText(context, "" + ex.toString(), Toast.LENGTH_SHORT).show();
        }

        return data;
    }

    public String valor(int rta) {
        try {
            DecimalFormatSymbols separator = new DecimalFormatSymbols();
            separator.setDecimalSeparator('.');

            DecimalFormat decimalFormat = new DecimalFormat("#.##",separator);
            float operacion = Float.parseFloat((rta < 0) ? "-1" : String.valueOf((rt.getPonderado() / rt.getReglas()) * (rt.getReglas() - rta)));

            return String.valueOf(decimalFormat.format(operacion));
        }catch (Exception ex){
            Toast.makeText(context, ""+ex.toString(), Toast.LENGTH_SHORT).show();
            return "";
        }
    }

    public void registro(String rta, String valor) throws Exception {
        new iContenedor(path).editarTemporal(ubicacion, rt.getId().intValue(), rta, String.valueOf(valor));
    }

    //retorna el layout del encabezado pregunta porcentaje
    public LinearLayout pp(View v1, View v2) {
        LinearLayout.LayoutParams llparamspregunta = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        LinearLayout llpregunta = new LinearLayout(context);
        llpregunta.setLayoutParams(llparamspregunta);
        llpregunta.setWeightSum(3);
        llpregunta.setOrientation(LinearLayout.HORIZONTAL);
        llpregunta.setBackgroundColor(Color.parseColor("#00FFFFFF"));

        llpregunta.addView(v1); //retorna pregunta
        llpregunta.addView(v2); //retorna pocentaje

        return llpregunta;
    }

}
