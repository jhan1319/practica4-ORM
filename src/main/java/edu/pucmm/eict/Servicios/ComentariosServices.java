package edu.pucmm.eict.Servicios;

import edu.pucmm.eict.entidades.Comentarios;

public class ComentariosServices extends GestionDB<Comentarios>{

    private static ComentariosServices instancia;

    private ComentariosServices() {super(Comentarios.class);}

    public static ComentariosServices getInstancia(){
        if (instancia == null){

            instancia = new ComentariosServices();
        }
        return instancia;
    }



}
