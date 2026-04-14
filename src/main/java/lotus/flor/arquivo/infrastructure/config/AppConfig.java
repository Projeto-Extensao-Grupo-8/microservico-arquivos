package lotus.flor.arquivo.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lotus.flor.arquivo.application.port.FileUploadPublisherPort;
import lotus.flor.arquivo.application.port.S3StoragePort;
import lotus.flor.arquivo.application.usecase.UploadFileUseCase;
import lotus.flor.arquivo.infrastructure.messaging.RabbitMQPublisherAdapter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public UploadFileUseCase uploadFileUseCase(FileUploadPublisherPort publisherPort) {
        return new UploadFileUseCase(publisherPort);
    }

    @Bean
    public FileUploadPublisherPort publisherPort(
            RabbitTemplate rabbitTemplate,
            ObjectMapper objectMapper
    ) {
        return new RabbitMQPublisherAdapter(
                rabbitTemplate,
                objectMapper,
                RabbitConfig.EXCHANGE,
                RabbitConfig.ROUTING_KEY
        );
    }
}