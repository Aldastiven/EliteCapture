package com.example.procesos2.Tools;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;
import com.example.procesos2.R;
import com.example.procesos2.genated;
import java.util.Timer;
import java.util.TimerTask;

public class Dialog extends genated {

    private ProgressDialog progress;
    private Context context;

    /*public Dialog(Context context){
        this.context = context;
    }*/

    public void Dialog(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        builder.setTitle("Rosen Tantau");
        builder.setMessage(msg);
        builder.setCancelable(true);
        //builder.setIcon(R.drawable.ok);

        final AlertDialog dialog= builder.create();

        dialog.show();

        final Timer timer2 = new Timer();
        timer2.schedule(new TimerTask() {
            public void run() {
                dialog.dismiss();
                timer2.cancel();
            }
        }, 3000);

    }

    public void progressBar(String msgCarga, final String msgCompletado, final int tiempoSalteado){
        try {
            progress = new ProgressDialog(context, R.style.MyProgressDialogDespues);
            progress.setMessage(msgCarga);
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setProgress(0);
            progress.show();

            final int totalProgressTime = 100;
            final Thread t = new Thread() {
                @Override
                public void run() {
                    int jumpTime = 0;

                    while (jumpTime < totalProgressTime) {
                        try {
                            jumpTime += tiempoSalteado;
                            progress.setProgress(jumpTime);
                            sleep(200);
                        } catch (InterruptedException e) {
                            Toast.makeText(context, "Progress Bar \n" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run()
                        {
                            progress.setMessage(msgCompletado);
                            progress.setProgressStyle(R.style.MyProgressDialogDespues);
                            int dur = 2500;
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    progress.dismiss();
                                }
                            }, dur);

                        }
                    });

                }
            };
            t.start();

        }catch (Exception ex){
            Toast.makeText(context, ""+ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

}
