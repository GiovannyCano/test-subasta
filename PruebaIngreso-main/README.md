- # Prueba de Ingreso — Backend + Front

Proyecto de subastas (Spring Boot + PostgreSQL + React).  
Se puede levantar con **Docker Compose** sin depender de la IDE con el comando "docker compose up --build"

> **Nota importante:** No hay **archivo de seed**. Para probar, debes **crear datos manualmente** (por API o SQL). Abajo se incluyen cURLs y ejemplos SQL.

---

## 🧭 Índice

- [Qué se implementó (resumen por partes)](#qué-se-implementó-resumen-por-partes)
- [Requisitos](#requisitos)
- [Estructura del repositorio](#estructura-del-repositorio)
- [Levantamiento con Docker Compose](#levantamiento-con-docker-compose)
- [Cómo cargar datos (no hay seed)](#cómo-cargar-datos-no-hay-seed)
- [Endpoints y ejemplos cURL](#endpoints-y-ejemplos-curl)
- [Tarea programada (cleanup)](#tarea-programada-cleanup)
- [Notas de diseño](#notas-de-diseño)
- [Troubleshooting](#troubleshooting)

---

## Qué se implementó (resumen por partes)

**Parte 1 — Total apostado por usuario**
- Endpoint `GET /api/v1/usuario/{usuarioId}/total`.
- Consulta agregada en `ApuestaRepository` para sumar montos por usuario.

**Parte 2 — Validaciones**
- `ReqAddApuestaDto` con `jakarta.validation`:
    - `usuarioNombre`: 5–50 caracteres.
    - `montoApuesta`: `>= 1000` y `<= 999_999_999`.
- `@Valid` en controller + `@RestControllerAdvice` para errores 400 claros.

**Parte 3 — Ganador por ítem (performance)**
- `findTopByItem_IdOrderByAmountDesc` (derived query, evita `LIMIT` en JPQL).
- Índice SQL `(apuesta_item_id, apuesta_monto DESC)` para acelerar “ganador”.

**Parte 4 — Limpieza periódica**
- Flag `item_abierta` (migración Flyway).
- Job `@Scheduled("0 */2 * * * *")` que elimina **apuestas** (no usuarios) en ítems **abiertos** cuando el **usuario** tiene caracteres no alfanuméricos.
- Endpoint admin opcional `POST /api/v1/_admin/cleanup`.

**Parte 5 — Front (React + Vite)**
- Pantallas mínimas: Crear Ítem, Crear Apuesta, Ver Ganador por Ítem, Ver Total por Usuario.
- Cliente `fetch` simple; validaciones y manejo de errores del backend.

**Parte 6 — Docker Compose (DB + backend + front)**
- Compose levanta **Postgres**, **microservicio** y **front** (Vite dev).  
  *(Bonus: alternativa con Nginx para servir el front ya construído.)*

---

## Requisitos

- Docker + Docker Compose
- (Opcional) `curl`, `jq` para pruebas rápidas
- (Opcional) `psql` para consultar la BD

---

## Estructura del repositorio

```
/ (raíz)
├─ docker-compose.yml
├─ PruebaIngreso-main/          # backend (Spring Boot)
│  ├─ Dockerfile
│  └─ src/...
└─ apuestas-front/              # frontend (Vite + React)
   └─ vite.config.js
```

---

## Levantamiento con Docker Compose

Desde la **raíz** del repo:

```bash
docker compose up --build
```

Servicios expuestos:
- **Postgres:** `localhost:5432` (DB=`mydatabase`, USER=`myuser`, PASS=`secret`)
- **Backend:** `http://localhost:8080`
- **Front (Vite dev):** `http://localhost:5173`

El front usa **proxy** de Vite apuntando al servicio `app` del Compose; en `apuestas-front/src/api.js` el `BASE` es `'/api/v1'`.

---

## Cómo cargar datos (no hay seed)

> No existe archivo de seed. Debes crear datos **manualmente** por API o SQL.

### Opción A — por API (recomendado)

1) **Crear ítems**
```bash
curl -i -X POST http://localhost:8080/api/v1/item   -H "Content-Type: application/json"   -d '{"name":"PlayStation 5"}'

curl -i -X POST http://localhost:8080/api/v1/item   -H "Content-Type: application/json"   -d '{"name":"Nintendo Switch OLED"}'
```

2) **Crear apuestas** (si el usuario no existe, se crea al vuelo)
```bash
curl -s -X POST http://localhost:8080/api/v1/apuesta   -H "Content-Type: application/json"   -d '{"itemId":1,"usuarioNombre":"Alice123","montoApuesta":5000}' | jq
```

### Opción B — por SQL (rápido)

Entrar a `psql`:
```bash
docker compose exec -it postgres psql -U myuser -d mydatabase
```

Crear ítems:
```sql
INSERT INTO subasta_item (item_nombre, item_abierta) VALUES
 ('PlayStation 5', true),
 ('Nintendo Switch OLED', true);
```

---

## Endpoints y ejemplos cURL

> Base URL: `http://localhost:8080/api/v1`

### Listar ítems (solo id y nombre)
```
GET /items
```
```bash
curl -s http://localhost:8080/api/v1/items | jq
```

### Crear ítem
```
POST /item
Body: { "name": "PlayStation 5" }
```
```bash
curl -i -X POST http://localhost:8080/api/v1/item   -H "Content-Type: application/json"   -d '{"name":"PlayStation 5"}'
```

### Crear apuesta (crea usuario si no existe)
```
POST /apuesta
Body: { "itemId": 1, "usuarioNombre": "Alice123", "montoApuesta": 5000 }
```
```bash
curl -s -X POST http://localhost:8080/api/v1/apuesta   -H "Content-Type: application/json"   -d '{"itemId":1,"usuarioNombre":"Alice123","montoApuesta":5000}' | jq
```
- **404** si `itemId` no existe.
- **400** si `usuarioNombre` < 5 o `montoApuesta` < 1000.

### Ganador por ítem
```
GET /winner/{itemId}
```
```bash
curl -s http://localhost:8080/api/v1/winner/1 | jq
```

### Total apostado por usuario
```
GET /usuario/{usuarioId}/total
```
```bash
curl -s http://localhost:8080/api/v1/usuario/1/total | jq
```

### (Admin) Ejecutar limpieza manual
```
POST /_admin/cleanup
```
```bash
curl -s -X POST http://localhost:8080/api/v1/_admin/cleanup | jq
```

---

## Tarea programada (cleanup)

- Se ejecuta cada **2 minutos** (`0 */2 * * * *`).
- Elimina **apuestas** (no usuarios) de ítems **abiertos** cuyo `usuario_nombre` contenga caracteres **no alfanuméricos**.
- Puedes forzarla con `POST /_admin/cleanup`.

---

## Notas de diseño

- **Persistencia**: JPA/Hibernate + Flyway para migraciones.
- **Validaciones**: `jakarta.validation` en DTOs + `@Valid` en controller.
- **Errores**:
    - 400: validaciones de entrada (mensaje con campos).
    - 404: recursos inexistentes (por ejemplo, `itemId`).
    - 409: duplicados (si aplica).
- **Performance**:
    - Índice `(apuesta_item_id, apuesta_monto DESC)` para ganador por ítem.
    - Derived queries; se evita `LIMIT` en JPQL.

---

## Troubleshooting

**FlywayValidateException (migraciones)**
- No editar migraciones ya aplicadas. Agregar nuevas (`V2__...`, `V3__...`).
- En dev, para resetear:
  ```bash
  docker compose down -v
  docker compose up --build
  ```

**404 en `/api/v1/...`**
- Verificar `@RestController` y `@RequestMapping("/api/v1")` (o usar ruta absoluta en el método).
- Evitar duplicar `context-path`: si usas `server.servlet.context-path: /api/v1`, no repitas el prefijo en el controller.

**Front (Vite) no llega al backend**
- `vite.config.js` debe tener proxy `target: 'http://app:8080'` (no `localhost`).
- Reiniciar front: `docker compose restart front`.

**500 en `POST /apuesta`**
- No usar `Optional.get()`; utilizar `orElseThrow(...)` para 404 si `itemId` no existe.
- Confirmar getters correctos en entidades (`getName()` vs `getNombre()`).

---
