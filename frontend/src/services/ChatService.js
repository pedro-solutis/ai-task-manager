import api from './api';

export const ChatService = {
  // POST /chat body=chatId, message
  sendMessage: async (chatId, message) => {
    const response = await api.post('/chat', {
      chatId,
      message
    });
    return response.data;
  }
};

