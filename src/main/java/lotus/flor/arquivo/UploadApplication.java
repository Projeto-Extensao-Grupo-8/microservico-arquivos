package lotus.flor.arquivo;

import lotus.flor.arquivo.application.port.FileUploadPublisherPort;
import lotus.flor.arquivo.application.usecase.UploadFileUseCase;
import lotus.flor.arquivo.infrastructure.aws.S3StorageAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@SpringBootApplication
public class UploadApplication {

	public static void main(String[] args) {
		SpringApplication.run(UploadApplication.class, args);
	}

	@Bean
	public S3AsyncClient s3AsyncClient(@Value("${aws.s3.region}") String region) {
		return S3AsyncClient.builder()
				.region(Region.of(region))
				.build();
	}

	@Bean
	public UploadFileUseCase uploadFileUseCase(S3AsyncClient client,
			@Value("${aws.s3.bucket}") String bucket) {
		return new UploadFileUseCase(
                (FileUploadPublisherPort) new S3StorageAdapter(client, bucket)
        );
	}
}
