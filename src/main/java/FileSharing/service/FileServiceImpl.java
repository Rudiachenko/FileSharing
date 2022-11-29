package FileSharing.service;

import org.apache.commons.lang3.StringUtils;
import FileSharing.dao.FileDao;
import FileSharing.dto.File;
import FileSharing.lib.Inject;
import FileSharing.lib.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;

@Service
public class FileServiceImpl implements FileService {
    @Inject
    private FileDao fileDao;

    public File saveFile(Path path) {
        File fileToSave = createFileToSave(path);
        return fileDao.create(fileToSave);
    }

    @Override
    public File getFileById(Long id) {
        return fileDao.getById(id).orElseThrow(() ->
                new NoSuchElementException("File with id " + id + " not found"));
    }

    public static void saveFileLocally(File file) {
        try {
            byte[] fileDataByteArray = file.getFileDataByteArray();
            Path path = Files.write(file.getPath(), fileDataByteArray);
            System.out.printf("File successfully saved locally: %s %n", path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File createFileToSave(Path path) {
        String fileName = path.getFileName().toString();
        String fileExtension = getFileExtension(fileName);
        return File.builder()
                .name(fileName)
                .path(path)
                .extension(fileExtension)
                .fileDataByteArray(getFileDataByteArray(path))
                .build();
    }

    private byte[] getFileDataByteArray(Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new IllegalArgumentException("Can't find file to represent in byte array", e);
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return StringUtils.EMPTY;
        }
    }
}
