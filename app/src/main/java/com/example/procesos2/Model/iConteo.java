package com.example.procesos2.Model;


import android.content.Context;
import android.widget.TableLayout;
import android.widget.Toast;

import com.example.procesos2.Config.Util.JsonAdmin;
import com.example.procesos2.Config.sqlConect;
import com.example.procesos2.Model.interfaz.procesos;
import com.example.procesos2.Model.tab.procesosTap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class iConteo extends sqlConect implements procesos {

    private TableLayout tableLayout;

    private Context context;

    public String nombre;
    final String ins =  "INSERT INTO [dbo].[Datos_Calidad_Pos1] ([Id_Proceso] ,[fecha] ,[Dato1] ,[Dato2] ,[Dato3] ,[Dato4] ,[Dato5] ,[Dato6] ,[Dato7], [Dato8] ,[Dato9] ,[Dato10] ,[Dato11] ,[Dato12] ,[Dato13] ,[Dato14] ,[Dato15] ,[Dato16] ,[Dato17] ,[Dato18] ,[Dato19] ,[Dato20] ,[IdUsuario]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


    Connection cn = null;
    String path = null;
    JsonAdmin ja = null;


    private List<procesosTap> cl = new ArrayList<>();


    SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
    private procesosTap c;
    private int id;


    public iConteo(String path) throws Exception {
        this.cn = getConexion();
        getPath(path);
    }

    public void getPath(String path) {
        ja = new JsonAdmin();
        this.path = path;
    }


    @Override
    public String insert(procesosTap c) {
        try {
            cl.add(c);
            local();
            return "Registro realizado exitosamente";
        } catch (Exception e) {
            return "Error: " + e.toString();
        }
    }


    @Override
    public String update(procesosTap c, Long id) {
        String msj="";
        try {
            all();
            int id2 = (int) id.longValue()-1;
            cl.set(id2,c);
            local();
            msj="se actualizo exitosamente";
            return msj;
        } catch (Exception e) {
            msj = "Error excepcion update :\n " + e.toString();
            return msj;
        }
    }

    @Override
    public String delete(Long id) {
        String msj="";
        try {
            all();
            int id2 = (int) id.longValue()-1;
            cl.remove(id2);
            local();

            msj="Registro eliminado con exito ";
            return msj;
        } catch (Exception e) {
            msj = "Error excepcion delete :\n \n " + e.toString();
            return msj;
        }
    }



    @Override
    public String limpiar(procesosTap c) {
        String msj="";
        try {

            int bl = c.getBloque();
            all();
            //cl.remove(id);
            local();

            msj="se elimino exitosamente el bloque \n"+bl;
            return msj;
        } catch (Exception e) {
            msj = "Error excepcion limpiar :\n " + e.toString();
            return msj;
        }
    }

    @Override
    public procesosTap oneId(Long id) throws Exception {
        procesosTap cr = new procesosTap();

        for (procesosTap c : cl) {
            if (c.getCodigo() == id) {
                cr = c;
            }
        }

        return cr;
    }

    @Override
    public boolean local() throws Exception {
        String contenido = cl.toString();
        nombre = "procesos_2";
        return ja.WriteJson(path, nombre, contenido);
    }

    @Override
    public List<procesosTap> all() throws Exception {
            Gson gson = new Gson();
            cl = gson.fromJson(ja.ObtenerLista(path, nombre), new TypeToken<List<procesosTap>>() {
            }.getType());

            if(cl==null){
                Toast.makeText(context,"",Toast.LENGTH_LONG).show();
            }else {
                return cl;
            }

            return cl;
    }


    @Override
    public String send(List<procesosTap> ls)



    {
        String msj = "";
        for (procesosTap c : cl) {
            msj = msj + record(c) + "\n";
        }
        return msj;
    }

    public String deleteAfter(String o, String c){

        String msj = "";
        try{
            PreparedStatement ps = cn.prepareStatement("delete from conteos where IdSiembra in (select idsiembra from plano_siembra where idfinca=555)\n" +
                    "  and fecha = ? and idUsuario = ? \n");

            ps.setString(1, o);
            ps.setString(2, c);

            if (ps.executeUpdate() == 0) {
                msj = "Se subieros los registros exitosamente";
            } else {
                msj = "Conteo de la siembra #" + o + " eliminada exitosamente";
            }

        }catch (Exception e){
            msj="exception \n"+e;
        }

     return msj;
    }

    public String record(procesosTap o) {
        String msj = "";
        try {
            PreparedStatement ps = cn.prepareStatement(ins);
            ps.setInt(1,Integer.parseInt("2"));
            ps.setString(2, o.getFecha());
            ps.setString(3, String.valueOf(o.getCodigo()));
            ps.setString(4, String.valueOf(o.getBloque()));
            ps.setString(5, String.valueOf(o.getCama()));
            ps.setString(6, String.valueOf(o.getFinca()));
            ps.setString(7, String.valueOf(o.getPr1()));
            ps.setString(8, String.valueOf(o.getPr2()));
            ps.setString(9, String.valueOf(o.getPr3()));
            ps.setString(10, String.valueOf(o.getPr4()));
            ps.setString(11, String.valueOf(o.getPr5()));
            ps.setString(12, String.valueOf(o.getPr6()));
            ps.setString(13, String.valueOf(o.getPr7()));
            ps.setString(14, String.valueOf(o.getPr8()));
            ps.setString(15, String.valueOf(o.getPr9()));
            ps.setString(16, String.valueOf(o.getPr10()));
            ps.setString(17, String.valueOf(o.getPr11()));
            ps.setString(18, String.valueOf(o.getPr12()));
            ps.setString(19, String.valueOf(o.getPr13()));
            ps.setString(20, String.valueOf(o.getPr14()));
            ps.setString(21, null);
            ps.setString(22, null);
            ps.setString(23, String.valueOf(o.getTerminal()));


            if (ps.executeUpdate() == 0) {
                msj = "Ocurrio un error al subir los datos";
            } else {
                msj = "Se subieros los registros exitosamente" ;
                all().clear();
                local();
            }
            return msj;
        } catch (Exception e) {
            msj = e.toString();
        }
        return msj;
    }
}
