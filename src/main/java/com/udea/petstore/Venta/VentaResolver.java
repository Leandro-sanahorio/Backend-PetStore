package com.udea.petstore.Venta;
import com.udea.petstore.Producto.Producto;
import com.udea.petstore.Producto.ProductoRepository;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.graphql.data.method.annotation.Argument;
import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;
import java.time.LocalDateTime;

@Controller
public class VentaResolver {
    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;

    public VentaResolver(VentaRepository ventaRepository, ProductoRepository productoRepository) {
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public List<Venta> ventas() {
        return ventaRepository.findAll();
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public Venta venta(@Argument Long id) {
        return ventaRepository.findById(id).orElseThrow(() -> new RuntimeException("venta no encontrado"));
    }

    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Venta> ventasDeHoy() {
        LocalDateTime inicio = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime fin = inicio.plusDays(1);
        return ventaRepository.findByFechaCreacionBetween(inicio, fin);
    }

    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Venta> ventasUltimaSemana() {
        LocalDateTime fin = LocalDateTime.now();
        LocalDateTime inicio = fin.minusWeeks(1);
        return ventaRepository.findByFechaCreacionBetween(inicio, fin);
    }

    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Venta> ventasUltimoMes() {
        LocalDateTime fin = LocalDateTime.now();
        LocalDateTime inicio = fin.minusMonths(1);
        return ventaRepository.findByFechaCreacionBetween(inicio, fin);
    }

    public record VentaInput(String usuario,Boolean ventaespecial, int cantidadProductosVenta,Long producto) {}

    @MutationMapping(name = "insertarVenta")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public Venta insertarVenta(@Argument VentaInput ventaInput) {
        Producto producto = productoRepository.findById(ventaInput.producto()).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        if (ventaInput.cantidadProductosVenta() <= 0) {
            throw new RuntimeException("La cantidad de productos vendidos debe ser mayor a cero");
        }
        if (producto.getCantidadDisponible() < ventaInput.cantidadProductosVenta()) {
            throw new RuntimeException("No hay stock suficiente del producto para realizar la venta");
        }
        Venta venta = new Venta();
        String usuarioAutenticado = SecurityContextHolder.getContext().getAuthentication().getName();
        venta.setUsuario(usuarioAutenticado);
        venta.setVentaespecial(ventaInput.cantidadProductosVenta() >= 30);
        venta.setProducto(producto);
        venta.setCantidadProductosVenta(ventaInput.cantidadProductosVenta());
        venta.setTotal((double) (producto.getprecio() * ventaInput.cantidadProductosVenta()));
        venta.setFechaCreacion(LocalDateTime.now());
        producto.setCantidadDisponible(producto.getCantidadDisponible() - ventaInput.cantidadProductosVenta());
        producto.setProductoVendidos(producto.getProductoVendidos() + ventaInput.cantidadProductosVenta());
        productoRepository.save(producto);
        return ventaRepository.save(venta);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Venta updateVenta(@Argument Long id, @Argument VentaInput ventaInput) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        Producto productoAnterior = venta.getProducto();
        int cantidadAnterior = venta.getCantidadProductosVenta();

        Producto nuevoProducto = productoRepository.findById(ventaInput.producto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (ventaInput.cantidadProductosVenta() <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a cero");
        }

        // Revertir los efectos de la venta anterior
        productoAnterior.setCantidadDisponible(productoAnterior.getCantidadDisponible() + cantidadAnterior);
        productoAnterior.setProductoVendidos(productoAnterior.getProductoVendidos() - cantidadAnterior);
        productoRepository.save(productoAnterior);

        // Validar stock del nuevo producto
        if (nuevoProducto.getCantidadDisponible() < ventaInput.cantidadProductosVenta()) {
            throw new RuntimeException("No hay stock suficiente del producto para realizar la venta");
        }

        // Aplicar la nueva venta
        nuevoProducto.setCantidadDisponible(nuevoProducto.getCantidadDisponible() - ventaInput.cantidadProductosVenta());
        nuevoProducto.setProductoVendidos(nuevoProducto.getProductoVendidos() + ventaInput.cantidadProductosVenta());
        productoRepository.save(nuevoProducto);

        // Actualizar datos de la venta
        String usuarioAutenticado = SecurityContextHolder.getContext().getAuthentication().getName();
        venta.setUsuario(usuarioAutenticado);
        venta.setVentaespecial(ventaInput.cantidadProductosVenta() >= 30);
        venta.setProducto(nuevoProducto);
        venta.setCantidadProductosVenta(ventaInput.cantidadProductosVenta());
        venta.setTotal((double) (nuevoProducto.getprecio() * ventaInput.cantidadProductosVenta()));
        venta.setFechaCreacion(LocalDateTime.now());

        return ventaRepository.save(venta);
    }


    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Boolean deleteVenta(@Argument Long id) {
        Venta venta = ventaRepository.findById(id).orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        Producto producto = venta.getProducto();
        int cantidad = venta.getCantidadProductosVenta();
        producto.setCantidadDisponible(producto.getCantidadDisponible() + cantidad);
        producto.setProductoVendidos(producto.getProductoVendidos() - cantidad);
        productoRepository.save(producto);
        ventaRepository.deleteById(id);
        return true;
    }
}
