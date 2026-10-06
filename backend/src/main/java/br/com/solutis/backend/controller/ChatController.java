package br.com.solutis.backend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.solutis.backend.service.ChatService;
import br.com.solutis.backend.dto.request.*;
import br.com.solutis.backend.dto.response.*;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping ("/chat")
@RequiredArgsConstructor 
public class ChatController {

    private final ChatService chatService;

    @PostMapping 
    public ChatResponseDTO chat(ChatRequestDTO request){
        return chatService.chat(request);
    }

}
