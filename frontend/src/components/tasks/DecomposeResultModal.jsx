import { X, Network, ListPlus } from 'lucide-react';

export function DecomposeResultModal({ isOpen, onClose, subtasks, isCreating, onCreateTasks }) {
  if (!isOpen || !subtasks) return null;

  return (
    <div className="fixed inset-0 bg-black/50 backdrop-blur-sm z-[60] flex justify-center items-center p-4">
      <div className="bg-white dark:bg-slate-800 rounded-xl shadow-xl w-full max-w-lg overflow-hidden border border-gray-200 dark:border-gray-700">
        <div className="p-4 border-b border-gray-200 dark:border-gray-700 flex justify-between items-center bg-amber-50 dark:bg-amber-900/20">
          <div className="flex items-center gap-2 text-amber-700 dark:text-amber-400">
            <Network className="h-5 w-5" />
            <h2 className="text-lg font-bold">Resultado da Decomposição / Subtarefas</h2>
          </div>
          <button onClick={onClose} className="text-gray-500 hover:text-slate-800 dark:hover:text-white transition-colors">
            <X className="h-5 w-5" />
          </button>
        </div>

        <div className="p-6 max-h-[60vh] overflow-y-auto">
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
                  <div key={idx} className="bg-gray-50 dark:bg-slate-700/50 p-4 rounded-lg border border-gray-100 dark:border-gray-600 flex gap-3">
                    <div className="flex-shrink-0 mt-0.5">
                      <div className="w-6 h-6 rounded-full bg-amber-100 dark:bg-amber-900/40 text-amber-600 dark:text-amber-400 flex items-center justify-center text-xs font-bold">
                        {idx + 1}
                      </div>
                    </div>
                    <div>
                      <h3 className="font-semibold text-slate-800 dark:text-gray-100 mb-1">{task.title}</h3>
                      <p className="text-sm text-gray-600 dark:text-gray-400 leading-relaxed">{task.description}</p>
                    </div>
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

