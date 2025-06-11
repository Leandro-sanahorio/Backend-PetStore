package com.udea.petstore.Venta;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.graphql.data.method.annotation.Argument;
import java.util.List;

@Controller
public class VentaResolver {
    private final VentaRepository ventaRepository;

    public VentaResolver(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public List<Venta> ventas() {
        return ventaRepository.findAll();
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public Venta venta(@Argument Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("venta no encontrado"));
    }

    public record VentaInput(String usuario, Double total, String mediopago, Boolean ventaespecial, int cantidadProductosVenta) {}

    @MutationMapping(name = "insertarVenta")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public Venta insertarVenta(@Argument VentaInput ventaInput) {
        Venta venta = new Venta();
        venta.setUsuario(ventaInput.usuario());
        venta.setTotal(ventaInput.total());
        venta.setMediopago(ventaInput.mediopago());
        venta.setVentaespecial(ventaInput.ventaespecial());
        venta.setCantidadProductosVenta(ventaInput.cantidadProductosVenta());
        return ventaRepository.save(venta);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Venta updateVenta(@Argument Long id, @Argument VentaInput ventaInput) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        venta.setUsuario(ventaInput.usuario());
        venta.setTotal(ventaInput.total());
        venta.setMediopago(ventaInput.mediopago());
        venta.setVentaespecial(ventaInput.ventaespecial());
        venta.setCantidadProductosVenta(ventaInput.cantidadProductosVenta());

        return ventaRepository.save(venta);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Boolean deleteVenta(@Argument Long id) {
        if (!ventaRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado");
        }
        ventaRepository.deleteById(id);
        return true;
    }
}
