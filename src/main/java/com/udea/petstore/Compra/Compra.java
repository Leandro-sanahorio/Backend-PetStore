package com.udea.petstore.Compra;
import com.udea.petstore.Producto.Producto;
import jakarta.persistence.*;

@Entity
public class Compra {

    @Id
    @GeneratedValue
    private Long id;

    private int cantidadProductosCompra;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    public Compra() {
    }

    public Compra(Long id, int cantidadProductosCompra, Producto producto) {
        this.id = id;
        this.cantidadProductosCompra = cantidadProductosCompra;
        this.producto = producto;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public int getCantidadProductosCompra() {
        return cantidadProductosCompra;
    }

    public void setCantidadProductosCompra(int cantidadProductosCompra) {
        this.cantidadProductosCompra = cantidadProductosCompra;
    }

    @Override
    public String toString() {
        return "Compra{" +
                "id=" + id +
                ", cantidadProductosCompra=" + cantidadProductosCompra +
                ", producto=" + producto +
                '}';
    }
}
