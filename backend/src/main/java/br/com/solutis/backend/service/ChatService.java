package br.com.solutis.backend.service;

import br.com.solutis.backend.dto.response.*;
import br.com.solutis.backend.dto.request.*;
import br.com.solutis.backend.strategy.ai.*;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor  
public class ChatService {

    private final ChatClientFactory chatClientFactory;

    public ChatResponseDTO chat(ChatRequestDTO requestDTO) {
        return chatClientFactory.chat(requestDTO);
    }

}
