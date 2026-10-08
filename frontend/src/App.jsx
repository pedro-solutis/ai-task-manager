import { useState } from 'react';
import './App.css';
import { Layout } from './components/layout/Layout.jsx';
import { KanbanBoard } from './components/kanban/KanbanBoard.jsx';
import { TaskFormModal } from './components/tasks/TaskFormModal.jsx';

function App() {
  const [isTaskModalOpen, setIsTaskModalOpen] = useState(false);
  const [refreshTick, setRefreshTick] = useState(0);

  return (
    <>
      <Layout onNewTaskClick={() => setIsTaskModalOpen(true)}>
        <KanbanBoard refreshTick={refreshTick} />
      </Layout>
      <TaskFormModal 
        isOpen={isTaskModalOpen} 
        onClose={() => setIsTaskModalOpen(false)}
        onSaved={() => setRefreshTick(prev => prev + 1)}
      />
    </>
  );
}

export default App;
