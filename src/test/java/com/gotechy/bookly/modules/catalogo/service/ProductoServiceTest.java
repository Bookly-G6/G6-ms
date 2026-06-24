package com.gotechy.bookly.modules.catalogo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.gotechy.bookly.modules.accesos.repository.PersonaRepository;
import com.gotechy.bookly.modules.accesos.repository.UsuarioRepository;
import com.gotechy.bookly.modules.catalogo.dto.ProductoResponseDTO;
import com.gotechy.bookly.modules.catalogo.model.AutorArtista;
import com.gotechy.bookly.modules.catalogo.model.Categoria;
import com.gotechy.bookly.modules.catalogo.model.EditorialSello;
import com.gotechy.bookly.modules.catalogo.model.Producto;
import com.gotechy.bookly.modules.catalogo.model.RangoEtario;
import com.gotechy.bookly.modules.catalogo.model.TipoProducto;
import com.gotechy.bookly.modules.catalogo.repository.AutorArtistaRepository;
import com.gotechy.bookly.modules.catalogo.repository.CategoriaRepository;
import com.gotechy.bookly.modules.catalogo.repository.EditorialSelloRepository;
import com.gotechy.bookly.modules.catalogo.repository.HistorialPrecioRepository;
import com.gotechy.bookly.modules.catalogo.repository.ProductoRepository;
import com.gotechy.bookly.modules.catalogo.repository.RangoEtarioRepository;
import com.gotechy.bookly.modules.catalogo.repository.TipoProductoRepository;
import com.gotechy.bookly.modules.ventas.repository.EmpleadoRepository;
import com.gotechy.bookly.modules.ventas.repository.MovimientoStockRepository;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

class ProductoServiceTest {

    @Test
    void listarSegunRolAdminIncluyeProductosInactivos() {
        ProductoRepository productoRepository = mock(ProductoRepository.class);
        TipoProductoRepository tipoProductoRepository = mock(
            TipoProductoRepository.class
        );
        EditorialSelloRepository editorialSelloRepository = mock(
            EditorialSelloRepository.class
        );
        RangoEtarioRepository rangoEtarioRepository = mock(
            RangoEtarioRepository.class
        );
        CategoriaRepository categoriaRepository = mock(
            CategoriaRepository.class
        );
        AutorArtistaRepository autorArtistaRepository = mock(
            AutorArtistaRepository.class
        );

        HistorialPrecioRepository historialPrecioRepository = mock(
            HistorialPrecioRepository.class
        );
        UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
        EmpleadoRepository empleadoRepository = mock(EmpleadoRepository.class);
        PersonaRepository personaRepository = mock(PersonaRepository.class);
        MovimientoStockRepository movimientoStockRepository = mock(
            MovimientoStockRepository.class
        );

        ProductoService productoService = new ProductoService(
            productoRepository,
            tipoProductoRepository,
            editorialSelloRepository,
            rangoEtarioRepository,
            categoriaRepository,
            autorArtistaRepository,
            historialPrecioRepository,
            usuarioRepository,
            empleadoRepository,
            personaRepository,
            movimientoStockRepository
        );

        Producto productoActivo = crearProducto(true);
        Producto productoInactivo = crearProducto(false);
        when(productoRepository.findAll()).thenReturn(
            List.of(productoActivo, productoInactivo)
        );

        Authentication authentication = mock(Authentication.class);
        Collection<GrantedAuthority> authorities = List.of(
            new SimpleGrantedAuthority("ROLE_ADMIN")
        );
        when(authentication.getAuthorities()).thenReturn(
            (Collection) authorities
        );

        List<ProductoResponseDTO> productos = productoService.listarSegunRol(
            authentication
        );

        assertEquals(2, productos.size());
    }

    @Test
    void listarSegunRolClienteSoloIncluyeProductosActivos() {
        ProductoRepository productoRepository = mock(ProductoRepository.class);
        TipoProductoRepository tipoProductoRepository = mock(
            TipoProductoRepository.class
        );
        EditorialSelloRepository editorialSelloRepository = mock(
            EditorialSelloRepository.class
        );
        RangoEtarioRepository rangoEtarioRepository = mock(
            RangoEtarioRepository.class
        );
        CategoriaRepository categoriaRepository = mock(
            CategoriaRepository.class
        );
        AutorArtistaRepository autorArtistaRepository = mock(
            AutorArtistaRepository.class
        );
        HistorialPrecioRepository historialPrecioRepository = mock(
            HistorialPrecioRepository.class
        );

        UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);

