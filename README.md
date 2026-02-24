# notification-service

A Spring Boot microservice that consumes Docker container events from Apache Kafka and sends formatted Discord webhook notifications. Intentionally decoupled from the monitoring layer.

## Architecture

This service is the alerting half of an event-driven homelab monitoring platform. It knows nothing about Docker — it only reacts to events published by [`homelab-monitor`](https://github.com/Ellipsis1/homelab-monitor).

```
homelab-monitor → Kafka Topic: container-events → notification-service → Discord
```

This decoupling means:
- Additional consumers can be added (Slack, email, PagerDuty) without touching the monitor
- The notification service can go down and restart without affecting monitoring or data persistence
- Each service can be deployed, updated, and scaled independently

## Tech Stack

- **Java 17** with Spring Boot 3.x
- **Apache Kafka** — event consumption via `@KafkaListener`
- **Discord Webhooks** — outbound notifications via HTTP

## Event Handling

Listens on Kafka topic `container-events` in consumer group `notification-group`.

Handles the following event types:

| Event | Discord Message |
|-------|----------------|
| `CONTAINER_DOWN` | Container is down with previous/current status |
| `CONTAINER_UP` | Container is back up |
| `CONTAINER_RESTARTED` | Manual restart notification |
| `CONTAINER_STOPPED` |  Manual stop notification |

Each notification includes a direct link to the container's detail page on the dashboard at `monitor.ellipsis.local`.

## Configuration

```properties
spring.kafka.bootstrap-servers=${KAFKA_BOOTSTRAP_SERVERS:localhost:9092}
discord.webhook.url=${DISCORD_WEBHOOK_URL:}
dashboard.url=${DASHBOARD_URL:http://localhost:3002}
```

## Docker

```bash
docker buildx build --platform linux/arm64 \
  -t ghcr.io/ellipsis1/notification-service:latest --push .
```

## Deployment

Deployed via Jenkins CI/CD pipeline. See [`homelab-monitor`](https://github.com/Ellipsis1/homelab-monitor) for full infrastructure details.