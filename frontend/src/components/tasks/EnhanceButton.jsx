import { useState } from 'react';
import { Sparkles } from 'lucide-react';

export function EnhanceButton({ formData, onUpdate, disabled, onLoadingChange }) {
  const [isLoading, setIsLoading] = useState(false);

  const handleEnhance = async () => {
    if (!formData.title && !formData.description) return;
    setIsLoading(true);
    if (onLoadingChange) onLoadingChange(true);
    
    try {
      const payload = { title: formData.title, description: formData.description };
      
      // Mock temporário da API
      const result = await new Promise(resolve => {
        setTimeout(() => {
          resolve({
            title: payload.title ? `[Melhorado] ${payload.title}` : '',
            description: payload.description 
              ? `${payload.description}\n\n[Texto expandido pela IA com detalhes e critérios de aceite...]` 
              : 'Esta é uma descrição gerada automaticamente pela IA baseada no contexto fornecido.'
          });
        }, 1200);
      });
      
      const updates = {};
      if (result.title) updates.title = result.title;
      if (result.description) updates.description = result.description;
      
      onUpdate(updates);
    } catch (error) {
      console.error(error);
      alert('Falha ao melhorar com IA');
    } finally {
      setIsLoading(false);
      if (onLoadingChange) onLoadingChange(false);
    }
  };

  return (
    <button 
      type="button" 
      onClick={handleEnhance}
      disabled={disabled || isLoading || (!formData.title && !formData.description)}
      className="flex items-center gap-1 text-xs bg-purple-100 text-purple-700 hover:bg-purple-200 dark:bg-purple-900/30 dark:text-purple-300 dark:hover:bg-purple-900/50 py-1.5 px-3 rounded-md transition-colors disabled:opacity-50"
    >
      <Sparkles className="h-3.5 w-3.5" />
      {isLoading ? 'Melhorando...' : 'Melhorar'}
    </button>
  );
}

