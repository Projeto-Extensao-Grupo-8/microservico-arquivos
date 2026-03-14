package lotus.flor.arquivo.domain.model;

import lombok.Getter;

@Getter
public class FileUpload {

    private final String fileName;
    private final byte[] content;
    private final String contentType;

    public FileUpload(String fileName, byte[] content, String contentType) {
        this.fileName = fileName;
        this.content = content;
        this.contentType = contentType;
    }

}
