¡Hola! Gracias por revisar mi solución al desafío técnico. La desarrollé en mis tiempos libres, después del trabajo y la familia.

La solución está dividida en **backend** (`PruebaIngreso-main`) y **frontend** (`apuestas-front`). Cada proyecto incluye un README con instrucciones detalladas para levantar el entorno (incluye Docker Compose), endpoints y decisiones técnicas.

> Nota: no se incluye archivo de *seed*; los datos de prueba deben cargarse manualmente.

¡Quedo atento a sus comentarios y sugerencias!


## Requisitos

Necesitas **Docker** y **Docker Compose v2** instalados:

- **Windows (Docker Desktop)**: https://docs.docker.com/desktop/setup/install/windows-install/
- **macOS (Docker Desktop)**: https://docs.docker.com/desktop/setup/install/mac-install/
- **Linux (Docker Engine)**: https://docs.docker.com/engine/install/
- **Linux (Compose plugin)**: https://docs.docker.com/compose/install/linux/

> Verifica la instalación:
>
> ```bash
> docker --version
> docker compose version
> ```

## Estructura del repo

- **backend**: `PruebaIngreso-main` (Spring Boot)
- **frontend**: `apuestas-front` (Vite/React)
- **docker-compose** en la raíz para levantar todo junto.

## Cómo correr

Desde la **raíz del repo**:



```bash
# 1) Construir e iniciar servicios
docker compose up --build

# (opcional) levantar en segundo plano
# docker compose up -d --build
```
# si usas el front dev:
open http://localhost:5173
