import { KanbanCard } from './KanbanCard.jsx';

export function KanbanColumn({ statusId, title, count, badgeClass, tasks, onTaskClick, onDropColumn }) {
  const handleDragOver = (e) => e.preventDefault();
  
  const handleDrop = (e) => {
    e.preventDefault();
    if (onDropColumn) onDropColumn(e, statusId);
  };

  return (
    <div 
      className="flex-1 min-w-[300px] max-w-sm rounded-lg border border-gray-200 dark:border-gray-800 bg-gray-100 dark:bg-surface-dark flex flex-col"
      onDragOver={handleDragOver}
      onDrop={handleDrop}
    >
      <div className="p-4 border-b border-gray-200 dark:border-gray-800 font-semibold flex items-center justify-between">
        {title}
        <span className={`${badgeClass} text-xs py-1 px-2 rounded-full`}>{count}</span>
      </div>
      <div className="p-4 flex-1 space-y-3">
        {tasks.map(task => (
          <KanbanCard key={task.id} task={task} onClick={onTaskClick} />
        ))}
      </div>
    </div>
  );
}

