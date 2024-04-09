package org.icmss.icmsscarservice.domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileRepresentation {

    private String fileName;

    private Long size;

    private String type;

    private MultipartFile file;

    public static FileRepresentation of(String fileName, @NotNull MultipartFile file) {
        return FileRepresentation.builder()
                .fileName(fileName)
                .file(file)
                .size(file.getSize())
                .type(file.getContentType())
                .build();
    }

}
