package com.example.eliteCapture.Config.Util.ControlViews;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteCapture.Config.sqlConect;
import com.example.eliteCapture.Model.Data.iDesplegable;
import com.example.eliteCapture.Model.Data.Tab.DesplegableTab;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.Model.View.iContenedor;
import com.example.eliteCapture.R;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class CradioButton {
	View ControlView;

	Context context;
	String path;
	String ubicacion;
	Long id;
	String pregunta;
	float ponderado;
	List<DesplegableTab> desplegable;
	RespuestasTab rt;

	public CradioButton(Context context, String path, String ubicacion, Long id, String pregunta, float ponderado, List<DesplegableTab> desplegable, RespuestasTab rt) {
		this.context = context;
		this.path = path;
		this.ubicacion = ubicacion;
		this.id = id;
		this.pregunta = pregunta;
		this.ponderado = ponderado;
		this.desplegable = desplegable;
		this.rt = rt;
	}

	//crea el control del radio button y retorna el view
	public View Tradiobtn() {

		//ORGANIZA LOS CONTROLES INTEGRADOS
		LinearLayout.LayoutParams llparamsTotal = new
				LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

		llparamsTotal.setMargins(0, 0, 0, 10);

		LinearLayout LLtotal = new LinearLayout(context);
		LLtotal.setLayoutParams(llparamsTotal);
		LLtotal.setWeightSum(2);
		LLtotal.setOrientation(LinearLayout.VERTICAL);
		LLtotal.setPadding(10, 30, 10, 12);
		LLtotal.setGravity(Gravity.CENTER_HORIZONTAL);
		LLtotal.setBackgroundResource(R.drawable.bordercontainer);

		LinearLayout ll = new LinearLayout(context);
		RadioGroup rg = new RadioGroup(context);
		rg.setOrientation(LinearLayout.HORIZONTAL);
		rg.setGravity(Gravity.CENTER_HORIZONTAL);

		int i;

		LinearLayout.LayoutParams llparamsText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		llparamsText.weight = (float) 2.3;

		LinearLayout.LayoutParams llparamsTextpo = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		llparamsText.weight = (float) 0.7;

		LinearLayout.LayoutParams llparamsTextitem = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

		TextView tvItem = new TextView(context);
		tvItem.setText("" + id.intValue());
		tvItem.setTextColor(Color.parseColor("#58d68d"));
		tvItem.setTypeface(null, Typeface.BOLD);
		tvItem.setVisibility(View.INVISIBLE);
		tvItem.setTextSize(5);
		tvItem.setLayoutParams(llparamsTextitem);

		final TextView tvp = new TextView(context);
		tvp.setId(id.intValue());
		tvp.setText(pregunta + "\nponderado: " + ponderado);
		tvp.setTextColor(Color.parseColor("#979A9A"));
		tvp.setPadding(5, 5, 5, 5);
		tvp.setBackgroundColor(Color.parseColor("#ffffff"));
		tvp.setTypeface(null, Typeface.BOLD);
		tvp.setLayoutParams(llparamsText);

		final TextView tvpor = new TextView(context);
		tvpor.setText((rt.getValor() != null) ? "Resultado: \n " + rt.getValor() : "Resultado: \n ");
		tvpor.setTextColor(Color.parseColor("#979A9A"));
		tvpor.setBackgroundColor(Color.parseColor("#ffffff"));
		tvpor.setPadding(10, 10, 10, 10);
		tvpor.setTypeface(null, Typeface.BOLD);
		tvpor.setLayoutParams(llparamsTextpo);


		for (i = 0; i <= desplegable.size() - 1; i++) {

			LinearLayout.LayoutParams llrb = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			llrb.setMargins(10, 5, 10, 0);

			final RadioButton rb = new RadioButton(context);
			rb.setText(desplegable.get(i).getOpcion());
			rb.setId(Integer.parseInt(desplegable.get(i).getCodigo()));
			rb.setLayoutParams(llrb);
			rb.setTextSize(12);
			rb.setScaleX((float) 1.10);
			rb.setScaleY((float) 1.10);
			rb.setChecked( (desplegable.get(i).getOpcion().equals(rt.getRespuesta())) );
			rg.addView(rb);
			rg.setPadding(10, 0, 10, 0);

			int id =rb.getId();

			if (id == 1) {//no aplica
				rb.setButtonTintList(ColorSelected(153, 163, 164));
			} else if (id == 0) {//no cumple
				rb.setButtonTintList(ColorSelected(231, 76, 60));
			} else if (id == 2) {//si cumple
				rb.setButtonTintList(ColorSelected(34, 153, 84));
			}

			funRB(rb,tvpor);
		}

		LLtotal.addView(LLPREGUNTA(context, tvp, tvpor));
		LLtotal.addView(tvItem);
		LLtotal.addView(rg);

		ll.addView(LLtotal);
		ControlView = ll;
		return ControlView;
	}

	//retorna view de pregunta y ponderado
	public View LLPREGUNTA(Context c, View v1, View v2) {

		LinearLayout.LayoutParams llparamspregunta = new
				LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

		LinearLayout llpregunta = new LinearLayout(c);
		llpregunta.setLayoutParams(llparamspregunta);
		llpregunta.setWeightSum(3);
		llpregunta.setOrientation(LinearLayout.HORIZONTAL);

		llpregunta.addView(v1); //retorna pregunta
		llpregunta.addView(v2); //retorna pocentaje

		return llpregunta;
	}

	//funcion del radio group
	public void funRB(final RadioButton rb, final TextView tvpor){
		rb.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				try {
					String rta;
					String vlr;
					switch (rb.getId()){
						case 2:
							rta = rb.getText().toString();
							vlr = String.valueOf(rt.getPonderado());
							tvpor.setText("resultado: \n" + vlr);
							registro(rta,vlr);
							break;
						case 0:
							rta = rb.getText().toString();
							vlr = String.valueOf(0);
							tvpor.setText("resultado: \n" + vlr);
							registro(rta,vlr);
							break;
						case 1:
							rta = rb.getText().toString();
							vlr = String.valueOf(-1);
							tvpor.setText("resultado: \n "+ vlr);
							registro(rta,vlr);
							break;
					}

				} catch (Exception ex) {
					Toast.makeText(context, "Exc al insertar en CradioButton.class \n \n " + ex.toString(), Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	//añade el color al radio segun el texto del control
	public ColorStateList ColorSelected(int red, int green, int blue) {
		final ColorStateList colorStateList = new ColorStateList(
				new int[][]{
						new int[]{-android.R.attr.state_checked},
						new int[]{android.R.attr.state_checked}
				},
				new int[]{

						Color.DKGRAY
						, Color.rgb(red, green, blue),
				}
		);

		return colorStateList;
	}

	public void registro(String rta, String valor) throws Exception {
		iContenedor conTemp = new iContenedor(path);
		conTemp.editarTemporal(ubicacion, rt.getId().intValue(), rta, valor);
	}

}