        EmpleadoRepository empleadoRepository = mock(EmpleadoRepository.class);

        PersonaRepository personaRepository = mock(PersonaRepository.class);

        MovimientoStockRepository movimientoStockRepository = mock(
            MovimientoStockRepository.class
        );

        ProductoService productoService = new ProductoService(
            productoRepository,
            tipoProductoRepository,
            editorialSelloRepository,
            rangoEtarioRepository,
            categoriaRepository,
            autorArtistaRepository,
            historialPrecioRepository,
            usuarioRepository,
            empleadoRepository,
            personaRepository,
            movimientoStockRepository
        );

        Producto productoActivo = crearProducto(true);
        Producto productoInactivo = crearProducto(false);
        when(productoRepository.findByActivoTrue()).thenReturn(
            List.of(productoActivo)
        );

        Authentication authentication = mock(Authentication.class);
        Collection<GrantedAuthority> authorities = List.of(
            new SimpleGrantedAuthority("ROLE_CLIENTE")
        );
        when(authentication.getAuthorities()).thenReturn(
            (Collection) authorities
        );

        List<ProductoResponseDTO> productos = productoService.listarSegunRol(
            authentication
        );

        assertEquals(1, productos.size());
        assertEquals(
            productoActivo.getIdProducto(),
            productos.get(0).getIdProducto()
        );
    }

    @Test
    void mapearAResponseDTOExponeStockDelProducto() {
        ProductoRepository productoRepository = mock(ProductoRepository.class);
        TipoProductoRepository tipoProductoRepository = mock(
            TipoProductoRepository.class
        );
        EditorialSelloRepository editorialSelloRepository = mock(
            EditorialSelloRepository.class
        );
        RangoEtarioRepository rangoEtarioRepository = mock(
            RangoEtarioRepository.class
        );
        CategoriaRepository categoriaRepository = mock(
            CategoriaRepository.class
        );
        AutorArtistaRepository autorArtistaRepository = mock(
            AutorArtistaRepository.class
        );
        HistorialPrecioRepository historialPrecioRepository = mock(
            HistorialPrecioRepository.class
        );
        UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
        EmpleadoRepository empleadoRepository = mock(EmpleadoRepository.class);
        PersonaRepository personaRepository = mock(PersonaRepository.class);
        MovimientoStockRepository movimientoStockRepository = mock(
            MovimientoStockRepository.class
        );

        ProductoService productoService = new ProductoService(
            productoRepository,
            tipoProductoRepository,
            editorialSelloRepository,
            rangoEtarioRepository,
            categoriaRepository,
            autorArtistaRepository,
            historialPrecioRepository,
            usuarioRepository,
            empleadoRepository,
            personaRepository,
            movimientoStockRepository
        );

        Producto producto = crearProducto(true);
        producto.setStock(7);

        var response = productoService.mapearAResponseDTO(producto);

        assertEquals(7, response.getStock());
    }

    private Producto crearProducto(boolean activo) {
        Producto producto = new Producto();
        producto.setIdProducto(UUID.randomUUID());
        producto.setCodigoBarras("12345678");
        producto.setNombreProducto(activo ? "Clean Code" : "Old Book");
        producto.setDescripcion("Libro de referencia");
        producto.setPrecioCosto(BigDecimal.TEN);
        producto.setPrecioActual(BigDecimal.valueOf(25));
        producto.setActivo(activo);

        TipoProducto tipoProducto = new TipoProducto();
        tipoProducto.setNombreTipoProducto("Libro Físico");
        producto.setTipoProducto(tipoProducto);

        EditorialSello editorial = new EditorialSello();
        editorial.setNombreEditorial("O'Reilly");
        producto.setEditorialSello(editorial);

        RangoEtario rango = new RangoEtario();
        rango.setDescripcion("Adultos");
        producto.setRangoEtario(rango);

        Categoria categoria = new Categoria();
        categoria.setNombreCategoria("Programación");
        producto.setCategorias(List.of(categoria));

        AutorArtista autor = new AutorArtista();
        autor.setNombre("Robert C. Martin");
        producto.setAutores(List.of(autor));

        return producto;
    }
}
