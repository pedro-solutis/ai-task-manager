import { useState, useEffect } from 'react';
import { X } from 'lucide-react';
import { EnhanceButton } from './EnhanceButton.jsx';
import { AnalyzeButton } from './AnalyzeButton.jsx';
import { DecomposeButton } from './DecomposeButton.jsx';
import { TASK_PRIORITY } from '../../utils/constants.js';

export function TaskFormModal({ isOpen, onClose, onSaved, initialData }) {
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    priority: 'MEDIUM',
    dueDate: ''
  });

  useEffect(() => {
    if (isOpen) {
      if (initialData) {
        setFormData({
          title: initialData.title || '',
          description: initialData.description || '',
          priority: initialData.priority || 'MEDIUM',
          dueDate: initialData.dueDate ? new Date(initialData.dueDate).toISOString().slice(0, 16) : ''
        });
      } else {
        setFormData({
          title: '',
          description: '',
          priority: 'MEDIUM',
          dueDate: ''
        });
      }
    }
  }, [initialData, isOpen]);

  const [isLoading, setIsLoading] = useState(false);
  const [aiLoading, setAiLoading] = useState(null);

  if (!isOpen) return null;

  const handleChange = (e) => {
    setFormData(prev => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleSubmit = async (e) => {
    if (e && e.preventDefault) e.preventDefault();
    setIsLoading(true);
    try {
      const payload = {
         ...formData,
         dueDate: formData.dueDate ? new Date(formData.dueDate).toISOString() : null
      };
      
      // Mock temporário da API para criação da tarefa
      await new Promise(resolve => setTimeout(resolve, 800));
      console.log('Tarefa mockada criada:', payload);
      
      if (onSaved) onSaved();
      onClose();
    } catch (error) {
      console.error(error);
      alert('Erro ao salvar tarefa');
    } finally {
      setIsLoading(false);
    }
  };

  const handleCreateWithSubtasks = async (subtasks) => {
    setIsLoading(true);
    try {
      const payload = {
         ...formData,
         dueDate: formData.dueDate ? new Date(formData.dueDate).toISOString() : null
      };
      
      // 1. Cria a tarefa pai
      await new Promise(resolve => setTimeout(resolve, 800));
      const parentTaskId = Math.floor(Math.random() * 1000) + 100;
      console.log('Tarefa pai criada via IA:', { ...payload, id: parentTaskId });
      
      // 2. Cria cada subtarefa individualmente
      if (subtasks && subtasks.length > 0) {
        console.log(`Criando ${subtasks.length} subtarefas vinculadas ao pai ID: ${parentTaskId}...`);
        for (const subtask of subtasks) {
          await new Promise(resolve => setTimeout(resolve, 300));
          const subtaskPayload = {
            title: subtask.title,
            description: subtask.description,
            priority: payload.priority,
            parentId: parentTaskId
          };
          console.log('Subtarefa criada individualmente via IA:', subtaskPayload);
        }
      }
      
      if (onSaved) onSaved();
      onClose();
    } catch (error) {
      console.error(error);
      alert('Erro ao criar tarefas com IA');
    } finally {
      setIsLoading(false);
    }
  };


  return (
    <div className="fixed inset-0 bg-black/50 backdrop-blur-sm z-50 flex justify-center items-center p-4">
      <div className="bg-white dark:bg-slate-800 rounded-xl shadow-xl w-full max-w-lg overflow-hidden border border-gray-200 dark:border-gray-700">
        <div className="p-4 border-b border-gray-200 dark:border-gray-700 flex justify-between items-center">
          <h2 className="text-xl font-bold text-slate-800 dark:text-gray-100">{initialData ? 'Editar Tarefa' : 'Nova Tarefa'}</h2>
        </div>

        <form onSubmit={handleSubmit} className="p-4 space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">Título</label>
            <input 
              type="text" 
              name="title"
              required
              value={formData.title}
              onChange={handleChange}
              className="w-full bg-gray-50 dark:bg-slate-700 border border-gray-300 dark:border-slate-600 rounded-md px-3 py-2 text-slate-800 dark:text-gray-100 focus:ring-2 focus:ring-blue-500 outline-none"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">Descrição</label>
            <textarea 
              name="description"
              rows="3"
              value={formData.description}
              onChange={handleChange}
              className="w-full bg-gray-50 dark:bg-slate-700 border border-gray-300 dark:border-slate-600 rounded-md px-3 py-2 text-slate-800 dark:text-gray-100 focus:ring-2 focus:ring-blue-500 outline-none"
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">Prioridade</label>
              <select 
                name="priority"
                value={formData.priority}
                onChange={handleChange}
                className="w-full bg-gray-50 dark:bg-slate-700 border border-gray-300 dark:border-slate-600 rounded-md px-3 py-2 text-slate-800 dark:text-gray-100 focus:ring-2 focus:ring-blue-500 outline-none"
              >
                {Object.entries(TASK_PRIORITY).map(([key, config]) => (
                  <option key={key} value={key}>{config.label}</option>
                ))}
              </select>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">Data de Entrega</label>
              <input 
                type="datetime-local" 
                name="dueDate"
                value={formData.dueDate}
                onChange={handleChange}
                className="w-full bg-gray-50 dark:bg-slate-700 border border-gray-300 dark:border-slate-600 rounded-md px-3 py-2 text-slate-800 dark:text-gray-100 focus:ring-2 focus:ring-blue-500 outline-none"
              />
            </div>
          </div>

          <div className="flex gap-2 pt-2 border-t border-gray-200 dark:border-gray-700 mt-4 items-center">
            <span className="text-xs font-semibold text-gray-500 dark:text-gray-400">Ações de IA:</span>
            <EnhanceButton 
              formData={formData} 
              onUpdate={(updates) => setFormData(prev => ({ ...prev, ...updates }))} 
              disabled={aiLoading} 
              onLoadingChange={setAiLoading} 
            />
            <AnalyzeButton 
              formData={formData} 
              onUpdate={(updates) => setFormData(prev => ({ ...prev, ...updates }))} 
              disabled={aiLoading} 
              onLoadingChange={setAiLoading} 
            />
            <DecomposeButton 
              formData={formData} 
              onUpdate={(updates) => setFormData(prev => ({ ...prev, ...updates }))} 
              disabled={aiLoading} 
              onLoadingChange={setAiLoading}
              isCreating={!initialData}
              onCreateTasks={handleCreateWithSubtasks}
            />
          </div>

          <div className="flex justify-end gap-3 pt-4">
            <button type="button" onClick={onClose} className="px-4 py-2 text-sm font-medium text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-slate-700 rounded-md transition-colors">
              Cancelar
            </button>
            <button type="submit" disabled={isLoading} className="px-4 py-2 text-sm font-medium bg-blue-600 hover:bg-blue-700 text-white rounded-md transition-colors disabled:opacity-50">
              Salvar Tarefa
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

