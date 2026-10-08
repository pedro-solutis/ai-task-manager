import api from './api';

export const TaskService = {
  // GET /tasks?sort&direction
  findAll: async (sort = 'dueDate', direction = 'DESC') => {
    const response = await api.get('/tasks', {
      params: { sort, direction }
    });
    return response.data;
  },

  // GET /tasks/{id}
  findById: async (id) => {
    const response = await api.get(`/tasks/${id}`);
    return response.data;
  },

  // POST /tasks
  create: async (taskData) => {
    const response = await api.post('/tasks', taskData);
    return response.data;
  },

  // PUT /tasks/{id} body = title, description, prioritym dueDate
  update: async (id, taskData) => {
    const response = await api.put(`/tasks/${id}`, taskData);
    return response.data;
  },

  // PATCH /tasks/{id} body=status
  updateStatus: async (id, status) => {
    const response = await api.patch(`/tasks/${id}/status`, { status });
    return response.data;
  },

  // PATCH /tasks/{id} body=parentTasks
  updateParent: async (id, parentTaskId) => {
    const response = await api.patch(`/tasks/${id}/parent`, { parentTaskId });
    return response.data;
  },

  // DELETE /tasks/{id}
  delete: async (id) => {
    const response = await api.delete(`/tasks/${id}`);
    return response.data;
  }
};

