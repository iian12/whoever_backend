package com.jygoh.whoever.domain.image;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ImageUploadResponse {

    private String url;

    @Builder
    public ImageUploadResponse(String url) {
        this.url = url;
    }
}
