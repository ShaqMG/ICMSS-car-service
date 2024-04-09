package org.icmss.icmsscarservice.domain.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MediaResponse {

    private Long id;

    private String name;

    private String type;

    private Long size;

    private String url;

}
