package FileSharing;

import FileSharing.dto.File;
import FileSharing.lib.Injector;
import FileSharing.service.FileService;
import FileSharing.util.ConnectionUtil;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Objects;

public class App {
    private static final Injector injector = Injector.getInstance("FileSharing");
    private static final String PATH_TO_CAT = "src/main/resources/cat.jpg";
    private static final String PATH_TO_DOG = "src/main/resources/dog.png";
    private static final Long ID_1 = 1L;
    private static final Long ID_2 = 2L;

    public static void main(String[] args) {
        createTable();

        FileService fileService = (FileService) injector.getInstance(FileService.class);

        File savedImageCat = fileService.saveFile(Path.of(PATH_TO_CAT));
        File savedImageDog = fileService.saveFile(Path.of(PATH_TO_DOG));

        File retrievedImageCat = fileService.getFileById(ID_1);
        File retrievedImageDog = fileService.getFileById(ID_2);

        System.out.println("- Is saved image equals to retrieved image: " + Objects.equals(savedImageCat, retrievedImageCat));

        System.out.println("- Is saved image equals to retrieved image: " + Objects.equals(savedImageDog, retrievedImageDog));
    }

    private static void createTable() {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String createFileTable = "CREATE TABLE files" +
                    "    (`file_id` BIGINT(11)   NOT NULL AUTO_INCREMENT," +
                    "    `file_name`       VARCHAR(225) NOT NULL," +
                    "    `file_extension`       VARCHAR(225) NOT NULL," +
                    "    `file_path`       VARCHAR(225) NOT NULL," +
                    "    `file_data`      LONGBLOB   NOT NULL," +
                    "    `deleted`    TINYINT(11)  NULL DEFAULT FALSE," +
                    "    PRIMARY KEY (`file_id`));";
            PreparedStatement statement = connection.prepareStatement(createFileTable);
            statement.execute();
        } catch (Exception e) {
            System.out.println("Fail while creating table");
        }
    }
}
