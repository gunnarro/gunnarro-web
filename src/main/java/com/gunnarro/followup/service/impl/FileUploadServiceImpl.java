package com.gunnarro.followup.service.impl;

import com.gunnarro.followup.domain.log.ImageResource;
import com.gunnarro.followup.service.FileUploadService;
import com.gunnarro.followup.service.exception.UploadFileException;
import com.gunnarro.followup.service.exception.UploadFileNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Service
public class FileUploadServiceImpl implements FileUploadService {

    private static final Logger LOG = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    @Autowired
    private Environment environment;

    private Path rootLocation;

    @PostConstruct
    public void init() {
        try {
            this.rootLocation = Paths.get(environment.getProperty("fileupload.root.dir"));
            Files.createDirectories(rootLocation);
            LOG.debug("root dir: {}", rootLocation.toString());
            System.out.println(rootLocation.toString());
        } catch (Exception e) {
            LOG.error("Error init root dir: {}, error: {}", environment.getProperty("fileupload.root.dir"), e);
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
            LOG.debug("Store file: {}", userDir.resolve(filename));
            Files.copy(file.getInputStream(), userDir.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            LOG.error(null, e);
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
                LOG.debug("deleted: {}", id);
            } else {
                LOG.error("error deleting {}", id);
            }
        } catch (IOException e) {
            LOG.error(null, e);
        }
    }

    @Override
    public Stream<Path> loadAll(String id) {
        try {
            Path userImageDir = getUserImageDir(id);
            return Files.walk(userImageDir, 1).filter(path -> !path.equals(userImageDir))
                    .map(userImageDir::relativize);
        } catch (Exception e) {
            LOG.error("root path: {}", this.rootLocation.toAbsolutePath());
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
            LOG.debug("created images dir: {}", id);
        }
        return userDir;
    }
}
