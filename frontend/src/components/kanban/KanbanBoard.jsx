import { KanbanColumn } from './KanbanColumn.jsx';

export function KanbanBoard() {
  const mockTasks = {
    TODO: [
      { id: 1, title: 'Criar estrutura do Kanban', description: 'Implementar layout base' }
    ],
    IN_PROGRESS: [],
    DONE: []
  };

  return (
    <div className="flex justify-center gap-6 h-full min-h-[500px]">
      <KanbanColumn 
        title="A Fazer" 
        count={mockTasks.TODO.length}
        badgeClass="bg-gray-200 dark:bg-slate-700 text-gray-700 dark:text-gray-300"
        tasks={mockTasks.TODO}
      />
      <KanbanColumn 
        title="Em Andamento" 
        count={mockTasks.IN_PROGRESS.length}
        badgeClass="bg-blue-100 text-blue-800 dark:bg-blue-900/30 dark:text-blue-300"
        tasks={mockTasks.IN_PROGRESS}
      />
      <KanbanColumn 
        title="Concluído" 
        count={mockTasks.DONE.length}
        badgeClass="bg-green-100 text-green-800 dark:bg-green-900/30 dark:text-green-300"
        tasks={mockTasks.DONE}
      />
    </div>
  );
}

