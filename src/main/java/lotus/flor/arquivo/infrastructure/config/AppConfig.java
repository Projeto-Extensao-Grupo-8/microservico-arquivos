package lotus.flor.arquivo.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lotus.flor.arquivo.application.port.FileUploadPublisherPort;
import lotus.flor.arquivo.application.port.S3StoragePort;
import lotus.flor.arquivo.application.usecase.GetFileByNameUseCase;
import lotus.flor.arquivo.application.usecase.GetProfilePhotoUseCase;
import lotus.flor.arquivo.application.usecase.UploadFileUseCase;
import lotus.flor.arquivo.infrastructure.aws.S3DownloadAdapter;
import lotus.flor.arquivo.infrastructure.aws.S3StorageAdapter;
import lotus.flor.arquivo.infrastructure.messaging.RabbitMQPublisherAdapter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@Configuration
public class AppConfig {

    // ── Upload ────────────────────────────────────────────────────────────────

    @Bean
    public UploadFileUseCase uploadFileUseCase(FileUploadPublisherPort publisherPort) {
        return new UploadFileUseCase(publisherPort);
    }

    @Bean
    public FileUploadPublisherPort publisherPort(
            RabbitTemplate rabbitTemplate,
            ObjectMapper objectMapper) {
        return new RabbitMQPublisherAdapter(
                rabbitTemplate,
                objectMapper,
                RabbitConfig.EXCHANGE,
                RabbitConfig.ROUTING_KEY
        );
    }

    // ── Storage ───────────────────────────────────────────────────────────────

    @Bean
    public S3StoragePort s3StoragePort(
            S3AsyncClient s3AsyncClient,
            @Value("${aws.s3.bucket}") String bucket) {
        return new S3StorageAdapter(s3AsyncClient, bucket);
    }

    // ── Download ──────────────────────────────────────────────────────────────

    @Bean
    public S3DownloadAdapter s3DownloadAdapter(
            S3AsyncClient s3AsyncClient,
            @Value("${aws.s3.bucket}") String bucket) {
        return new S3DownloadAdapter(s3AsyncClient, bucket);
    }

    @Bean
    public GetProfilePhotoUseCase getProfilePhotoUseCase(S3DownloadAdapter s3DownloadAdapter) {
        return new GetProfilePhotoUseCase(s3DownloadAdapter);
    }

    @Bean
    public GetFileByNameUseCase getFileByNameUseCase(S3DownloadAdapter s3DownloadAdapter) {
        return new GetFileByNameUseCase(s3DownloadAdapter);
    }
}