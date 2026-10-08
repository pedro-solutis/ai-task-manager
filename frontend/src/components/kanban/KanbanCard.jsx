import { Calendar, Flag, ListTree, Plus } from 'lucide-react';
import { TASK_PRIORITY } from '../../utils/constants.js';
import { DeleteButton } from '../common/DeleteButton.jsx';

export function KanbanCard({ task, onClick, onDelete, onViewSubtasks, onCreateSubtask }) {
  const priorityConfig = TASK_PRIORITY[task.priority] || { label: task.priority, colorClass: 'text-gray-500' };

  return (
    <div 
      draggable
      onDragStart={(e) => e.dataTransfer.setData('taskId', task.id)}
      onClick={() => onClick && onClick(task)}
      className="p-4 rounded-md bg-white dark:bg-slate-700 shadow-sm border border-gray-200 dark:border-gray-600 cursor-pointer hover:border-blue-500 transition-colors flex flex-col gap-2"
    >
      <div>
        <h3 className="font-medium text-slate-800 dark:text-gray-100">{task.title}</h3>
        <p className="text-sm text-gray-500 dark:text-gray-400 mt-1 line-clamp-2">{task.description}</p>
      </div>

      <div className="flex items-center justify-between pt-2 mt-1 border-t border-gray-100 dark:border-gray-600/50 text-xs text-gray-500 dark:text-gray-400">
        <div className="flex items-center gap-3">
          {task.priority && (
            <div className="flex items-center gap-1">
              <Flag className={`h-3 w-3 ${priorityConfig.colorClass}`} />
              <span className="font-medium">{priorityConfig.label}</span>
            </div>
          )}
          
          {task.dueDate && (
            <div className="flex items-center gap-1">
              <Calendar className="h-3 w-3" />
              <span>{new Date(task.dueDate).toLocaleDateString()}</span>
            </div>
          )}
        </div>
        
        <div className="flex items-center gap-1">
          {/* Botão de Ver Subtarefas */}
          <button 
            type="button"
            title="Visualizar subtarefas"
            onClick={(e) => {
              e.stopPropagation();
              if (onViewSubtasks) onViewSubtasks(task);
            }}
            className="text-gray-400 hover:text-blue-500 dark:hover:text-blue-400 transition-colors p-1 rounded-md flex items-center gap-1"
          >
            <ListTree className="h-3.5 w-3.5" />
            {(task.subtasks?.length > 0) && <span className="font-medium">{task.subtasks.length}</span>}
          </button>

          {/* Botão de Adicionar Subtarefa */}
          <button 
            type="button"
            title="Criar subtarefa"
            onClick={(e) => {
              e.stopPropagation();
              if (onCreateSubtask) onCreateSubtask(task);
            }}
            className="text-gray-400 hover:text-emerald-500 dark:hover:text-emerald-400 transition-colors p-1 rounded-md"
          >
            <Plus className="h-3.5 w-3.5" />
          </button>

          {onDelete && (
            <DeleteButton 
              onClick={() => onDelete(task.id)} 
              variant="icon" 
              label="Excluir tarefa" 
            />
          )}
        </div>
      </div>
    </div>
  );
}

