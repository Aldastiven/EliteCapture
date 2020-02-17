package com.example.eliteCapture.Config.Util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.eliteCapture.R;

public class Dialogo {
	Context context;
	private boolean retorno;

	public Dialogo(Context context) {
		this.context = context;
	}

	public boolean DialogConfirm(String msj) {
		AlertDialog.Builder alertdialog = new AlertDialog.Builder(context, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
		alertdialog.setMessage(msj)
				.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						setRetorno(true);
					}
				})
				.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						setRetorno(false);
					}
				});
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setInverseBackgroundForced(true);
		AlertDialog mensaje = alertdialog.create();
		mensaje.show();

		return isRetorno();

	}


	public boolean isRetorno() {
		return retorno;
	}

	public void setRetorno(boolean retorno) {
		this.retorno = retorno;
	}
}
