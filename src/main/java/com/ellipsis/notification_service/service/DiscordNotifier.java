package com.ellipsis.notification_service.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class DiscordNotifier {

    @Value("${discord.webhook.url}")
    private String webhookUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public void send(String message) {
        try {
            restTemplate.postForEntity(
                    webhookUrl,
                    Map.of("content", message),
                    String.class
            );
        } catch (Exception e) {
            System.out.println("Failed to send Discord notification: " + e.getMessage());
        }
    }
}
