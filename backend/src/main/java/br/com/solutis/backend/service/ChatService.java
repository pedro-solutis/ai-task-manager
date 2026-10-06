package br.com.solutis.backend.service;

import br.com.solutis.backend.dto.response.*;
import br.com.solutis.backend.dto.request.*;
import br.com.solutis.backend.ai.TaskAiService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor  
public class ChatService {

    private final TaskAiService taskAiService;

    public ChatResponseDTO chat(ChatRequestDTO requestDTO) {
        return taskAiService.chat(requestDTO);
    }

}
