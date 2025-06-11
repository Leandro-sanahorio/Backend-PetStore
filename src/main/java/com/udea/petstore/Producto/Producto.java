package com.udea.petstore.Producto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Producto {
    @Id
    @GeneratedValue
    private Long id;
    private String nombre ;
    private String descripcion;
    private String categoria ;
    private Float precio;
    private Long cantidadDisponible;
    @Column(name = "productoVendidos")
    private Integer productoVendidos;

    public Producto() {}

    public Producto(String nombre, String descripcion, String categoria, Float precio,Long cantidadDisponible, Integer productoVendidos) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.precio = precio;
        this.cantidadDisponible = cantidadDisponible;
        this.productoVendidos = productoVendidos;
    }

    public Integer getProductoVendidos() {
        return productoVendidos;
    }

    public void setProductoVendidos(Integer productoVendidos) {
        this.productoVendidos = productoVendidos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Float getprecio() {
        return precio;
    }

    public void setprecio(Float precio) {
        this.precio = precio;
    }

    public Long getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(Long cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", categoria='" + categoria + '\'' +
                ", precio=" + precio +
                ", cantidadDisponible=" + cantidadDisponible +
                '}';
    }
}
