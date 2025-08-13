package cl.tuxpan.pruebaingreso.dtos;

import java.util.List;

public record ResItemDto(Integer id, String name, List<Integer> idApuestas) {}
