import { useState } from 'react';
import { X, Send, Bot } from 'lucide-react';

export function ChatAssistant({ isOpen, onClose }) {
  const [messages, setMessages] = useState([{ role: 'bot', text: 'Olá! Como posso ajudar você a organizar suas tarefas hoje?' }]);
  const [input, setInput] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const handleSend = async (e) => {
    e.preventDefault();
    if (!input.trim()) return;

    const userMessage = input.trim();
    setMessages(prev => [...prev, { role: 'user', text: userMessage }]);
    setInput('');
    setIsLoading(true);

    try {
      // Mock temporário da API
      const text = await new Promise((resolve) => {
        setTimeout(() => {
          resolve(`Esta é uma resposta simulada para: "${userMessage}". A integração com o backend está desativada.`);
        }, 1000);
      });
      setMessages(prev => [...prev, { role: 'bot', text }]);
    } catch (error) {
      console.error(error);
      setMessages(prev => [...prev, { role: 'bot', text: 'Desculpe, ocorreu um erro de conexão.' }]);
    } finally {
      setIsLoading(false);
    }
  };

  if (!isOpen) return null;

  return (
    <div className="fixed bottom-24 right-6 w-80 sm:w-96 h-[500px] bg-white dark:bg-slate-800 rounded-xl shadow-2xl border border-gray-200 dark:border-gray-700 flex flex-col overflow-hidden z-50">
      <div className="bg-blue-600 p-4 flex justify-between items-center text-white">
        <div className="flex items-center gap-2 font-semibold">
          <Bot className="h-5 w-5" />
          Assistente IA
        </div>
        <button onClick={onClose} className="hover:bg-blue-700 p-1 rounded-full transition-colors">
          <X className="h-5 w-5" />
        </button>
      </div>

      <div className="flex-1 overflow-y-auto p-4 space-y-4 bg-slate-50 dark:bg-slate-900">
        {messages.map((msg, index) => (
          <div key={index} className={`flex ${msg.role === 'user' ? 'justify-end' : 'justify-start'}`}>
            <div className={`max-w-[80%] p-3 rounded-lg text-sm ${msg.role === 'user' ? 'bg-blue-600 text-white rounded-br-none' : 'bg-gray-200 dark:bg-slate-700 text-slate-800 dark:text-gray-100 rounded-bl-none'}`}>
              {msg.text}
            </div>
          </div>
        ))}
        {isLoading && (
          <div className="flex justify-start">
            <div className="max-w-[80%] p-3 rounded-lg text-sm bg-gray-200 dark:bg-slate-700 flex gap-1 items-center rounded-bl-none">
              <span className="w-2 h-2 rounded-full bg-gray-400 animate-bounce"></span>
              <span className="w-2 h-2 rounded-full bg-gray-400 animate-bounce" style={{ animationDelay: '0.2s' }}></span>
              <span className="w-2 h-2 rounded-full bg-gray-400 animate-bounce" style={{ animationDelay: '0.4s' }}></span>
            </div>
          </div>
        )}
      </div>

      <form onSubmit={handleSend} className="p-3 border-t border-gray-200 dark:border-gray-700 bg-white dark:bg-slate-800 flex gap-2">
        <input 
          type="text"
          value={input}
          onChange={(e) => setInput(e.target.value)}
          placeholder="Digite sua mensagem..."
          className="flex-1 bg-gray-100 dark:bg-slate-700 text-slate-800 dark:text-gray-100 rounded-md px-3 py-2 outline-none focus:ring-2 focus:ring-blue-500"
          disabled={isLoading}
        />
        <button 
          type="submit"
          disabled={!input.trim() || isLoading}
          className="bg-blue-600 hover:bg-blue-700 disabled:opacity-50 text-white p-2 rounded-md transition-colors"
        >
          <Send className="h-5 w-5" />
        </button>
      </form>
    </div>
  );
}

