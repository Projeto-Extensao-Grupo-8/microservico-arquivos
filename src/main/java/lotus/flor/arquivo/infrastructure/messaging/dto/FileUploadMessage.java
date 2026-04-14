package lotus.flor.arquivo.infrastructure.messaging.dto;

import lombok.Data;

@Data
public class FileUploadMessage {
    private String fileName;
    private byte[] content;
    private String contentType;
}