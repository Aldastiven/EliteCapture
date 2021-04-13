package com.example.eliteCapture.Config.Util.secondTaks;

import android.util.Log;

import java.util.logging.Level;
import java.util.logging.Logger;

public class getUpdate extends Thread{
    @Override
    public void run() {
        super.run();

        int conta1 = 0;
        while(true){
            if (conta1==9){
                conta1=0;
            }
            else {
                conta1++;
            }

            Log.i("secondTasks", "data : "+conta1);

            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(getUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
