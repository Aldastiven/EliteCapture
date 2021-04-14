package com.example.eliteCapture.Config.Util.secondTaks;

import android.util.Log;
import android.widget.TextView;

public class getTimeTaks extends Thread{
    long initialTime = System.currentTimeMillis(), timeMinutes = 0, timeSeconds;
    String timeChar, nameTask;
    TextView timeText;
    int terminadeTime = 0;

    @Override
    public void run() {
        super.run();
        getTime();
    }

    public int getTerminadeTime() {
        return terminadeTime;
    }

    public void setTerminadeTime(int terminadeTime) {
        this.terminadeTime = terminadeTime;
    }

    public String getNameTask() {
        return nameTask;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }

    public void setTimeText(TextView timeText) {
        this.timeText = timeText;
    }

    public void setTimeChar(String timeChar) {
        this.timeChar = timeChar;
        if(timeText != null){
            timeText.setText("Tomando tiempo  : " + this.timeChar);
        }
    }

    private void initialTime(){
        initialTime = System.currentTimeMillis();
        timeSeconds = 0;
    }

    public void getTime(){
        try {
            //obtiene el tiempo inicial de la tarea
            timeSeconds = (System.currentTimeMillis() - initialTime) / 1000;
            if (timeSeconds == 60) {
                initialTime();
                timeMinutes = timeMinutes + 1;
            }

            String minutesChar = (timeMinutes > 0 ? timeMinutes + " min. , " : ""),
                    secondsChar = timeSeconds + " seg. ";

            setTimeChar(minutesChar + secondsChar);

            Thread.sleep(1000);

            if(timeSeconds < getTerminadeTime()){
                getTime();
            }else{//temino la tarea
                timeText.setText("Termino la tarea : "+getNameTask());
            }
        }catch (Exception e){
            Log.i("secondTasks", "Error en la tarea  : "+e.toString());
        }
    }
}
