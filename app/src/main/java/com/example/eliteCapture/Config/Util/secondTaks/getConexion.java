package com.example.eliteCapture.Config.Util.secondTaks;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;

import com.example.eliteCapture.Config.sqlConect;
import java.sql.Connection;

public class getConexion{

    Activity act;

    String conexionEstableshed = "", getIntentConexion = "", IntentTermined = "", intermitendStatus = "";
    int intentosConexion = 0, intentosDefinidos = 3;
    long initialTimeConexion = System.currentTimeMillis(), timeSeconds;
    Connection cn = null;
    Boolean terminated = false;

    TextView txtConexionStatus;

    public getConexion(Activity act, TextView txtConexionStatus, int intentosDefinidos) {
        this.act = act;
        this.txtConexionStatus = txtConexionStatus;
        this.intentosDefinidos = intentosDefinidos;
        initialMessage();
        new getConexionThread().start();
    }

    public Connection getCn() {
        return cn;
    }

    public void setCn(Connection cn) {
        //envia la conexion al encapsulamiento
        this.cn = cn;
    }

    public int getIntentosConexion() {
        return intentosConexion;
    }

    public void setIntentosConexion(int intentosConexion) {
        this.intentosConexion = intentosConexion;
    }

    public long getTimeSeconds() {
        return timeSeconds;
    }

    public void setTimeSeconds(long timeSeconds) {
        this.timeSeconds = timeSeconds;
    }

    public Boolean getTerminated() {
        return terminated;
    }

    public void setTerminated(Boolean terminated) {
        this.terminated = terminated;
    }

    public void initialMessage(){
        conexionEstableshed = " Conexion establecida, tiempo de ejecución : "+getTimeSeconds()+" Seg.";
        getIntentConexion = " Termino el tiempo de conexion, intentando "+getIntentosConexion()+"/"+intentosDefinidos;
        IntentTermined = " !SIN CONEXION¡ Termino el tiempo de conexion, se utilizaron "+getIntentosConexion()+"/"+intentosDefinidos+" intentos";
        intermitendStatus = " comprobando la conexión"+", tiempo "+getTimeSeconds()+" Seg., intentos : "+getIntentosConexion()+"/"+intentosDefinidos;
    }

    class getConexionThread extends Thread{

        @Override
        public void run() {
            super.run();
            comenzarTareas();
        }

        public void comenzarTareas(){
            //INICIA LOS HILOS REQURIDOS, TIEMPOS E INTENTOS
            initialTimeConexion = System.currentTimeMillis();

            setIntentosConexion(intentosConexion + 1); //SUMATORIA DE INTENTOS EM LA CONEXIÓN

            Thread takeTime = takeTime();
            takeTime.start();
            validateConexion().start();
            waitTime(takeTime);
        }

        public boolean waitTime(Thread thread){
            try {
                initialMessage();
                if (thread.getState() == State.TERMINATED || cn != null) {
                    if(cn != null){
                        setCn(cn);
                        getMessage(conexionEstableshed, false, 1);
                        setTerminated(true);
                    }else if(intentosConexion < intentosDefinidos){
                        comenzarTareas();
                    }else if(intentosConexion == intentosDefinidos){
                        getMessage(IntentTermined, true, 2);
                        setTerminated(true);
                    }
                    return true;
                }else{
                    getMessage(intermitendStatus, true, 0);
                    waitTime(thread);
                    return false;
                }
            }catch (Exception e){
                Log.i("downloadFarms", "termino la tarea pero con error : "+e.toString());
                return false;
            }
        }

        public void getMessage(String msg, boolean wait, int type) throws Exception{
            //actualiza estatus
            Log.i("downloadFarms", msg);
            paintTextStatus(msg, type);
            Thread.sleep(wait ? 1000 : 0);
        }

        public Thread takeTime(){
            //INICIA LA TOMA DE TIEMPOS
            return new Thread(() -> {
                getTime();
            });
        }

        public void getTime(){
            try {
                //OBTIENE TIEMPOS DE LA CONEXION EN SEGUNTOS Y TERMINA LA EJECUCION DE ESTA
                setTimeSeconds((System.currentTimeMillis() - initialTimeConexion) / 1000);
                Thread.sleep(1000);
                if(timeSeconds < 3600){
                    getTime();
                }
            }catch (Exception e){
                Log.i("secondTasks", "Error en la tarea  : "+e.toString());
            }
        }

        public Thread validateConexion(){
            //INCIA LA CONEXION AL SERVIDOR
            return  new Thread(() -> {
                cn = new sqlConect().getConexion();
            });
        }

        public void paintTextStatus(String data, int typeMsg){
            //pinta el color del texto
            act.runOnUiThread(() -> {
                if(txtConexionStatus != null){
                    txtConexionStatus.setText(data);

                    String color = "#154360";//normal, gray

                    if(typeMsg == 1){
                        color = "#2ecc71";//ok, verde
                    }if(typeMsg == 2){
                        color = "#E74C3C";//error, rojo
                    }

                    txtConexionStatus.setTextColor(Color.parseColor(color));
                }
            });
        }
    }
}


