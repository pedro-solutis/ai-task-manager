import { useState } from 'react';
import { Activity } from 'lucide-react';
import { AiService } from '../../services/AiService.js';
import { AnalysisResultModal } from './AnalysisResultModal.jsx';

export function AnalyzeButton({ formData, onUpdate, disabled, onLoadingChange }) {
  const [isLoading, setIsLoading] = useState(false);
  const [analysisResult, setAnalysisResult] = useState(null);
  const [showModal, setShowModal] = useState(false);

  const handleAnalyze = async () => {
    if (!formData.title && !formData.description) return;
    setIsLoading(true);
    if (onLoadingChange) onLoadingChange(true);
    
    try {
      let result;
      if (formData.id) {
        result = await AiService.analyzeById(formData.id);
      } else {
        const payload = { 
          title: formData.title, 
          description: formData.description,
          priority: formData.priority,
          dueDate: formData.dueDate ? new Date(formData.dueDate).toISOString() : null
        };
        result = await AiService.analyzePreview(payload);
      }
      
      setAnalysisResult(result);
      setShowModal(true);
      
    } catch (error) {
      console.error(error);
      alert('Falha ao analisar com IA');
    } finally {
      setIsLoading(false);
      if (onLoadingChange) onLoadingChange(false);
    }
  };

  const handleApply = (result) => {
    const updates = {};
    if (result.priority) updates.priority = result.priority;
    
    onUpdate(updates);
  };

  return (
    <>
      <button 
        type="button" 
        onClick={handleAnalyze}
        disabled={disabled || isLoading || (!formData.title && !formData.description)}
        className="flex items-center gap-1 text-xs bg-emerald-100 text-emerald-700 hover:bg-emerald-200 dark:bg-emerald-900/30 dark:text-emerald-300 dark:hover:bg-emerald-900/50 py-1.5 px-3 rounded-md transition-colors disabled:opacity-50"
      >
        <Activity className="h-3.5 w-3.5" />
        {isLoading ? 'Analisando...' : 'Analisar'}
      </button>

      <AnalysisResultModal 
        isOpen={showModal} 
        onClose={() => setShowModal(false)} 
        result={analysisResult} 
        onApply={onUpdate ? handleApply : undefined} 
      />
    </>
  );
}

