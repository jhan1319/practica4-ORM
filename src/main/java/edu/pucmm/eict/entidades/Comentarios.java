package edu.pucmm.eict.entidades;

import edu.pucmm.eict.Producto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity
public class Comentarios implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    private String usuario;
    private String comment;

    @ManyToOne
    private Producto producto;

    public Comentarios() {
    }

    public Comentarios(String usuario, String comment, Producto producto) {
        this.usuario = usuario;
        this.comment = comment;
        this.producto = producto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
