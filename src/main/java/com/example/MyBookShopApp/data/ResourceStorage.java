package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.storage.BookFileRepository;
import com.example.MyBookShopApp.struct.book.file.BookFileEntity;
import liquibase.util.file.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

@Service
public class ResourceStorage {
    @Value("${upload.path}")
    String uploadPath;

    @Value("${download.path}")
    String downloadPath;

    BookFileRepository bookFileRepository;


    @Autowired
    public ResourceStorage(BookFileRepository bookFileRepository) {
        this.bookFileRepository = bookFileRepository;
    }

    public String saveNewBookImage(MultipartFile file, String slug) throws IOException {
        String resourceURI = null;
        if (!file.isEmpty()) {
            if (!new File(uploadPath).exists()) {
                Files.createDirectories(Paths.get(uploadPath));

            }
            Logger.getLogger(ResourceStorage.class.getName()).info(uploadPath);

            String fileName = slug + "." + FilenameUtils.getExtension(file.getOriginalFilename());
            Path path = Paths.get(uploadPath, fileName);
            resourceURI = "/images/" + fileName;
            file.transferTo(path);
        }
        return resourceURI;
    }

    public Path getBookFilePath(String hash) {
        BookFileEntity bookFile = bookFileRepository.findBookByHash(hash);
        return Paths.get(bookFile.getPath());
    }

    public MediaType getBookFileMime(String hash) {
        BookFileEntity bookFile = bookFileRepository.findBookByHash(hash);
        String mimeType = URLConnection.guessContentTypeFromName(Paths.get(bookFile.getPath()).getFileName().toString());
        if (mimeType != null) {
            return MediaType.parseMediaType(mimeType);
        }
        return MediaType.APPLICATION_OCTET_STREAM;
    }

    public byte[] getBookFileByteArray(String hash) throws IOException {
        BookFileEntity bookFile = bookFileRepository.findBookByHash(hash);
        Path path = Paths.get(downloadPath, bookFile.getPath());
        return Files.readAllBytes(path);
    }
}
