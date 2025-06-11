package com.udea.petstore.Compra;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.graphql.data.method.annotation.Argument;
import java.util.List;

@Controller
public class CompraResolver {

    private final CompraRepository compraRepository;

    public CompraResolver(CompraRepository compraRepository) {
        this.compraRepository = compraRepository;
    }

    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Compra> compras() {
        return compraRepository.findAll();
    }

    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Compra compra(@Argument Long id) {
        return compraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));
    }

    public record CompraInput(String nombreproducto, int cantidadProductosCompra) {}

    @MutationMapping(name = "insertarCompra")
    @PreAuthorize("hasRole('ADMIN')")
    public Compra insertarCompra(@Argument CompraInput compraInput) {
        Compra compra = new Compra();
        compra.setNombreProducto(compraInput.nombreproducto());
        compra.setCantidadProductosCompra(compraInput.cantidadProductosCompra());
        return compraRepository.save(compra);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Boolean deleteCompra(@Argument Long id) {
        compraRepository.deleteById(id);
        return true;
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Compra updateCompra(@Argument Long id, @Argument CompraInput compraInput) {
        Compra compra = compraRepository.findById(id).orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        compra.setNombreProducto(compraInput.nombreproducto());
        compra.setCantidadProductosCompra(compraInput.cantidadProductosCompra());
        return compraRepository.save(compra);
    }
}
