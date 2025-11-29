package com.cafeteriasoma.app.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cafeteriasoma.app.dto.venta.VentaMapper;
import com.cafeteriasoma.app.dto.venta.VentaRequest;
import com.cafeteriasoma.app.dto.venta.VentaResponse;
import com.cafeteriasoma.app.entity.CarritoTemporal;
import com.cafeteriasoma.app.entity.DetalleVenta;
import com.cafeteriasoma.app.entity.Producto;
import com.cafeteriasoma.app.entity.Usuario;
import com.cafeteriasoma.app.entity.Venta;
import com.cafeteriasoma.app.exception.BadRequestException;
import com.cafeteriasoma.app.exception.ResourceNotFoundException;
import com.cafeteriasoma.app.repository.CarritoTemporalRepository;
import com.cafeteriasoma.app.repository.DetalleVentaRepository;
import com.cafeteriasoma.app.repository.ProductoRepository;
import com.cafeteriasoma.app.repository.UsuarioRepository;
import com.cafeteriasoma.app.repository.VentaRepository;
import com.cafeteriasoma.app.service.interfaces.VentaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final CarritoTemporalRepository carritoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    private static final Set<String> METODOS_PAGO_VALIDOS = Set.of(
            "EFECTIVO", "TARJETA", "YAPE", "PLIN"
    );

    @Override
    public VentaResponse crearVenta(String correo, VentaRequest request) {

        String metodoPagoNormalizado = normalizarMetodoPago(request.getMetodoPago());

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        List<CarritoTemporal> carrito = carritoRepository.findByUsuario(usuario);
        if (carrito.isEmpty()) {
            throw new BadRequestException("El carrito está vacío. Agrega productos antes de realizar la compra.");
        }

        // Validar stock y calcular total
        BigDecimal total = BigDecimal.ZERO;
        for (CarritoTemporal item : carrito) {
            Producto producto = item.getProducto();

            if (!Boolean.TRUE.equals(producto.getActivo())) {
                throw new BadRequestException("El producto '" + producto.getNombre() + "' está inactivo.");
            }

            if (producto.getStock() < item.getCantidad()) {
                throw new BadRequestException("Stock insuficiente para el producto '" + producto.getNombre() + "'.");
            }

            BigDecimal subtotal = producto.getPrecio()
                    .multiply(BigDecimal.valueOf(item.getCantidad()));

            total = total.add(subtotal);
        }

        if (total.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("El total de la venta debe ser mayor a 0.");
        }

        // Crear venta (cabecera)
        Venta venta = Venta.builder()
                .usuario(usuario)
                .total(total)
                .metodoPago(metodoPagoNormalizado)
                .estado("COMPLETADO") // por ahora fijo
                .build();

        Venta ventaGuardada = ventaRepository.save(venta);

        // Crear detalles de venta y descontar stock
        List<DetalleVenta> detalles = new ArrayList<>();

        for (CarritoTemporal item : carrito) {
            Producto producto = item.getProducto();

            DetalleVenta detalle = DetalleVenta.builder()
                    .venta(ventaGuardada)
                    .producto(producto)
                    .cantidad(item.getCantidad())
                    .precioUnitario(producto.getPrecio()) // precio del momento
                    .subtotal(producto.getPrecio().multiply(
                            BigDecimal.valueOf(item.getCantidad())
                    ))
                    .build();

            detalles.add(detalle);

            // Descontar stock
            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);
        }

        List<DetalleVenta> detallesGuardados = detalleVentaRepository.saveAll(detalles);

        // Vaciar carrito
        carritoRepository.deleteByUsuario(usuario);

        return VentaMapper.toVentaResponse(ventaGuardada, detallesGuardados);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> listarMisVentas(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        List<Venta> ventas = ventaRepository.findByUsuario(usuario);

        return ventas.stream()
                .map(venta -> {
                    List<DetalleVenta> detalles = detalleVentaRepository.findByVenta(venta);
                    return VentaMapper.toVentaResponse(venta, detalles);
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> listarTodas() {
        List<Venta> ventas = ventaRepository.findAll();

        return ventas.stream()
                .map(venta -> {
                    List<DetalleVenta> detalles = detalleVentaRepository.findByVenta(venta);
                    return VentaMapper.toVentaResponse(venta, detalles);
                })
                .toList();
    }

    // ===================== helpers =====================

    private String normalizarMetodoPago(String metodoPago) {
        if (metodoPago == null) {
            throw new BadRequestException("El método de pago es obligatorio.");
        }

        String normalizado = metodoPago.trim().toUpperCase();

        if (!METODOS_PAGO_VALIDOS.contains(normalizado)) {
            throw new BadRequestException(
                    "Método de pago no válido. Use uno de: EFECTIVO, TARJETA, YAPE, PLIN."
            );
        }
        return normalizado;
    }
}
