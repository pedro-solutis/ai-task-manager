import { Calendar, Flag } from 'lucide-react';

export function KanbanCard({ task, onClick }) {
  const getPriorityColor = (priority) => {
    switch(priority) {
      case 'HIGH': return 'text-red-500';
      case 'MEDIUM': return 'text-yellow-500';
      case 'LOW': return 'text-green-500';
      default: return 'text-gray-500';
    }
  };

  return (
    <div 
      onClick={() => onClick && onClick(task)}
      className="p-4 rounded-md bg-white dark:bg-slate-700 shadow-sm border border-gray-200 dark:border-gray-600 cursor-pointer hover:border-blue-500 transition-colors flex flex-col gap-2"
    >
      <div>
        <h3 className="font-medium text-slate-800 dark:text-gray-100">{task.title}</h3>
        <p className="text-sm text-gray-500 dark:text-gray-400 mt-1 line-clamp-2">{task.description}</p>
      </div>

      {(task.dueDate || task.priority) && (
        <div className="flex items-center gap-3 pt-2 mt-1 border-t border-gray-100 dark:border-gray-600/50 text-xs text-gray-500 dark:text-gray-400">
          {task.priority && (
            <div className="flex items-center gap-1">
              <Flag className={`h-3 w-3 ${getPriorityColor(task.priority)}`} />
              <span className="font-medium">{task.priority === 'HIGH' ? 'Alta' : task.priority === 'MEDIUM' ? 'Média' : 'Baixa'}</span>
            </div>
          )}
          
          {task.dueDate && (
            <div className="flex items-center gap-1">
              <Calendar className="h-3 w-3" />
              <span>{new Date(task.dueDate).toLocaleDateString()}</span>
            </div>
          )}
        </div>
      )}
    </div>
  );
}

