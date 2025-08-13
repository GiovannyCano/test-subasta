import { useState } from 'react';
import { api } from '../api';

export default function BetManager() {
  const [itemId, setItemId] = useState('');
  const [usuarioNombre, setUsuarioNombre] = useState('');
  const [montoApuesta, setMontoApuesta] = useState('');
  const [msg, setMsg] = useState('');
  const [resp, setResp] = useState(null);
  const disabled =
    !itemId || Number(itemId) <= 0 ||
    !usuarioNombre || usuarioNombre.trim().length < 5 ||
    !montoApuesta || Number(montoApuesta) < 1000;

  const submit = async (e) => {
    e.preventDefault();
    setMsg(''); setResp(null);
    try {
      const payload = {
        itemId: Number(itemId),
        usuarioNombre: usuarioNombre.trim(),
        montoApuesta: Number(montoApuesta),
      };
      const r = await api.createBet(payload);
      setResp(r);
      setMsg('✅ Apuesta creada');
      setItemId(''); setUsuarioNombre(''); setMontoApuesta('');
    } catch (err) {
        setMsg('❌ El item indicado no existe.');
    }
  };

  return (
    <section className="card">
      <h2>Crear Apuesta</h2>
      <form onSubmit={submit} className="col">
        <div className="row">
          <input type="number" min="1" placeholder="Item ID"
                 value={itemId} onChange={(e)=>setItemId(e.target.value)} />
          <input placeholder="Usuario (≥5 caracteres)"
                 value={usuarioNombre} onChange={(e)=>setUsuarioNombre(e.target.value)} />
          <input type="number" min="1000" placeholder="Monto (≥1000)"
                 value={montoApuesta} onChange={(e)=>setMontoApuesta(e.target.value)} />
          <button type="submit" disabled={disabled}>Apostar</button>
        </div>
      </form>
      {msg && <p className="msg">{msg}</p>}
      {resp && <pre className="pre">{JSON.stringify(resp, null, 2)}</pre>}
    </section>
  );
}
