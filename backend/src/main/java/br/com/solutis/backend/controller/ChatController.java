package br.com.solutis.backend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.solutis.backend.service.ChatService;
import jakarta.validation.Valid;
import br.com.solutis.backend.dto.request.*;
import br.com.solutis.backend.dto.response.*;
import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Chat")
@RestController 
@RequestMapping ("/chat")
@RequiredArgsConstructor 
public class ChatController {

    private final ChatService chatService;

    @Operation(summary = "Send a chat message")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @PostMapping 
    public ChatResponseDTO chat(@RequestBody @Valid ChatRequestDTO request){
        return chatService.chat(request);
    }

}
