# Apuestas Front — React + Vite

Interfaz mínima para el proyecto de subastas. Permite:
- Crear **Ítem**
- Crear **Apuesta** (crea usuario al vuelo si no existe)
- Ver **Ganador por Ítem**
- Ver **Total apostado por Usuario**

> **Nota:** No hay seed en el backend; primero **crea ítems** para poder apostar.

---

## 🧩 Stack
- **React 18** + **Vite**
- Fetch nativo (sin axios)
- Estilos simples (CSS plano)
- Proxy de Vite para evitar CORS en dev

---

## 📁 Estructura (resumen)

```
apuestas-front/
├─ src/
│  ├─ api.js                 # cliente API
│  ├─ App.jsx
│  ├─ app.css
│  └─ components/
│     ├─ ItemManager.jsx     # crear ítem + listar (si el backend expone /items)
│     ├─ BetManager.jsx      # crear apuesta
│     ├─ WinnerViewer.jsx    # ver ganador por ítem
│     └─ UserTotalViewer.jsx # ver total por usuario
├─ index.html
├─ package.json
└─ vite.config.js
```

---

## 🚀 Quick start (local)

```bash
# desde la carpeta apuestas-front/
npm ci
npm run dev
# abre http://localhost:5173
```

Si el backend corre de forma independiente en `http://localhost:8080`, puedes **NO** usar proxy y setear la base con `.env`:

```
VITE_API_BASE=http://localhost:8080/api/v1
```

y en `src/api.js`:

```js
const BASE = import.meta.env.VITE_API_BASE || '/api/v1';
```

---

## 🐳 Con Docker Compose (recomendado en este repo)

El `docker-compose.yml` de la raíz ya trae un servicio `front` que arranca Vite y un servicio `app` para el backend.

- **Proxy Vite** (`apuestas-front/vite.config.js`):

```js
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api/v1': {
        target: 'http://app:8080',  // nombre del servicio del backend en docker-compose
        changeOrigin: true,
      },
    },
  },
})
```

- **Cliente API** (`src/api.js`):

```js
const BASE = '/api/v1'; // con proxy; NO uses http://localhost:8080 aquí
```

Levantar todo desde la raíz del repo:
```bash
docker compose up --build
# Front:  http://localhost:5173
# API:    http://localhost:8080/api/v1
```

---

## 🛠️ Scripts útiles

```bash
npm run dev       # desarrollo
npm run build     # build de producción (carpeta dist)
npm run preview   # sirve dist/ localmente
```

---

## 🔌 Endpoints que usa el front

- `GET  /api/v1/items`                 → listar ítems (id, name)
- `POST /api/v1/item`                  → crear ítem
- `POST /api/v1/apuesta`               → crear apuesta ({ itemId, usuarioNombre, montoApuesta })
- `GET  /api/v1/winner/{itemId}`       → ver ganador por ítem
- `GET  /api/v1/usuario/{usuarioId}/total` → total apostado por usuario

> Si algún endpoint no está disponible, el front lo indicará con un mensaje de error.

---

## 🧪 Flujo de prueba sugerido

1) Crear uno o más **Ítems** desde la UI (o por cURL).  
2) Crear una **Apuesta** con un `itemId` existente (mínimo 1000).  
3) Consultar **Ganador** de ese `itemId`.  
4) Si conoces el `usuarioId`, ver **Total** apostado por ese usuario.

---

## 🧯 Troubleshooting

**`[vite] http proxy error: ECONNREFUSED 127.0.0.1:8080`**  
- Dentro del contenedor de Vite, `localhost` no es tu host. El `target` del proxy debe ser `http://app:8080` (nombre del servicio), no `http://localhost:8080`.
- Reinicia el front tras cambiar `vite.config.js`: `docker compose restart front`.

**CORS en dev**  
- Si llamas directo a `http://localhost:8080` desde el navegador (sin proxy), el backend debe permitir CORS. Lo evitamos usando el proxy de Vite y `BASE='/api/v1'`.

**404 al llamar a `/api/v1/...`**  
- Verifica que el backend tenga `@RestController` y el mapping correcto (ya sea `@RequestMapping("/api/v1")` en clase o rutas absolutas en métodos).
- Comprueba la pestaña *Network* del navegador: la URL debe ser `http://localhost:5173/api/v1/...` (Vite la proxeará).

**500 en crear apuesta**  
- Suele ser por `itemId` inexistente o validaciones. El backend retorna 404 si el ítem no existe y 400 si las reglas no se cumplen. El front muestra el mensaje.

---

## 📦 Build “prod” con Nginx (opcional)

Si quieres empaquetar el front en un contenedor estático:

`apuestas-front/Dockerfile`:
```dockerfile
FROM node:20 AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

FROM nginx:1.27-alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80
```

`apuestas-front/nginx.conf`:
```nginx
server {
  listen 80;
  root /usr/share/nginx/html;
  index index.html;

  location /api/v1/ {
    proxy_pass http://app:8080/api/v1/;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
  }

  location / {
    try_files $uri /index.html;
  }
}
```

En producción, el cliente puede dejar `const BASE = ''` y llamar directamente a `/api/v1/...` (Nginx hace el proxy).

---

## ✅ Checklist rápido

- [ ] Backend arriba en `:8080` (o vía Compose).
- [ ] `vite.config.js` con proxy a `http://app:8080` cuando se usa Compose.
- [ ] `src/api.js` con `BASE='/api/v1'` (proxy) **o** `VITE_API_BASE` si no hay proxy.
- [ ] Crear ítems antes de crear apuestas.
- [ ] Revisar *Network* en caso de error (status, payload).

---

¡Listo! Este front está pensado para validar el backend de forma rápida y visual. Cualquier ajuste de endpoints o layout es sencillo de extender en `src/components/`.
