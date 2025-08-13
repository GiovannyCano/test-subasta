package cl.tuxpan.pruebaingreso.controllers;

import cl.tuxpan.pruebaingreso.dtos.*;
import cl.tuxpan.pruebaingreso.services.interfaces.FrontService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para operaciones de ítems, apuestas y totales por usuario.
 *
 * <p><strong>Prefijo</strong> de todas las rutas: {@code /api/v1}</p>
 *
 * <h2>Resumen de endpoints</h2>
 * <ul>
 *   <li>POST {@code /item} — crea un nuevo ítem.</li>
 *   <li>GET  {@code /item/{id}} — obtiene un ítem por su identificador.</li>
 *   <li>GET  {@code /item} — lista de ítems con detalle.</li>
 *   <li>POST {@code /apuesta} — registra una apuesta.</li>
 *   <li>GET  {@code /winner/{itemId}} — ganador para el ítem indicado.</li>
 *   <li>GET  {@code /usuario/{usuarioId}/total} — total del usuario.</li>
 *   <li>POST {@code /_admin/cleanup} — limpieza administrativa de datos inválidos.</li>
 *   <li>GET  {@code /items} — lista de ítems en formato simple.</li>
 * </ul>
 *
 * <p>Las validaciones de entrada se realizan a nivel de DTO con {@code jakarta.validation} y,
 * ante errores de negocio o ausencia de recursos, el servicio puede lanzar
 * {@link org.springframework.web.server.ResponseStatusException} para mapear códigos HTTP adecuados.</p>
 *
 * @author Giovanny Cano Leal
 * @since 1.0
 */
@RestController
@RequestMapping("/api/v1")
public class FrontController {

  /** Fachada de reglas de negocio para el front. */
  FrontService frontService;

  /**
   * Constructor con inyección de dependencias.
   *
   * @param frontService servicio que implementa la lógica del dominio expuesta a este controlador
   */
  FrontController(FrontService frontService) {
    this.frontService = frontService;
  }

  /**
   * Crea un nuevo ítem.
   *
   * <p><strong>Ejemplo (cURL)</strong>:</p>
   * <pre>{@code
   * curl -X POST http://localhost:8080/api/v1/item \
   *   -H "Content-Type: application/json" \
   *   -d '{"nombre":"Televisor","descripcion":"55 4K","precio":399990}'
   * }</pre>
   *
   * @param reqAddItemDto datos del ítem a crear
   * @return el ítem creado
   * @throws org.springframework.web.server.ResponseStatusException si los datos son inválidos o hay conflicto
   */
  @PostMapping("/item")
  public ResItemDto addItem(@RequestBody ReqAddItemDto reqAddItemDto) {
    return frontService.addItem(reqAddItemDto);
  }

  /**
   * Obtiene un ítem por su identificador.
   *
   * @param id identificador del ítem (path variable)
   * @return el ítem encontrado
   * @throws org.springframework.web.server.ResponseStatusException si no existe el ítem
   */
  @GetMapping("/item/{id}")
  public ResItemDto getItem(@PathVariable Integer id) {
    return frontService.getItem(id);
  }

  /**
   * Obtiene el listado de ítems (vista detallada).
   *
   * @return contenedor con la colección de ítems y/o metadatos asociados
   */
  @GetMapping("/item")
  public ResGetItemsDto getItems() {
    return frontService.getItems();
  }

  /**
   * Registra una apuesta.
   *
   * <p>El cuerpo se valida con {@link jakarta.validation.Valid} según las
   * restricciones declaradas en {@link ReqAddApuestaDto}.</p>
   *
   * <p><strong>Ejemplo (cURL)</strong>:</p>
   * <pre>{@code
   * curl -X POST http://localhost:8080/api/v1/apuesta \
   *   -H "Content-Type: application/json" \
   *   -d '{"usuarioId":1,"itemId":10,"monto":5000}'
   * }</pre>
   *
   * @param body datos de la apuesta
   * @return la apuesta registrada
   * @throws org.springframework.web.server.ResponseStatusException si el usuario o ítem no existen,
   *         o si la apuesta no cumple las reglas de negocio
   */
  @PostMapping(value = "/apuesta", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResApuestaDto addApuesta(@Valid @RequestBody ReqAddApuestaDto body) {
    return frontService.addApuesta(body);
  }

  /**
   * Obtiene el ganador para un ítem determinado.
   *
   * @param itemId identificador del ítem
   * @return DTO con información del ganador
   * @throws org.springframework.web.server.ResponseStatusException si no existe el ítem o aún no hay ganador
   */
  @GetMapping("/winner/{itemId}")
  public ResWinnerDto getWinner(@PathVariable Integer itemId) {
    return frontService.getWinner(itemId);
  }

  /**
   * Obtiene el total acumulado para un usuario.
   *
   * @param usuarioId identificador del usuario
   * @return DTO con el total asociado al usuario
   * @throws org.springframework.web.server.ResponseStatusException si el usuario no existe
   */
  @GetMapping(value = "/usuario/{usuarioId}/total", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResTotalUsuarioDto getTotalUsuario(@PathVariable Integer usuarioId) {
    return frontService.getTotalUsuario(usuarioId);
  }

  /**
   * Ejecuta una limpieza administrativa de datos inválidos o temporales.
   *
   * <p><strong>Nota:</strong> endpoint administrativo; úsese con precaución.</p>
   *
   * <p><strong>Ejemplo (cURL)</strong>:</p>
   * <pre>{@code
   * curl -X POST http://localhost:8080/api/v1/_admin/cleanup
   * }</pre>
   *
   * @return mapa con el conteo de filas eliminadas bajo la clave {@code "deleted"}
   */
  @PostMapping("/_admin/cleanup")
  public Map<String, Object> triggerCleanup() {
    int rows = frontService.triggerCleanup();
    return Map.of("deleted", rows);
  }

  /**
   * Lista los ítems en formato simple (ligero).
   *
   * @return arreglo de ítems simplificados
   */
  @GetMapping(value = "/items", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<ResItemSimpleDto> listItems() {
    return frontService.listItemsSimple();
  }
}
