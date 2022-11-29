package FileSharing.service;

import FileSharing.dto.File;

import java.nio.file.Path;

public interface FileService {
    File saveFile(Path path);

    File getFileById(Long id);
}
