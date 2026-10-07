import api from './api';

export const ChatService = {
  // POST /chat body=chatId, prompt
  sendMessage: async (chatId, prompt) => {
    const response = await api.post('/chat', {
      chatId,
      prompt
    });
    return response.data;
  }
};

