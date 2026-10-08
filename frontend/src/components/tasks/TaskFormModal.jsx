import { useState, useEffect } from 'react';
import { X } from 'lucide-react';
import { EnhanceButton } from './EnhanceButton.jsx';
import { AnalyzeButton } from './AnalyzeButton.jsx';
import { DecomposeButton } from './DecomposeButton.jsx';
import { TASK_PRIORITY } from '../../utils/constants.js';
import { TaskService } from '../../services/TaskService.js';

export function TaskFormModal({ isOpen, onClose, onSaved, initialData, parentId, availableParents }) {
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    priority: 'MEDIUM',
    dueDate: '',
    parentId: parentId || null
  });

  useEffect(() => {
    if (isOpen) {
      if (initialData) {
        setFormData({
          title: initialData.title || '',
          description: initialData.description || '',
          priority: initialData.priority || 'MEDIUM',
          dueDate: initialData.dueDate ? new Date(initialData.dueDate).toISOString().slice(0, 16) : '',
          parentId: initialData.parentId || parentId || null
        });
      } else {
        setFormData({
          title: '',
          description: '',
          priority: 'MEDIUM',
          dueDate: '',
          parentId: parentId || null
        });
      }
    }
  }, [initialData, isOpen, parentId]);

  const [isLoading, setIsLoading] = useState(false);
  const [aiLoading, setAiLoading] = useState(null);
  const [parentsList, setParentsList] = useState(availableParents || []);

  useEffect(() => {
    if (!isOpen) return;

    if (availableParents && availableParents.length > 0) {
      setParentsList(availableParents);
    } else {
      TaskService.findAll(0, 1000).then(res => {
        const data = res.content || res.data || res;
        if (Array.isArray(data)) {
          setParentsList(data);
        }
      }).catch(err => console.error(err));
    }
  }, [isOpen]);

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
      if (!payload.parentId) {
        delete payload.parentId;
      } else {
        payload.parentTaskId = payload.parentId;
        delete payload.parentId;
      }
      
      if (initialData && initialData.id) {
        await TaskService.update(initialData.id, payload);
        if (formData.parentId !== initialData.parentId) {
          await TaskService.updateParent(initialData.id, formData.parentId || null);
        }
      } else {
        await TaskService.create(payload);
      }
      
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
      
      if (!payload.parentId) {
        delete payload.parentId;
      } else {
        payload.parentTaskId = payload.parentId;
        delete payload.parentId;
      }
      
      let parentTaskId = initialData ? initialData.id : null;
      
      // 1. Cria ou atualiza a tarefa pai
      if (parentTaskId) {
        await TaskService.update(parentTaskId, payload);
        if (formData.parentId !== initialData.parentId) {
          await TaskService.updateParent(parentTaskId, formData.parentId || null);
        }
      } else {
        const createdParent = await TaskService.create(payload);
        parentTaskId = createdParent.id;
      }
      
      // 2. Cria cada subtarefa individualmente
      if (subtasks && subtasks.length > 0) {
        for (const subtask of subtasks) {
          const subtaskPayload = {
            title: subtask.title,
            description: subtask.description,
            priority: payload.priority || 'MEDIUM',
            dueDate: payload.dueDate,
            parentTaskId: parentTaskId
          };
          await TaskService.create(subtaskPayload);
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

          <div>
            <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">Tarefa Pai (Opcional)</label>
            <select
              name="parentId"
              value={formData.parentId || ''}
              onChange={(e) => setFormData(prev => ({ ...prev, parentId: e.target.value ? e.target.value : null }))}
              className="w-full bg-gray-50 dark:bg-slate-700 border border-gray-300 dark:border-slate-600 rounded-md px-3 py-2 text-slate-800 dark:text-gray-100 focus:ring-2 focus:ring-blue-500 outline-none"
            >
              <option value="">Sem tarefa pai</option>
              {parentsList
                .filter(t => !initialData || t.id !== initialData.id)
                .map(t => (
                  <option key={t.id} value={t.id}>{t.title}</option>
              ))}
            </select>
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
              isCreating={true}
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

