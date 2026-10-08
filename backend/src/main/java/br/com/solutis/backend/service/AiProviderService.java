package br.com.solutis.backend.service;

import org.springframework.stereotype.Service;
import lombok.Getter;

@Service
@Getter
public class AiProviderService {
    
    private String activeProvider = "gemini";

    public void setActiveProvider(String provider) {
        if (provider != null && !provider.trim().isEmpty()) {
            this.activeProvider = provider.toLowerCase();
        }
    }
}

