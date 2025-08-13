package cl.tuxpan.pruebaingreso.services;

import cl.tuxpan.pruebaingreso.dtos.*;
import cl.tuxpan.pruebaingreso.models.ItemModel;
import cl.tuxpan.pruebaingreso.models.ApuestaModel;
import cl.tuxpan.pruebaingreso.models.UsuarioModel;
import cl.tuxpan.pruebaingreso.repositories.ItemRepository;
import cl.tuxpan.pruebaingreso.repositories.ApuestaRepository;
import cl.tuxpan.pruebaingreso.repositories.UsuarioRepository;
import cl.tuxpan.pruebaingreso.services.interfaces.FrontService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FrontServiceImpl implements FrontService {

  ItemRepository itemRepository;
  ApuestaRepository apuestaRepository;
  UsuarioRepository usuarioRepository;

  FrontServiceImpl(
    ItemRepository itemRepository,
    ApuestaRepository apuestaRepository,
    UsuarioRepository usuarioRepository) {
    this.itemRepository = itemRepository;
    this.apuestaRepository = apuestaRepository;
    this.usuarioRepository = usuarioRepository;
  }

  @Override
  public ResItemDto addItem(ReqAddItemDto reqAddItemDto) {
    log.warn("addItem: {}", reqAddItemDto);
    ItemModel item =
      itemRepository.save(new ItemModel(null, reqAddItemDto.name(),true, Collections.emptyList()));
    return new ResItemDto(item.getId(), item.getName(), Collections.emptyList());
  }

  @Override
  public ResGetItemsDto getItems() {
    return new ResGetItemsDto(
      itemRepository.findAll().stream()
        .map(
          itemModel ->
            new ResItemDto(
              itemModel.getId(),
              itemModel.getName(),
              itemModel.getApuestas().stream().map(ApuestaModel::getId).toList()))
        .toList());
  }

  @Override
  public ResItemDto getItem(Integer id) {
    return itemRepository
      .findById(id)
      .map(
        itemModel ->
          new ResItemDto(
            itemModel.getId(),
            itemModel.getName(),
            itemModel.getApuestas().stream().map(ApuestaModel::getId).toList()))
      .orElse(null);
  }

  @Override
  public ResApuestaDto addApuesta(ReqAddApuestaDto reqAddApuestaDto) {
    Optional<ItemModel> itemModel = itemRepository.findById(reqAddApuestaDto.itemId());
    ItemModel item = itemModel.orElseThrow();

    Optional<UsuarioModel> usuarioModel =
      usuarioRepository.findByName(reqAddApuestaDto.usuarioNombre());

    UsuarioModel usuario =
      usuarioModel.orElseGet(
        () ->
          usuarioRepository.save(
            new UsuarioModel(null, reqAddApuestaDto.usuarioNombre(), new ArrayList<>())));

    ApuestaModel apuesta =
      apuestaRepository.save(new ApuestaModel(null, reqAddApuestaDto.montoApuesta(), usuario, item));

    return new ResApuestaDto(apuesta.getId(), apuesta.getAmount());
  }

  @Override
  public ResWinnerDto getWinner(Integer id) {
    return apuestaRepository.findTopByItem_IdOrderByAmountDesc(id)
      .map(apuesta -> new ResWinnerDto(
        apuesta.getItem().getId(),
        apuesta.getItem().getName(),
        apuesta.getUsuario().getId(),
        apuesta.getUsuario().getName(),
        apuesta.getAmount()
      ))
      .orElse(null);
  }

  @Override
  @Transactional()
  public ResTotalUsuarioDto getTotalUsuario(Integer usuarioId) {
    var usuario = usuarioRepository.findById(usuarioId)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

    Integer total = apuestaRepository.totalApostadoPorUsuario(usuarioId);

    // ajusta getNombre() a getName() si tu entidad usa "name" en vez de "nombre"
    return new ResTotalUsuarioDto(usuario.getId(), usuario.getName(), total);
  }

  @Override
  @Transactional
  public int triggerCleanup() {
    return apuestaRepository.purgeApuestasCorruptas();
  }

  @Override
  @Transactional()
  public List<ResItemSimpleDto> listItemsSimple() {
    return itemRepository.findAll(Sort.by(Sort.Order.asc("id"))).stream()
      .map(i -> new ResItemSimpleDto(i.getId(), i.getName()))
      .toList();
  }


}
