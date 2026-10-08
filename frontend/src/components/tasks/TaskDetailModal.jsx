import { X, Calendar, Flag, Pencil, Trash2 } from 'lucide-react';

export function TaskDetailModal({ task, onClose, onEdit, onDelete }) {
  if (!task) return null;

  const statusColors = {
    TODO: 'bg-red-100 text-red-800 dark:bg-red-900/30 dark:text-red-300',
    IN_PROGRESS: 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900/30 dark:text-yellow-300',
    DONE: 'bg-green-100 text-green-800 dark:bg-green-900/30 dark:text-green-300'
  };

  const statusColorClass = statusColors[task.status] || 'bg-gray-100 text-gray-800 dark:bg-gray-800 dark:text-gray-300';

  return (
    <div className="fixed inset-0 bg-black/50 backdrop-blur-sm z-50 flex justify-center items-center p-4">
      <div className="bg-white dark:bg-slate-800 rounded-xl shadow-xl w-full max-w-lg overflow-hidden border border-gray-200 dark:border-gray-700">
        <div className="p-4 border-b border-gray-200 dark:border-gray-700 flex justify-between items-center">
          <h2 className="text-xl font-bold text-slate-800 dark:text-gray-100">Detalhes da Tarefa</h2>
          <div className="flex items-center gap-3">
            {task.status && (
              <span className={`px-2.5 py-1 text-xs font-semibold uppercase tracking-wider rounded-md ${statusColorClass}`}>
                {task.status === 'TODO' ? 'A Fazer' : task.status === 'IN_PROGRESS' ? 'Em Andamento' : task.status === 'DONE' ? 'Concluído' : task.status}
              </span>
            )}
          </div>
        </div>

        <div className="p-6 space-y-4">
          <div>
            <h3 className="text-lg font-bold text-slate-900 dark:text-white mb-2">{task.title}</h3>
            <p className="text-gray-600 dark:text-gray-300 whitespace-pre-wrap leading-relaxed">{task.description || 'Nenhuma descrição fornecida.'}</p>
          </div>

          <div className="flex flex-wrap gap-4 pt-4 border-t border-gray-200 dark:border-gray-700">
            {task.priority && (
              <div className="flex items-center gap-2 text-sm text-gray-600 dark:text-gray-300">
                <Flag className="h-4 w-4 text-orange-500" />
                <span>Prioridade: <span className="font-semibold">{task.priority}</span></span>
              </div>
            )}
            
            {task.dueDate && (
              <div className="flex items-center gap-2 text-sm text-gray-600 dark:text-gray-300">
                <Calendar className="h-4 w-4 text-blue-500" />
                <span>Entrega: <span className="font-semibold">{new Date(task.dueDate).toLocaleDateString()}</span></span>
              </div>
            )}
          </div>
        </div>
        
        <div className="p-4 border-t border-gray-200 dark:border-gray-700 flex justify-between items-center bg-gray-50 dark:bg-slate-800/50">
          <button onClick={onClose} className="px-4 py-2 text-sm font-medium bg-gray-200 text-gray-800 hover:bg-gray-300 dark:bg-slate-700 dark:text-white dark:hover:bg-slate-600 rounded-md transition-colors">
            Fechar
          </button>
        </div>
      </div>
    </div>
  );
}

