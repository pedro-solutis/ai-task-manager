import { useState } from 'react';
import { KanbanColumn } from './KanbanColumn.jsx';
import { TaskDetailModal } from '../tasks/TaskDetailModal.jsx';
import { TaskFormModal } from '../tasks/TaskFormModal.jsx';
import { DecomposeResultModal } from '../tasks/DecomposeResultModal.jsx';
import { TASK_STATUS } from '../../utils/constants.js';

export function KanbanBoard() {
  const [selectedTask, setSelectedTask] = useState(null);
  const [taskToEdit, setTaskToEdit] = useState(null);
  const [parentTaskForNewSubtask, setParentTaskForNewSubtask] = useState(null);
  const [subtasksToView, setSubtasksToView] = useState(null);
  
  const [tasks, setTasks] = useState([
    { id: 1, title: 'Criar estrutura do Kanban', description: 'Implementar layout base', priority: 'HIGH', dueDate: '2026-10-10T12:00:00Z', status: 'TODO', subtasks: [{ title: 'Subtarefa Exemplo', description: 'Teste de visualização' }] }
  ]);

  const handleEdit = (task) => {
    setSelectedTask(null);
    setTaskToEdit(task);
  };

  const handleDelete = (taskId) => {
    setTasks(prev => prev.filter(t => t.id !== taskId));
    alert(`[MOCK] Tarefa ${taskId} excluída com sucesso!`);
    setSelectedTask(null);
  };

  const handleDropColumn = (e, newStatus) => {
    const taskId = e.dataTransfer.getData('taskId');
    if (!taskId) return;
    
    setTasks(prev => prev.map(t => 
      t.id.toString() === taskId ? { ...t, status: newStatus } : t
    ));
    
    // Futura integração: TaskService.updateStatus(taskId, newStatus);
  };

  const handleViewSubtasks = (task) => {
    setSubtasksToView(task.subtasks || []);
  };

  const handleCreateSubtask = (task) => {
    setParentTaskForNewSubtask(task);
  };

  return (
    <>
      <div className="flex justify-center gap-6 h-full min-h-[500px]">
        {Object.entries(TASK_STATUS).map(([statusKey, config]) => {
          const columnTasks = tasks.filter(t => t.status === statusKey);
          return (
            <KanbanColumn 
              key={statusKey}
              statusId={statusKey}
              title={config.label} 
              count={columnTasks.length}
              badgeClass={config.colorClass}
              tasks={columnTasks}
              onTaskClick={setSelectedTask}
              onDropColumn={handleDropColumn}
              onDelete={handleDelete}
              onViewSubtasks={handleViewSubtasks}
              onCreateSubtask={handleCreateSubtask}
            />
          );
        })}
      </div>

      {selectedTask && (
        <TaskDetailModal 
          task={selectedTask} 
          onClose={() => setSelectedTask(null)} 
          onEdit={handleEdit}
          onDelete={handleDelete}
          onUpdateTask={(updates) => {
            setTasks(prev => prev.map(t => t.id === selectedTask.id ? { ...t, ...updates } : t));
            setSelectedTask(prev => ({ ...prev, ...updates }));
          }}
        />
      )}

      <TaskFormModal 
        isOpen={!!taskToEdit || !!parentTaskForNewSubtask}
        initialData={taskToEdit}
        parentId={parentTaskForNewSubtask?.id}
        onClose={() => {
          setTaskToEdit(null);
          setParentTaskForNewSubtask(null);
        }}
        onSaved={() => {
          console.log('[MOCK] Tarefa/Subtarefa salva!');
          setTaskToEdit(null);
          setParentTaskForNewSubtask(null);
        }}
      />

      <DecomposeResultModal 
        isOpen={subtasksToView !== null}
        onClose={() => setSubtasksToView(null)}
        subtasks={subtasksToView}
        isCreating={false}
      />
    </>
  );
}

