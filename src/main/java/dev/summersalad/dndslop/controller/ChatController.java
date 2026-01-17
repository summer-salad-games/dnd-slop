package dev.summersalad.dndslop.controller;

import dev.summersalad.dndslop.client.LLMClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final LLMClient llmClient;

    @GetMapping("/")
    public String chat() {
        return "home-view";
    }

    @PostMapping("/adventure")
    public String handleChat(
            @RequestParam(required = false) final String message,
            final Model model
    ) {
        if (message == null || message.isBlank()) {
            callModel("start", model);
        } else {
            callModel(message, model);
        }
        return "adventure-view";
    }

    private void callModel(final String message, final Model model) {
        final String response = llmClient.call(message, "id-114");
        model.addAttribute("response", response);
    }
}
