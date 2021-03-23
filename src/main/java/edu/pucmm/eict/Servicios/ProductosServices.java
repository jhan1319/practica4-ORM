package edu.pucmm.eict.Servicios;

import edu.pucmm.eict.Producto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.List;

public class ProductosServices extends GestionDB<Producto> {

    private static ProductosServices instancia;

    public ProductosServices() {
        super(Producto.class);
    }

    public static ProductosServices getInstancia(){
        if (instancia == null){
            instancia = new ProductosServices();
        }
        return instancia;
    }


    //retorna todos los productos de la base de datos
    public List<Producto> findAllProducts() throws PersistenceException {
        EntityManager em = getEntityManager();

        Query query = em.createQuery("select e from Producto e");

        List<Producto> lista = query.getResultList();
        return lista;
    }

    public List<Producto> findAllProductspaginados( int numPag  ) throws PersistenceException {

        EntityManager em = getEntityManager();

        Query pag = em.createQuery("SELECT COUNT (p.id) FROM Producto p");

        long contador = (Long) pag.getSingleResult();
        int lastPageNumber = (int) (Math.ceil(contador / 10));

        Query query = em.createQuery("select e from Producto e");

        query.setFirstResult((numPag -1)*10);
        query.setMaxResults(10);

        if(contador >= 11){
            List<Producto> lista = query.getResultList();
            return lista;
        } else {
           return findAllProducts();
        }



    }






}
