package com.gunnarro.followup.service;

import com.gunnarro.followup.domain.log.ImageResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface FileUploadService {

    Stream<Path> loadAll(String id);

    Resource loadAsResource(String id, String filename);

    void deleteAll(String id);

    void store(MultipartFile file, String id, String description);

    List<ImageResource> getImages(String id);

    void deleteImage(String id, String path);
}
