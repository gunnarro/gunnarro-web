package com.gunnarro.web.service.impl;

import com.gunnarro.web.domain.log.ImageResource;
import com.gunnarro.web.service.FileUploadService;
import com.gunnarro.web.service.exception.UploadFileException;
import com.gunnarro.web.service.exception.UploadFileNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Autowired
    private Environment environment;

    private Path rootLocation;

    @PostConstruct
    public void init() {
        try {
            this.rootLocation = Paths.get(environment.getProperty("fileupload.root.dir"));
            Files.createDirectories(rootLocation);
            log.debug("root dir: {}", rootLocation.toString());
            System.out.println(rootLocation.toString());
        } catch (Exception e) {
            log.error("Error init root dir: {}, error: {}", environment.getProperty("fileupload.root.dir"), e);
            throw new UploadFileException("Could not initialize storage", e);
        }
    }

    @Override
    public void store(MultipartFile file, String id, String description) {
        try {
            if (file.isEmpty()) {
                throw new UploadFileException("Failed to store empty file!");
            }
            if (Objects.requireNonNull(file.getOriginalFilename()).contains("..")) {
                // This is a security check
                throw new UploadFileException(
                        "Cannot store file with relative path outside current directory " + file.getOriginalFilename());
            }
            Path userDir = getUserImageDir(id);
            String filename = StringUtils.cleanPath(file.getOriginalFilename());
            log.debug("Store file: {}", userDir.resolve(filename));
            Files.copy(file.getInputStream(), userDir.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            log.error(null, e);
            throw new UploadFileException("Failed to store file!", e);
        }
    }

    @Override
    public List<ImageResource> getImages(String id) {
        Path userDir = Paths.get(rootLocation.toString() + "/" + id);
        if (!Files.exists(userDir)) {
            return null;
        }

        /**
         List<Path> result;
         try (Stream<Path> walk = Files.walk(path)) {
         result = walk.filter(Files::isRegularFile)
         .collect(Collectors.toList());
         }
         return result;
         */
        try {
            return Files
                    .walk(userDir, 1).filter(path -> !path.equals(userDir)).map(path -> ImageResource.builder().id(id).name(path.toFile().getName()).path(this.rootLocation.relativize(path).toString()).build())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new UploadFileException("Failed to read stored files", e);
        }
    }

    @Override
    public void deleteImage(String id, String fileName) {
        try {
            String path = String.format("%s/%s", getUserImageDir(id), fileName);
            if (new File(path).delete()) {
                log.debug("deleted: {}", id);
            } else {
                log.error("error deleting {}", id);
            }
        } catch (IOException e) {
            log.error(null, e);
        }
    }

    @Override
    public Stream<Path> loadAll(String id) {
        try {
            Path userImageDir = getUserImageDir(id);
            return Files.walk(userImageDir, 1).filter(path -> !path.equals(userImageDir))
                    .map(userImageDir::relativize);
        } catch (Exception e) {
            log.error("root path: {}", this.rootLocation.toAbsolutePath());
            throw new UploadFileException("Failed to read stored files", e);
        }
    }

    @Override
    public Resource loadAsResource(String id, String filename) {
        try {
            Path file = getUserImageDir(id).resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new UploadFileNotFoundException("Could not read file: " + id + ", " + filename);

            }
        } catch (Exception e) {
            throw new UploadFileNotFoundException("Could not read file: " + id + ", " + filename, e);
        }
    }

    @Override
    public void deleteAll(String id) {
        try {
            FileSystemUtils.deleteRecursively(getUserImageDir(id).toFile());
        } catch (IOException e) {
            throw new UploadFileNotFoundException("Could not delete all files: " + id);
        }
    }

    private Path getUserImageDir(String id) throws IOException {
        Path userDir = Paths.get(rootLocation.toString() + "/" + id);
        if (!Files.exists(userDir)) {
            Files.createDirectories(userDir);
            log.debug("created images dir: {}", id);
        }
        return userDir;
    }
}
