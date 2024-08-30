package com.jygoh.whoever.domain.image;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String uploadImage(MultipartFile file) throws IOException;

}
