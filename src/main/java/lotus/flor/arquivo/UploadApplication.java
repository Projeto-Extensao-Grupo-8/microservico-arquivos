package lotus.flor.arquivo;

import lotus.flor.arquivo.application.usecase.UploadFileUseCase;
import lotus.flor.arquivo.infrastructure.aws.S3StorageAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@SpringBootApplication
public class UploadApplication {

	public static void main(String[] args) {
		SpringApplication.run(UploadApplication.class, args);
	}

	@Bean
	public S3AsyncClient s3AsyncClient() {
		return S3AsyncClient.builder().build();
	}

	@Bean
	public UploadFileUseCase uploadFileUseCase(S3AsyncClient client) {
		return new UploadFileUseCase(
				new S3StorageAdapter(client, "meu-bucket")
		);
	}
}
