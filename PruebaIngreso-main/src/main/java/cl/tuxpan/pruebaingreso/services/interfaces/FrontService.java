package cl.tuxpan.pruebaingreso.services.interfaces;

import cl.tuxpan.pruebaingreso.dtos.*;

import java.util.List;

public interface FrontService {
  ResItemDto addItem(ReqAddItemDto reqAddItemDto);

  ResGetItemsDto getItems();

  ResItemDto getItem(Integer id);

  ResApuestaDto addApuesta(ReqAddApuestaDto reqAddApuestaDto);

  ResWinnerDto getWinner(Integer id);

  ResTotalUsuarioDto getTotalUsuario(Integer usuarioId);

  List<ResItemSimpleDto> listItemsSimple();

  int triggerCleanup();
}
