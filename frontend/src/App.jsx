import './App.css';
import { Layout } from './components/layout/Layout.jsx';
import { KanbanBoard } from './components/kanban/KanbanBoard.jsx';

function App() {
  return (
    <Layout>
      <KanbanBoard />
    </Layout>
  );
}

export default App;
