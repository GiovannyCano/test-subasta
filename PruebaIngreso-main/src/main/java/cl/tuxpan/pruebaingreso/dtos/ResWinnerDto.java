package cl.tuxpan.pruebaingreso.dtos;

public record ResWinnerDto(
    Integer itemId, String itemName, Integer usuarioId, String usuarioNombre, Integer montoApuesta) {}
