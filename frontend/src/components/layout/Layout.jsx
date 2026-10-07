import { useState, useEffect } from 'react';
import { Sun, Moon, Plus, MessageSquareMore } from 'lucide-react';

export function Layout({ children }) {
  const [theme, setTheme] = useState(() => {
    return localStorage.getItem('theme') || 'dark';
  });

  useEffect(() => {
    if (theme === 'dark') {
      document.documentElement.classList.add('dark');
    } else {
      document.documentElement.classList.remove('dark');
    }
    localStorage.setItem('theme', theme);
  }, [theme]);

  const toggleTheme = () => {
    setTheme(prev => (prev === 'dark' ? 'light' : 'dark'));
  };

  return (
    <div className="min-h-screen flex flex-col transition-colors duration-200">
      {/* Barra superior do cabeçalho */}
      <header className="px-6 py-4 border-b border-gray-200 dark:border-gray-800 bg-white dark:bg-surface-dark flex justify-between items-center shadow-sm">
        <h1 className="text-2xl font-bold text-slate-800 dark:text-white">
          AI Task Manager
        </h1>
        
        <div className="flex items-center gap-4">
          {/* Botão de mudança de tema */}
          <button
            onClick={toggleTheme}
            className="p-2 rounded-md hover:bg-gray-100 dark:hover:bg-slate-700 transition-colors"
            aria-label="Toggle Theme"
          >
            {theme === 'dark' ? (
              <Sun className="h-5 w-5 text-yellow-400" />
            ) : (
              <Moon className="h-5 w-5 text-slate-600" />
            )}
          </button>
            {/* Botão de criar nota tarefa */}
          <button className="bg-blue-600 hover:bg-blue-700 text-white font-medium py-2 px-4 rounded-md flex items-center gap-2 transition-colors">
            <Plus className="h-5 w-5" />
            Nova Tarefa
          </button>
        </div>
      </header>

      <main className="flex-1 overflow-x-auto p-6 flex flex-col items-center">
        <div className="w-full max-w-7xl">
          {children}
        </div>
      </main>

      {/* Botão Flutuante do Chat da IA */}
      <button 
        className="fixed bottom-6 right-6 p-4 rounded-full bg-blue-600 hover:bg-blue-700 text-white shadow-lg transition-transform hover:scale-105 z-50 flex items-center justify-center group"
        aria-label="Abrir Assistente de Chat IA"
        title="Assistente IA"
      >
        <MessageSquareMore className="h-6 w-6" />
        <span className="absolute -top-1 -right-1 flex h-3 w-3">
          <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-blue-400 opacity-75"></span>
          <span className="relative inline-flex rounded-full h-3 w-3 bg-blue-500"></span>
        </span>
      </button>
    </div>
  );
}

