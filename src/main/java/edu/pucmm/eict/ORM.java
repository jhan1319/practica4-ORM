package edu.pucmm.eict;

import edu.pucmm.eict.Servicios.ProductosServices;
import org.h2.tools.Server;

import java.sql.SQLException;

public class ORM {

    private static ORM instancia;


    public ORM() {
    }


    public static ORM getInstance(){
        if(instancia == null){
            instancia = new ORM();
        }
        return instancia;
    }


    public void startDB(){

        try {
            //Modo servidor H2.
            Server.createTcpServer("-tcpPort",
                    "9092",
                    "-tcpAllowOthers",
                    "-tcpDaemon",
                    "-ifNotExists").start();
            //Abriendo el cliente web. El valor 0 representa puerto aleatorio.
            String status = Server.createWebServer("-trace", "-webPort", "0").start().getStatus();
            //
            System.out.println("Status Web: "+status);
            ProductosServices.getInstancia().getEntityManager();
        }catch (SQLException ex){
            System.out.println("Problema con la base de datos: "+ex.getMessage());
        }


    }
}



