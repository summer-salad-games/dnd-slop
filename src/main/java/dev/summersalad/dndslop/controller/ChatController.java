package dev.summersalad.dndslop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.summersalad.dndslop.client.LLMClient;
import dev.summersalad.dndslop.model.AdventureProgress;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ObjectMapper objectMapper;
    private final LLMClient llmClient;

    @GetMapping("/")
    public String chat() {
        return "home-view";
    }

    @PostMapping("/adventure")
    public String handleChat(
            @RequestParam(required = false) final String choice,
            final HttpSession session,
            final Model model
    ) {
        if (choice == null || choice.isBlank()) {
            session.setAttribute("conversationId", UUID.randomUUID().toString());
            callModel("start", model, session.getAttribute("conversationId").toString());
        } else {
            callModel(choice, model, (String) session.getAttribute("conversationId"));
        }
        model.addAttribute("sessionId", session.getAttribute("conversationId").toString());
        return "adventure-view";
    }

    @SneakyThrows
    private void callModel(final String message, final Model model, final String conversationId) {
        final String response = llmClient.call(message, conversationId);
        final var adventureProgress = objectMapper.readValue(response, AdventureProgress.class);
        model.addAttribute("response", adventureProgress);
    }
}
