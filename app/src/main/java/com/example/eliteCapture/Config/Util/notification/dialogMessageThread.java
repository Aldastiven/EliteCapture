package com.example.eliteCapture.Config.Util.notification;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class dialogMessageThread extends AsyncTask<String, Void, Void> {

    ProgressDialog progress;

    String dataText;

    public dialogMessageThread(String dataText, Context context) {
        this.dataText = dataText;
        progress = new ProgressDialog(context);
        setProgress(progress);
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

    @Override
    protected void onCancelled() {
        super.onCancelled();
        progress.dismiss();
    }

    public ProgressDialog getProgress() {
        return progress;
    }

    public void setProgress(ProgressDialog progress) {
        this.progress = progress;
    }
}
