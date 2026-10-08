import { useState } from 'react';
import { Network } from 'lucide-react';

export function DecomposeButton({ formData, onUpdate, disabled, onLoadingChange }) {
  const [isLoading, setIsLoading] = useState(false);

  const handleDecompose = async () => {
    if (!formData.title && !formData.description) return;
    setIsLoading(true);
    if (onLoadingChange) onLoadingChange(true);
    
    try {
      const payload = { title: formData.title, description: formData.description };
      
      // Mock temporário da API
      const result = await new Promise(resolve => {
        setTimeout(() => {
          resolve([
            { title: 'Subtarefa 1', description: 'Parte 1 da tarefa' },
            { title: 'Subtarefa 2', description: 'Parte 2 da tarefa' },
            { title: 'Subtarefa 3', description: 'Parte 3 da tarefa' }
          ]);
        }, 1500);
      });
      
      const decomposeText = `\n\n--- Sugestão de Subtarefas ---\n${result.map((st, i) => `${i + 1}. ${st.title}: ${st.description}`).join('\n')}`;
      
      onUpdate({ description: formData.description + decomposeText });
    } catch (error) {
      console.error(error);
      alert('Falha ao decompor com IA');
    } finally {
      setIsLoading(false);
      if (onLoadingChange) onLoadingChange(false);
    }
  };

  return (
    <button 
      type="button" 
      onClick={handleDecompose}
      disabled={disabled || isLoading || (!formData.title && !formData.description)}
      className="flex items-center gap-1 text-xs bg-amber-100 text-amber-700 hover:bg-amber-200 dark:bg-amber-900/30 dark:text-amber-300 dark:hover:bg-amber-900/50 py-1.5 px-3 rounded-md transition-colors disabled:opacity-50"
    >
      <Network className="h-3.5 w-3.5" />
      {isLoading ? 'Decompondo...' : 'Decompor'}
    </button>
  );
}

