import ItemManager from './components/ItemManager';
import BetManager from './components/BetManager';
import WinnerViewer from './components/WinnerViewer';
import UserTotalViewer from './components/UserTotalViewer';
import './app.css';

export default function App() {
  return (
    <div className="container">
      <header>
        <h1>Subastas — Demo Front</h1>
        <p>API: {import.meta.env.VITE_API_BASE}</p>
      </header>

      <main className="grid">
        <ItemManager />
        <BetManager />
        <WinnerViewer />
        <UserTotalViewer />
      </main>
    </div>
  );
}
