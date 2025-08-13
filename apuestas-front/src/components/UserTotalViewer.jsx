import { useState } from 'react';
import { api } from '../api';

export default function UserTotalViewer() {
  const [usuarioId, setUsuarioId] = useState('');
  const [data, setData] = useState(null);
  const [msg, setMsg] = useState('');

  const fetchTotal = async () => {
    setMsg(''); setData(null);
    try {
      const res = await api.getUserTotal(Number(usuarioId));
      setData(res);
    } catch (err) {
      setMsg(`Error: ${err.message}`);
    }
  };

  return (
    <section className="card">
      <h2>Total apostado por Usuario</h2>
      <div className="row">
        <input type="number" min="1" placeholder="Usuario ID" value={usuarioId}
               onChange={(e) => setUsuarioId(e.target.value)} />
        <button onClick={fetchTotal}>Consultar</button>
      </div>
      {msg && <p className="msg">{msg}</p>}
      {data && (
        <div className="kv">
          <div><b>Usuario:</b> {data.usuarioId} — {data.usuarioNombre}</div>
          <div><b>Total:</b> {data.total}</div>
        </div>
      )}
    </section>
  );
}
