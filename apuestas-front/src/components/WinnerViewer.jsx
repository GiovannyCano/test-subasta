import { useState } from 'react';
import { api } from '../api';

export default function WinnerViewer() {
  const [itemId, setItemId] = useState('');
  const [data, setData] = useState(null);
  const [msg, setMsg] = useState('');

  const fetchWinner = async () => {
    setMsg(''); setData(null);
    try {
      const res = await api.getWinner(Number(itemId));
      if (!res) { setMsg('Sin ganador aún'); return; }
      setData(res);
    } catch (err) {
      setMsg(`Error: ${err.message}`);
    }
  };

  return (
    <section className="card">
      <h2>Ganador por Item</h2>
      <div className="row">
        <input type="number" min="1" placeholder="Item ID" value={itemId}
               onChange={(e) => setItemId(e.target.value)} />
        <button onClick={fetchWinner}>Ver ganador</button>
      </div>
      {msg && <p className="msg">{msg}</p>}
      {data && (
        <div className="kv">
          <div><b>Item:</b> {data.itemId} — {data.itemName}</div>
          <div><b>Usuario:</b> {data.usuarioId} — {data.usuarioNombre}</div>
          <div><b>Monto:</b> {data.montoApuesta}</div>
        </div>
      )}
    </section>
  );
}
