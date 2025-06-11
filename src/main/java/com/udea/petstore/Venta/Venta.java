package com.udea.petstore.Venta;

import com.udea.petstore.Producto.Producto;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
public class Venta {

    @Id
    @GeneratedValue
    private Long id;
    private String usuario;
    private Double total;
    private Boolean ventaespecial = false;
    private int cantidadproductosventa;
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    public Venta() {
    }

    public Venta(String usuario, Double total,Boolean ventaespecial, int cantidadproductosventa, LocalDateTime fechaCreacion, Producto producto) {
        this.usuario = usuario;
        this.total = total;
        this.ventaespecial = ventaespecial;
        this.cantidadproductosventa = cantidadproductosventa;
        this.fechaCreacion = fechaCreacion;
        this.producto = producto;
    }

    public Producto getProducto() {
        return producto;
    }
    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getVentaespecial() {
        return ventaespecial;
    }

    public void setVentaespecial(Boolean ventaespecial) {
        this.ventaespecial = ventaespecial;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getCantidadProductosVenta() {return cantidadproductosventa; }

    public void setCantidadProductosVenta(int cantidadproductosventa) {
        this.cantidadproductosventa = cantidadproductosventa;
    }

    @Override
    public String toString() {
        return "Venta{" +
                "id=" + id +
                ", usuario='" + usuario + '\'' +
                ", total=" + total +
                ", ventaespecial=" + ventaespecial +
                ", cantidadproductosventa=" + cantidadproductosventa +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}
