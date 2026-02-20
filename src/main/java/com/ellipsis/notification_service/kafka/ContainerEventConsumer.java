package com.ellipsis.notification_service.kafka;

import com.ellipsis.notification_service.event.ContainerEvent;
import com.ellipsis.notification_service.service.DiscordNotifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ContainerEventConsumer {

    private final DiscordNotifier discordNotifier;

    public ContainerEventConsumer(DiscordNotifier discordNotifier) {
        this.discordNotifier = discordNotifier;
    }

    @KafkaListener(topics = "container-events", groupId = "notification-group")
    public void consume(ContainerEvent event) {
        String message = buildMessage(event);
        System.out.println("Received event: " + event.getEventType()
        + " for " + event.getContainerName());
        discordNotifier.send(message);
    }

    private String buildMessage(ContainerEvent event) {
        String dashboardUrl = "http://192.168.1.173:3000/containers/"
                + event.getContainerName();

        return switch (event.getEventType()) {
            case "CONTAINER_DOWN" -> "**" + event.getContainerName()
                    + "** is down!\nStatus: " + event.getCurrentStatus()
                    + "\nPrevious: " + event.getPreviousStatus()
                    + "\nView Dashboard: " + dashboardUrl;
            case "CONTAINER_UP" -> "**" + event.getContainerName()
                    + "** is back up!\nStatus: " + event.getCurrentStatus();
            case "CONTAINER_RESTARTED" -> "**" + event.getContainerName()
                    + "** was restarted manually.\n";
            case "CONTAINER_STOPPED" -> "**" + event.getContainerName() + "** was stopped manually."
                    + "\nView Dashboard: " + dashboardUrl;
            default -> "**" + event.getContainerName()
                    + "** status changed to: " + event.getCurrentStatus()
                    + "\nView Dashboard: " + dashboardUrl;
        };
    }
}
