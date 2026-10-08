export function KanbanCard({ task, onClick }) {
  return (
    <div 
      onClick={() => onClick && onClick(task)}
      className="p-4 rounded-md bg-white dark:bg-slate-700 shadow-sm border border-gray-200 dark:border-gray-600 cursor-pointer hover:border-blue-500 transition-colors"
    >
      <h3 className="font-medium text-slate-800 dark:text-gray-100">{task.title}</h3>
      <p className="text-sm text-gray-500 dark:text-gray-400 mt-1">{task.description}</p>
    </div>
  );
}

