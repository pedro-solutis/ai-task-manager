import { Trash2 } from 'lucide-react';

export function DeleteButton({ onClick, variant = 'icon', label = "Excluir" }) {
  const handleClick = (e) => {
    e.stopPropagation(); // Prevents click events from bubbling up to parent cards
    if (onClick) onClick();
  };

  if (variant === 'button') {
    return (
      <button 
        type="button"
        onClick={handleClick} 
        title={label}
        className="flex items-center gap-1.5 px-3 py-1.5 text-sm font-medium text-red-600 dark:text-red-400 hover:bg-red-50 dark:hover:bg-red-900/30 rounded-md transition-colors"
      >
        <Trash2 className="h-4 w-4" />
        {label}
      </button>
    );
  }

  // Default 'icon' variant
  return (
    <button 
      type="button"
      onClick={handleClick}
      title={label}
      className="text-gray-400 hover:text-red-500 dark:hover:text-red-400 transition-colors p-1 rounded-md"
    >
      <Trash2 className="h-3.5 w-3.5" />
    </button>
  );
}

