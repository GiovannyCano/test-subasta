import { useState } from 'react';
import { api } from '../api';

export default function ItemManager() {
  const [name, setName] = useState('');
  const [items, setItems] = useState([]);
  const [msg, setMsg] = useState('');

  const create = async (e) => {
    e.preventDefault();
    setMsg('');
    try {
      if (name.trim().length < 3) throw new Error('El nombre debe tener al menos 3 caracteres');
      const res = await api.createItem(name.trim());
      setMsg(`Item creado (id=${res.id ?? '??'}, name=${res.name ?? name})`);
      setName('');
    } catch (err) {
      setMsg(`Error: ${err.message}`);
    }
  };

  const load = async () => {
    setMsg('');
    try {
      const data = await api.listItems();
      setItems(Array.isArray(data) ? data : []);
      if (!Array.isArray(data)) setMsg('El endpoint /items no está disponible');
    } catch (err) {
      setMsg(`Error al cargar items: ${err.message}`);
    }
  };

  return (
    <section className="card">
      <h2>Items</h2>
      <form onSubmit={create} className="row">
        <input
          placeholder="Nombre del item (p. ej. PlayStation 5)"
          value={name}
          onChange={(e) => setName(e.target.value)}
        />
        <button type="submit">Crear Item</button>
      </form>

      <div className="row">
        <button onClick={load}>Cargar Items</button>
      </div>

      {msg && <p className="msg">{msg}</p>}

      {items.length > 0 && (
        <table>
          <thead>
            <tr><th>ID</th><th>Nombre</th></tr>
          </thead>
          <tbody>
            {items.map((it) => (
              <tr key={it.id}><td>{it.id}</td><td>{it.name ?? it.itemNombre ?? it.nombre}</td></tr>
            ))}
          </tbody>
        </table>
      )}
    </section>
  );
}
