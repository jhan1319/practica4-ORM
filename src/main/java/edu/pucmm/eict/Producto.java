package edu.pucmm.eict;

import edu.pucmm.eict.entidades.Comentarios;
import edu.pucmm.eict.entidades.FotosEntidad;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity()
public class Producto implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    private String nombre;
    private float precio;
    private int quantity;
    private String descripcion;

    @ManyToMany(mappedBy = "productoSet")
    private List<VentasProductos> venta;

    @OneToMany (mappedBy = "productox")
    private List<FotosEntidad> fotos;

    @OneToMany(mappedBy = "producto", fetch = FetchType.EAGER)
    private List<Comentarios> productoComment;

    public List<Comentarios> getProductoComment() {
        return productoComment;
    }

    public void setProductoComment(List<Comentarios> productoComment) {
        this.productoComment = productoComment;
    }

    public Producto(String nombre, float precio, int quantity, String descripcion) {
        this.nombre = nombre;
        this.precio = precio;
        this.quantity = quantity;
        this.descripcion = descripcion;
    }

    public Producto() {
    }

    public List<FotosEntidad> getFotos() {
        return fotos;
    }

    public void setFotos(List<FotosEntidad> fotos) {
        this.fotos = fotos;
    }

    public Producto(String nombre, float precio, int quantity, List<FotosEntidad> fotos) {
        this.nombre = nombre;
        this.precio = precio;
        this.quantity = quantity;
        this.fotos = fotos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Producto(String nombre, float precio, int quantity) {
        this.nombre = nombre;
        this.precio = precio;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}