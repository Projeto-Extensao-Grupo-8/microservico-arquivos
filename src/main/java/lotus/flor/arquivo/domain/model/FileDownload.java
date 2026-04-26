package lotus.flor.arquivo.domain.model;

import lombok.Getter;

@Getter
public class FileDownload {

    private final String fileName;
    private final byte[] content;
    private final String contentType;

    public FileDownload(String fileName, byte[] content, String contentType) {
        this.fileName = fileName;
        this.content = content;
        this.contentType = contentType;
    }
}
