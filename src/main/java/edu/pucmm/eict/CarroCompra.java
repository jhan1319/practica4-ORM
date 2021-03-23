package edu.pucmm.eict;

import java.util.ArrayList;

public class CarroCompra{
    static int autoIncrement = 1;
    long id;
    ArrayList<Producto> listaProductos;

    public CarroCompra() {
        this.id = CarroCompra.autoIncrement;
        this.listaProductos = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ArrayList<Producto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(ArrayList<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    public void agregarProductoCarrito(int id, String nombre, float precio, int cant){
        Producto e = new Producto(nombre, precio, cant);
        e.setId(id);
        listaProductos.add(e);
    }
    public void eliminarProductoCarrito(int ide){

        listaProductos.removeIf(prod -> prod.getId() == ide);



    }

    public void limpiarCarrito(){
        listaProductos.clear();
    }
}