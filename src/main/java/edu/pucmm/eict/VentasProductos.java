package edu.pucmm.eict;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity()
public class VentasProductos implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    private Date fechaCompra;
    private String nombreCliente;

    @ManyToMany
    private List<Producto> productoSet;



    public VentasProductos() {
    }

    public VentasProductos(Date fechaCompra, String nombreCliente, List<Producto> productoSet) {
        this.fechaCompra = fechaCompra;
        this.nombreCliente = nombreCliente;
        this.productoSet = productoSet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public List<Producto> getProductoSet() {
        return productoSet;
    }

    public void setProductoSet(List<Producto> productoSet) {
        this.productoSet = productoSet;
    }
}