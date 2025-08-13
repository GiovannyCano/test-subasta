const BASE = '/api/v1';

async function req(path, options = {}) {
  const res = await fetch(`${BASE}${path}`, {
    headers: { 'Content-Type': 'application/json', ...(options.headers || {}) },
    ...options,
  });
  const text = await res.text();
  let data; try { data = text ? JSON.parse(text) : null; } catch { data = text; }
  if (!res.ok) throw new Error(`${res.status} ${data?.error || data?.message || res.statusText}`);
  return data;
}

export const api = {
  listItems: () => req('/items'),                
  createItem: (name) => req('/item', { method: 'POST', body: JSON.stringify({ name }) }),

  createBet: ({ itemId, usuarioNombre, montoApuesta }) =>
    req('/apuesta', { method: 'POST', body: JSON.stringify({ itemId, usuarioNombre, montoApuesta }) }),

  getWinner: (itemId) => req(`/winner/${itemId}`),

  getUserTotal: (usuarioId) => req(`/usuario/${usuarioId}/total`),
};
