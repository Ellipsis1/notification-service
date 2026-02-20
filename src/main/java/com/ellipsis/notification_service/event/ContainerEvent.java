package com.ellipsis.notification_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContainerEvent {
    private String containerId;
    private String containerName;
    private String previousStatus;
    private String currentStatus;
    private LocalDateTime occurredAt;
    private String eventType;
}