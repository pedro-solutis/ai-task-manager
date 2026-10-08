import { X, Activity, Clock, AlertTriangle, CheckCircle, Flag } from 'lucide-react';
import { TASK_PRIORITY } from '../../utils/constants.js';

const COMPLEXITY_TRANSLATION = {
  HIGH: 'Alta',
  MEDIUM: 'Média',
  LOW: 'Baixa'
};

export function AnalysisResultModal({ isOpen, onClose, result, onApply }) {
  if (!isOpen || !result) return null;

  const priorityConfig = TASK_PRIORITY[result.priority] || { label: result.priority || 'Não definida', colorClass: 'text-gray-500' };
  const translatedComplexity = COMPLEXITY_TRANSLATION[result.complexity] || result.complexity || 'Não avaliada';

  return (
    <div className="fixed inset-0 bg-black/50 backdrop-blur-sm z-[60] flex justify-center items-center p-4">
      <div className="bg-white dark:bg-slate-800 rounded-xl shadow-xl w-full max-w-md overflow-hidden border border-gray-200 dark:border-gray-700">
        <div className="p-4 border-b border-gray-200 dark:border-gray-700 flex justify-between items-center bg-emerald-50 dark:bg-emerald-900/20">
          <div className="flex items-center gap-2 text-emerald-700 dark:text-emerald-400">
            <Activity className="h-5 w-5" />
            <h2 className="text-lg font-bold">Resultado da Análise</h2>
          </div>
          <button onClick={onClose} className="text-gray-500 hover:text-slate-800 dark:hover:text-white transition-colors">
            <X className="h-5 w-5" />
          </button>
        </div>

        <div className="p-6 space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <div className="bg-gray-50 dark:bg-slate-700/50 p-4 rounded-lg border border-gray-100 dark:border-gray-600">
              <div className="flex items-center gap-2 text-sm text-gray-500 dark:text-gray-400 mb-1">
                <AlertTriangle className="h-4 w-4" />
                <span>Prioridade Sugerida</span>
              </div>
              <div className="flex items-center gap-2">
                <Flag className={`h-4 w-4 ${priorityConfig.colorClass}`} />
                <p className={`font-semibold ${priorityConfig.colorClass}`}>{priorityConfig.label}</p>
              </div>
            </div>
            
            <div className="bg-gray-50 dark:bg-slate-700/50 p-4 rounded-lg border border-gray-100 dark:border-gray-600">
              <div className="flex items-center gap-2 text-sm text-gray-500 dark:text-gray-400 mb-1">
                <Clock className="h-4 w-4" />
                <span>Estimativa</span>
              </div>
              <p className="font-semibold text-slate-800 dark:text-gray-100">{result.estimatedHours ? `${result.estimatedHours} horas` : 'N/A'}</p>
            </div>
          </div>

          <div className="bg-gray-50 dark:bg-slate-700/50 p-4 rounded-lg border border-gray-100 dark:border-gray-600">
            <h3 className="text-sm text-gray-500 dark:text-gray-400 mb-2 font-medium">Complexidade</h3>
            <p className="text-slate-800 dark:text-gray-100">{translatedComplexity}</p>
          </div>

          <div className="bg-gray-50 dark:bg-slate-700/50 p-4 rounded-lg border border-gray-100 dark:border-gray-600">
            <h3 className="text-sm text-gray-500 dark:text-gray-400 mb-2 font-medium">Motivo</h3>
            <p className="text-slate-800 dark:text-gray-100 text-sm leading-relaxed">{result.reason}</p>
          </div>
        </div>

        <div className="p-4 border-t border-gray-200 dark:border-gray-700 flex justify-end gap-3 bg-gray-50 dark:bg-slate-800/50">
          <button 
            onClick={onClose} 
            className="px-4 py-2 text-sm font-medium bg-gray-200 text-gray-800 hover:bg-gray-300 dark:bg-slate-700 dark:text-white dark:hover:bg-slate-600 rounded-md transition-colors"
          >
            Fechar
          </button>
          {onApply && (
            <button 
              onClick={() => {
                onApply(result);
                onClose();
              }} 
              className="flex items-center gap-2 px-4 py-2 text-sm font-medium bg-emerald-600 text-white hover:bg-emerald-700 rounded-md transition-colors"
            >
              <CheckCircle className="h-4 w-4" />
              Aplicar à Tarefa
            </button>
          )}
        </div>
      </div>
    </div>
  );
}

