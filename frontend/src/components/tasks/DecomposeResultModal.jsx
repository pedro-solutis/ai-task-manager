import { X, Network, ListPlus, Pencil, ArrowRight, ArrowLeft } from 'lucide-react';
import { DeleteButton } from '../common/DeleteButton.jsx';
import { TASK_STATUS } from '../../utils/constants.js';

export function DecomposeResultModal({ isOpen, onClose, subtasks, isCreating, onCreateTasks, onEditSubtask, onDeleteSubtask, onAdvanceStatus, onRetrogressStatus }) {
  if (!isOpen || !subtasks) return null;

  return (
    <div className="fixed inset-0 bg-black/50 backdrop-blur-sm z-[60] flex justify-center items-center p-4">
      <div className="bg-white dark:bg-slate-800 rounded-xl shadow-xl w-full max-w-lg overflow-hidden border border-gray-200 dark:border-gray-700 flex flex-col max-h-[90vh]">
        <div className="p-4 border-b border-gray-200 dark:border-gray-700 flex justify-between items-center bg-amber-50 dark:bg-amber-900/20 shrink-0">
          <div className="flex items-center gap-2 text-amber-700 dark:text-amber-400">
            <Network className="h-5 w-5" />
            <h2 className="text-lg font-bold">Resultado da Decomposição / Subtarefas</h2>
          </div>
          <button onClick={onClose} className="text-gray-500 hover:text-slate-800 dark:hover:text-white transition-colors">
            <X className="h-5 w-5" />
          </button>
        </div>

        <div className="p-6 overflow-y-auto flex-1">
          {subtasks.length === 0 ? (
            <p className="text-sm text-gray-500 dark:text-gray-400 italic text-center py-4">Nenhuma subtarefa encontrada.</p>
          ) : (
            <>
              {isCreating && (
                <p className="text-sm text-gray-600 dark:text-gray-300 mb-4">
                  A IA sugeriu dividir a sua tarefa nas seguintes subtarefas:
                </p>
              )}
              
              <div className="space-y-3">
                {subtasks.map((task, idx) => (
                  <div key={idx} className="bg-gray-50 dark:bg-slate-700/50 p-4 rounded-lg border border-gray-100 dark:border-gray-600 flex flex-col gap-2">
                    <div className="flex gap-3">
                      <div className="flex-shrink-0 mt-0.5">
                        <div className="w-6 h-6 rounded-full bg-amber-100 dark:bg-amber-900/40 text-amber-600 dark:text-amber-400 flex items-center justify-center text-xs font-bold">
                          {idx + 1}
                        </div>
                      </div>
                      <div className="flex-1">
                        <h3 className="font-semibold text-slate-800 dark:text-gray-100 mb-1">{task.title}</h3>
                        <p className="text-sm text-gray-600 dark:text-gray-400 leading-relaxed">{task.description}</p>
                      </div>
                    </div>
                    
                    {!isCreating && (
                      <div className="flex justify-between items-center mt-2 pt-2 border-t border-gray-200 dark:border-gray-600/50">
                        <div>
                          {task.status && (
                            <span className={`px-2 py-0.5 text-[10px] font-semibold uppercase tracking-wider rounded-md ${TASK_STATUS[task.status]?.colorClass || 'bg-gray-200 text-gray-800'}`}>
                              {TASK_STATUS[task.status]?.label || task.status}
                            </span>
                          )}
                        </div>
                        <div className="flex items-center gap-1">
                          {task.status !== 'TODO' && onRetrogressStatus && (
                            <button 
                              onClick={() => onRetrogressStatus(task)} 
                              title="Retroceder Status"
                              className="text-gray-400 hover:text-amber-600 dark:hover:text-amber-400 transition-colors p-1"
                            >
                              <ArrowLeft className="h-4 w-4" />
                            </button>
                          )}
                          {task.status !== 'DONE' && onAdvanceStatus && (
                            <button 
                              onClick={() => onAdvanceStatus(task)} 
                              title="Avançar Status"
                              className="text-gray-400 hover:text-emerald-600 dark:hover:text-emerald-400 transition-colors p-1"
                            >
                              <ArrowRight className="h-4 w-4" />
                            </button>
                          )}
                          {onEditSubtask && (
                            <button 
                              onClick={() => onEditSubtask(task)} 
                              title="Editar Subtarefa"
                              className="text-gray-400 hover:text-blue-600 dark:hover:text-blue-400 transition-colors p-1"
                            >
                              <Pencil className="h-4 w-4" />
                            </button>
                          )}
                          {onDeleteSubtask && (
                            <DeleteButton variant="icon" onClick={() => onDeleteSubtask(task)} />
                          )}
                        </div>
                      </div>
                    )}
                  </div>
                ))}
              </div>
            </>
          )}
        </div>

        <div className="p-4 border-t border-gray-200 dark:border-gray-700 flex justify-end gap-3 bg-gray-50 dark:bg-slate-800/50">
          <button 
            onClick={onClose} 
            className="px-4 py-2 text-sm font-medium bg-gray-200 text-gray-800 hover:bg-gray-300 dark:bg-slate-700 dark:text-white dark:hover:bg-slate-600 rounded-md transition-colors"
          >
            Fechar
          </button>
          {isCreating && onCreateTasks && (
            <button 
              onClick={() => {
                onCreateTasks(subtasks);
                onClose();
              }} 
              className="flex items-center gap-2 px-4 py-2 text-sm font-medium bg-amber-600 text-white hover:bg-amber-700 rounded-md transition-colors"
            >
              <ListPlus className="h-4 w-4" />
              Criar Subtarefas
            </button>
          )}
        </div>
      </div>
    </div>
  );
}

