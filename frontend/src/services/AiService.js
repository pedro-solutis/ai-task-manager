import api from './api';

export const AiService = {
  // POST /ai/tasks/{id}/enhance
  enhanceById: async (id) => {
    const response = await api.post(`/ai/tasks/${id}/enhance`);
    return response.data;
  },

  // POST /ai/tasks/{id}/analyze
  analyzeById: async (id) => {
    const response = await api.post(`/ai/tasks/${id}/analyze`);
    return response.data;
  },

  // POST /ai/tasks/{id}/decompose
  decomposeById: async (id) => {
    const response = await api.post(`/ai/tasks/${id}/decompose`);
    return response.data;
  },

  // POST /ai/tasks/enhance
  enhancePreview: async (dto) => {
    const response = await api.post('/ai/tasks/enhance', dto);
    return response.data;
  },

  // POST /ai/tasks/analyze
  analyzePreview: async (dto) => {
    const response = await api.post('/ai/tasks/analyze', dto);
    return response.data;
  },

  // POST /ai/tasks/decompose
  decomposePreview: async (dto) => {
    const response = await api.post('/ai/tasks/decompose', dto);
    return response.data;
  }
};

