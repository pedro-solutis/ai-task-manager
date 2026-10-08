import { useState, useEffect } from 'react';
import { KanbanColumn } from './KanbanColumn.jsx';
import { TaskDetailModal } from '../tasks/TaskDetailModal.jsx';
import { TaskFormModal } from '../tasks/TaskFormModal.jsx';
import { DecomposeResultModal } from '../tasks/DecomposeResultModal.jsx';
import { TASK_STATUS } from '../../utils/constants.js';
import { TaskService } from '../../services/TaskService.js';

export function KanbanBoard({ refreshTick }) {
  const [selectedTask, setSelectedTask] = useState(null);
  const [taskToEdit, setTaskToEdit] = useState(null);
  const [parentTaskForNewSubtask, setParentTaskForNewSubtask] = useState(null);
  
  const [viewingSubtasksParentId, setViewingSubtasksParentId] = useState(null);
  
  const [tasks, setTasks] = useState([]);

  const loadTasks = async () => {
    try {
      const response = await TaskService.findAll();
      let data = response || [];
      if (!Array.isArray(data)) data = [];
      
      data = data.map(task => ({
        ...task,
        subtasks: task.subTasks || task.subtasks || data.filter(t => t.parentTaskId === task.id)
      }));

      setTasks(data);
    } catch (error) {
      console.error('Erro ao buscar tarefas:', error);
    }
  };

  useEffect(() => {
    loadTasks();
  }, [refreshTick]);

  const parentTaskForView = tasks.find(t => t.id === viewingSubtasksParentId);
  const subtasksToView = parentTaskForView?.subtasks || null;

  const handleEdit = (task) => {
    setSelectedTask(null);
    setTaskToEdit(task);
  };

  const handleDelete = async (taskId) => {
    try {
      await TaskService.delete(taskId);
      setTasks(prev => prev.filter(t => t.id !== taskId));
      setSelectedTask(null);
    } catch (error) {
      console.error('Erro ao deletar tarefa:', error);
      alert('Erro ao deletar tarefa');
    }
  };

  const handleDropColumn = async (e, newStatus) => {
    const taskId = e.dataTransfer.getData('taskId');
    if (!taskId) return;
    
    setTasks(prev => prev.map(t => 
      t.id.toString() === taskId ? { ...t, status: newStatus } : t
    ));
    
    try {
      await TaskService.updateStatus(taskId, newStatus);
      loadTasks();
    } catch (error) {
      console.error('Erro ao atualizar status:', error);
      loadTasks();
    }
  };

  const handleViewSubtasks = (task) => {
    setViewingSubtasksParentId(task.id);
  };

  const handleCreateSubtask = (task) => {
    setParentTaskForNewSubtask(task);
  };

  const handleAdvanceSubtaskStatus = async (subtask) => {
    const nextStatus = subtask.status === 'TODO' ? 'IN_PROGRESS' : subtask.status === 'IN_PROGRESS' ? 'DONE' : 'DONE';
    
    setTasks(prev => prev.map(t => {
      if (t.id === (subtask.parentTaskId || subtask.parentId)) {
        return {
          ...t,
          subtasks: t.subtasks.map(st => st.id === subtask.id ? { ...st, status: nextStatus } : st)
        };
      }
      return t;
    }));

    try {
      await TaskService.updateStatus(subtask.id, nextStatus);
      loadTasks();
    } catch (error) {
      console.error('Erro ao atualizar sub-tarefa:', error);
      loadTasks();
    }
  };

  const handleRetrogressSubtaskStatus = async (subtask) => {
    const prevStatus = subtask.status === 'DONE' ? 'IN_PROGRESS' : subtask.status === 'IN_PROGRESS' ? 'TODO' : 'TODO';
    
    setTasks(prev => prev.map(t => {
      if (t.id === (subtask.parentTaskId || subtask.parentId)) {
        return {
          ...t,
          subtasks: t.subtasks.map(st => st.id === subtask.id ? { ...st, status: prevStatus } : st)
        };
      }
      return t;
    }));

    try {
      await TaskService.updateStatus(subtask.id, prevStatus);
      loadTasks();
    } catch (error) {
      console.error('Erro ao retroceder sub-tarefa:', error);
      loadTasks();
    }
  };

  const handleEditSubtask = (subtask) => {
    setViewingSubtasksParentId(null);
    setTaskToEdit(subtask);
  };

  const handleDeleteSubtask = async (subtask) => {
    try {
      await TaskService.delete(subtask.id);
      setTasks(prev => prev.map(t => {
        if (t.id === (subtask.parentTaskId || subtask.parentId)) {
          return {
            ...t,
            subtasks: t.subtasks.filter(st => st.id !== subtask.id)
          };
        }
        return t;
      }));
    } catch (error) {
      console.error('Erro ao deletar sub-tarefa:', error);
      alert('Erro ao deletar sub-tarefa');
    }
  };

  return (
    <>
      <div className="flex justify-center gap-6 h-full min-h-[500px]">
        {Object.entries(TASK_STATUS).map(([statusKey, config]) => {
          const columnTasks = tasks.filter(t => t.status === statusKey && !t.parentTaskId);
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
          onUpdateTask={async (updates) => {
            const updatedTask = { ...selectedTask, ...updates };
            setTasks(prev => prev.map(t => t.id === selectedTask.id ? updatedTask : t));
            setSelectedTask(updatedTask);
            try {
              await TaskService.update(selectedTask.id, updatedTask);
              loadTasks();
            } catch (error) {
              console.error('Erro ao atualizar tarefa via IA:', error);
            }
          }}
        />
      )}

      <TaskFormModal 
        isOpen={!!taskToEdit || !!parentTaskForNewSubtask}
        initialData={taskToEdit}
        parentId={parentTaskForNewSubtask?.id}
        availableParents={tasks}
        onClose={() => {
          setTaskToEdit(null);
          setParentTaskForNewSubtask(null);
        }}
        onSaved={() => {
          loadTasks();
          setTaskToEdit(null);
          setParentTaskForNewSubtask(null);
        }}
      />

      <DecomposeResultModal 
        isOpen={subtasksToView !== null}
        onClose={() => setViewingSubtasksParentId(null)}
        subtasks={subtasksToView}
        isCreating={false}
        onAdvanceStatus={handleAdvanceSubtaskStatus}
        onRetrogressStatus={handleRetrogressSubtaskStatus}
        onEditSubtask={handleEditSubtask}
        onDeleteSubtask={handleDeleteSubtask}
      />
    </>
  );
}

