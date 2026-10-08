import { useState } from 'react';
import { KanbanColumn } from './KanbanColumn.jsx';
import { TaskDetailModal } from '../tasks/TaskDetailModal.jsx';
import { TaskFormModal } from '../tasks/TaskFormModal.jsx';

export function KanbanBoard() {
  const [selectedTask, setSelectedTask] = useState(null);
  const [taskToEdit, setTaskToEdit] = useState(null);
  
  const mockTasks = {
    TODO: [
      { id: 1, title: 'Criar estrutura do Kanban', description: 'Implementar layout base', priority: 'HIGH', dueDate: '2026-10-10T12:00:00Z', status: 'TODO' }
    ],
    IN_PROGRESS: [],
    DONE: []
  };

  const handleEdit = (task) => {
    setSelectedTask(null);
    setTaskToEdit(task);
  };

  const handleDelete = (taskId) => {
    alert(`[MOCK] Tarefa ${taskId} excluída com sucesso!`);
    setSelectedTask(null);
  };

  return (
    <>
      <div className="flex justify-center gap-6 h-full min-h-[500px]">
        <KanbanColumn 
          title="A Fazer" 
          count={mockTasks.TODO.length}
          badgeClass="bg-gray-200 dark:bg-slate-700 text-gray-700 dark:text-gray-300"
          tasks={mockTasks.TODO}
          onTaskClick={setSelectedTask}
        />
        <KanbanColumn 
          title="Em Andamento" 
          count={mockTasks.IN_PROGRESS.length}
          badgeClass="bg-blue-100 text-blue-800 dark:bg-blue-900/30 dark:text-blue-300"
          tasks={mockTasks.IN_PROGRESS}
          onTaskClick={setSelectedTask}
        />
        <KanbanColumn 
          title="Concluído" 
          count={mockTasks.DONE.length}
          badgeClass="bg-green-100 text-green-800 dark:bg-green-900/30 dark:text-green-300"
          tasks={mockTasks.DONE}
          onTaskClick={setSelectedTask}
        />
      </div>

      {selectedTask && (
        <TaskDetailModal 
          task={selectedTask} 
          onClose={() => setSelectedTask(null)} 
          onEdit={handleEdit}
          onDelete={handleDelete}
        />
      )}

      <TaskFormModal 
        isOpen={!!taskToEdit}
        initialData={taskToEdit}
        onClose={() => setTaskToEdit(null)}
        onSaved={() => {
          console.log('[MOCK] Tarefa atualizada!');
          setTaskToEdit(null);
        }}
      />
    </>
  );
}

