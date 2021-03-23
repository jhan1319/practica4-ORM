package edu.pucmm.eict.Servicios;

import edu.pucmm.eict.VentasProductos;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.List;

public class VentasServices extends GestionDB<VentasProductos> {
    private static VentasServices instancia;

    public static VentasServices getInstancia(){

        if (instancia == null){

            instancia = new VentasServices();
        }
            return instancia;
    }

    public VentasServices() {
        super(VentasProductos.class);
    }
    //retorna todas las ventas de la base de datos

    public List<VentasProductos> findAllVentas() throws PersistenceException {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select e from VentasProductos e");
        List<VentasProductos> lista = query.getResultList();
        return lista;
    }

}
