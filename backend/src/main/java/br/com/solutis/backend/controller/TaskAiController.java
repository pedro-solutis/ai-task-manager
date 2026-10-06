package br.com.solutis.backend.controller;

import br.com.solutis.backend.dto.request.*;
import br.com.solutis.backend.dto.response.*;
import br.com.solutis.backend.facade.ai.*;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping ("ai/tasks")
@RequiredArgsConstructor 
public class TaskAiController {

    private final TaskAiFacade taskAiFacade;
    
    @PostMapping ("/{id}/enhance")
    public ResponseEntity<TaskEnhancedResponseDTO> enhanceById(@PathVariable("id") UUID taskId) {
        return ResponseEntity.ok(taskAiFacade.enhanceById(taskId));
    }

    @PostMapping ("/{id}/analyze")
    public ResponseEntity<TaskAnalysisResponseDTO> analyzeById(@PathVariable("id") UUID taskId) {
        return ResponseEntity.ok(taskAiFacade.analyzeById(taskId));
    }

    @PostMapping ("/{id}/decompose")
    public ResponseEntity<List<TaskResponseDTO>> decomposeById(@PathVariable("id") UUID taskId) {
        return ResponseEntity.ok(taskAiFacade.decomposeById(taskId));
    }

    @PostMapping ("/enhance")
    public ResponseEntity<TaskEnhancedResponseDTO> enhancePreview(@RequestBody @Valid TaskEnhanceRequestDTO dto) {
        return ResponseEntity.ok(taskAiFacade.enhancePreview(dto));
    }

    @PostMapping ("/analyze")
    public ResponseEntity<TaskAnalysisResponseDTO> analyzePreview(@RequestBody @Valid TaskAnalysisRequestDTO dto) {
        return ResponseEntity.ok(taskAiFacade.analyzePreview(dto));
    }

    @PostMapping ("/decompose")
    public ResponseEntity<TaskDecomposeResponseDTO> decomposePreview(@RequestBody @Valid TaskDecomposeRequestDTO dto) {
        return ResponseEntity.ok(taskAiFacade.decomposePreview(dto));
    }

}
