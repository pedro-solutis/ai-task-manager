import { useState } from 'react';
import { KanbanColumn } from './KanbanColumn.jsx';
import { TaskDetailModal } from '../tasks/TaskDetailModal.jsx';
import { TaskFormModal } from '../tasks/TaskFormModal.jsx';
import { TASK_STATUS } from '../../utils/constants.js';

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
        {Object.entries(TASK_STATUS).map(([statusKey, config]) => (
          <KanbanColumn 
            key={statusKey}
            title={config.label} 
            count={mockTasks[statusKey]?.length || 0}
            badgeClass={config.colorClass}
            tasks={mockTasks[statusKey] || []}
            onTaskClick={setSelectedTask}
          />
        ))}
      </div>

      {selectedTask && (
        <TaskDetailModal 
          task={selectedTask} 
          onClose={() => setSelectedTask(null)} 
          onEdit={handleEdit}
          onDelete={handleDelete}
          onUpdateTask={(updates) => setSelectedTask(prev => ({ ...prev, ...updates }))}
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

