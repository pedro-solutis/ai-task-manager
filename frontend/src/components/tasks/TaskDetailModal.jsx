import { useState } from 'react';
import { X, Calendar, Flag, Pencil } from 'lucide-react';
import { EnhanceButton } from './EnhanceButton.jsx';
import { AnalyzeButton } from './AnalyzeButton.jsx';
import { DecomposeButton } from './DecomposeButton.jsx';
import { DeleteButton } from '../common/DeleteButton.jsx';
import { TASK_STATUS, TASK_PRIORITY } from '../../utils/constants.js';

export function TaskDetailModal({ task, onClose, onEdit, onDelete, onUpdateTask }) {
  const [aiLoading, setAiLoading] = useState(false);

  if (!task) return null;

  const statusConfig = TASK_STATUS[task.status] || { label: task.status, colorClass: 'bg-gray-100 text-gray-800 dark:bg-gray-800 dark:text-gray-300' };
  const priorityConfig = TASK_PRIORITY[task.priority] || { label: task.priority, colorClass: 'text-gray-500' };

  return (
    <div className="fixed inset-0 bg-black/50 backdrop-blur-sm z-50 flex justify-center items-center p-4">
      <div className="bg-white dark:bg-slate-800 rounded-xl shadow-xl w-full max-w-lg overflow-hidden border border-gray-200 dark:border-gray-700">
        <div className="p-4 border-b border-gray-200 dark:border-gray-700 flex justify-between items-center">
          <h2 className="text-xl font-bold text-slate-800 dark:text-gray-100">Detalhes da Tarefa</h2>
          <div className="flex items-center gap-3">
            {task.status && (
              <span className={`px-2.5 py-1 text-xs font-semibold uppercase tracking-wider rounded-md ${statusConfig.colorClass}`}>
                {statusConfig.label}
              </span>
            )}
            <button onClick={onClose} className="text-gray-500 hover:text-slate-800 dark:hover:text-white transition-colors">
              <X className="h-5 w-5" />
            </button>
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
                <Flag className={`h-4 w-4 ${priorityConfig.colorClass}`} />
                <span>Prioridade: <span className="font-semibold">{priorityConfig.label}</span></span>
              </div>
            )}
            
            {task.dueDate && (
              <div className="flex items-center gap-2 text-sm text-gray-600 dark:text-gray-300">
                <Calendar className="h-4 w-4 text-blue-500" />
                <span>Entrega: <span className="font-semibold">{new Date(task.dueDate).toLocaleDateString()}</span></span>
              </div>
            )}
          </div>
          
          {onUpdateTask && (
            <div className="flex gap-2 pt-2 border-t border-gray-200 dark:border-gray-700 mt-4 items-center">
              <span className="text-xs font-semibold text-gray-500 dark:text-gray-400">Ações de IA:</span>
              <EnhanceButton 
                formData={task} 
                onUpdate={onUpdateTask} 
                disabled={aiLoading} 
                onLoadingChange={setAiLoading} 
              />
              <AnalyzeButton 
                formData={task} 
                onUpdate={onUpdateTask} 
                disabled={aiLoading} 
                onLoadingChange={setAiLoading} 
              />
              <DecomposeButton 
                formData={task} 
                onUpdate={onUpdateTask} 
                disabled={aiLoading} 
                onLoadingChange={setAiLoading} 
              />
            </div>
          )}
        </div>
        
        <div className="p-4 border-t border-gray-200 dark:border-gray-700 flex justify-between items-center bg-gray-50 dark:bg-slate-800/50">
          <div className="flex gap-2">
            {onEdit && (
              <button 
                onClick={() => onEdit(task)} 
                className="flex items-center gap-1.5 px-3 py-1.5 text-sm font-medium text-blue-600 dark:text-blue-400 hover:bg-blue-50 dark:hover:bg-blue-900/30 rounded-md transition-colors"
              >
                <Pencil className="h-4 w-4" />
                Editar
              </button>
            )}
            {onDelete && (
              <DeleteButton variant="button" onClick={() => onDelete(task.id)} />
            )}
          </div>
          <button onClick={onClose} className="px-4 py-2 text-sm font-medium bg-gray-200 text-gray-800 hover:bg-gray-300 dark:bg-slate-700 dark:text-white dark:hover:bg-slate-600 rounded-md transition-colors">
            Fechar
          </button>
        </div>
      </div>
    </div>
  );
}

