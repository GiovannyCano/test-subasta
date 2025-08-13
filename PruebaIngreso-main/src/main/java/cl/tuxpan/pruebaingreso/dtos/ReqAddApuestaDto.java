package cl.tuxpan.pruebaingreso.dtos;

import jakarta.validation.constraints.*;

public record ReqAddApuestaDto(
  @NotBlank
  @Size(min = 5, max = 50)
  String usuarioNombre,

  @NotNull
  Integer itemId,

  @NotNull
  @Min(1000)
  @Max(999_999_999)
  Integer montoApuesta
) {}
