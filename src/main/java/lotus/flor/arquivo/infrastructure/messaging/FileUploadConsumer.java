package lotus.flor.arquivo.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lotus.flor.arquivo.application.port.S3StoragePort;
import lotus.flor.arquivo.domain.model.FileUpload;
import lotus.flor.arquivo.infrastructure.messaging.dto.FileUploadMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FileUploadConsumer {

    private final ObjectMapper objectMapper;
    private final S3StoragePort storagePort;

    public FileUploadConsumer(ObjectMapper objectMapper,
                              S3StoragePort storagePort) {
        this.objectMapper = objectMapper;
        this.storagePort = storagePort;
    }

    @RabbitListener(queues = "file.upload.queue")
    public void consume(String message) {

        try {
            FileUploadMessage dto =
                    objectMapper.readValue(message, FileUploadMessage.class);

            FileUpload file = new FileUpload(
                    dto.getFileName(),
                    dto.getContent(),
                    dto.getContentType()
            );

            storagePort.upload(file);

        } catch (Exception e) {
            throw new RuntimeException("Error processing message", e);
        }
    }
}