package lotus.flor.arquivo.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lotus.flor.arquivo.application.port.FileUploadPublisherPort;
import lotus.flor.arquivo.domain.model.FileUpload;
import lotus.flor.arquivo.infrastructure.messaging.dto.FileUploadMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.concurrent.CompletableFuture;

public class RabbitMQPublisherAdapter implements FileUploadPublisherPort {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final String exchange;
    private final String routingKey;

    public RabbitMQPublisherAdapter(
            RabbitTemplate rabbitTemplate,
            ObjectMapper objectMapper,
            String exchange,
            String routingKey
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    @Override
    public CompletableFuture<String> publish(FileUpload fileUpload) {

        return CompletableFuture.supplyAsync(() -> {
            try {
                FileUploadMessage dto = new FileUploadMessage();
                dto.setFileName(fileUpload.getFileName());
                dto.setContent(fileUpload.getContent());
                dto.setContentType(fileUpload.getContentType());

                String payload = objectMapper.writeValueAsString(dto);

                rabbitTemplate.convertAndSend(exchange, routingKey, payload);

                return fileUpload.getFileName();
            } catch (Exception e) {
                throw new RuntimeException("Error publishing message", e);
            }
        });
    }
}