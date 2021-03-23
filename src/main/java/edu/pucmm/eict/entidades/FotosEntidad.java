package edu.pucmm.eict.entidades;


import edu.pucmm.eict.Producto;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class FotosEntidad implements Serializable {

    @Id
    @GeneratedValue
    private long id;
    private String nombre;
    private String mimeType;
    @Lob
    private String fotoBase64;

    @ManyToOne(fetch = FetchType.EAGER)
    private Producto productox;



    public FotosEntidad() {
    }

    public FotosEntidad(String nombre, String mimeType, String fotoBase64) {
        this.nombre = nombre;
        this.mimeType = mimeType;
        this.fotoBase64 = fotoBase64;
    }

    public FotosEntidad(String nombre, String mimeType, String fotoBase64, Producto productox) {
        this.nombre = nombre;
        this.mimeType = mimeType;
        this.fotoBase64 = fotoBase64;
        this.productox = productox;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getFotoBase64() {
        return fotoBase64;
    }

    public void setFotoBase64(String fotoBase64) {
        this.fotoBase64 = fotoBase64;
    }
}
