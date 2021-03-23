package edu.pucmm.eict.Servicios;

import edu.pucmm.eict.entidades.FotosEntidad;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.text.html.parser.Entity;

public class FotoServices extends GestionDB<FotosEntidad> {

    private static FotoServices instancia;

    private FotoServices() {super(FotosEntidad.class);}


    public static FotoServices getInstancia(){
        if (instancia == null){
            instancia = new FotoServices();
        }
        return instancia;
    }
    public void deleteFoto(int id){
        EntityManagerFactory em = Persistence.createEntityManagerFactory("MiUnidadPersistencia");
        EntityManager entMan = em.createEntityManager();
        entMan.getTransaction().begin();
        Query qr = entMan.createQuery("DELETE FROM FotosEntidad f WHERE f.productox.id = " + id);
        entMan.getTransaction().commit();
        entMan.close();
    }




}
