package br.com.solutis.backend.service;

import br.com.solutis.backend.dto.response.*;
import br.com.solutis.backend.dto.request.*;
import br.com.solutis.backend.adapter.ai.AiModelAdapter;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor  
public class ChatService {

    private final AiModelAdapter aiModelAdapter;

    public ChatResponseDTO chat(ChatRequestDTO requestDTO) {
        return aiModelAdapter.chat(requestDTO.chatId(), requestDTO.prompt());
    }

}
