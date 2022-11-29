package FileSharing.dao.impl.jdbc;

import FileSharing.dao.FileDao;
import FileSharing.dto.File;
import FileSharing.exceptions.DataProcessingException;
import FileSharing.lib.Dao;
import FileSharing.util.ConnectionUtil;

import java.nio.file.Path;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Dao
public class FileDaoJdbcImpl implements FileDao {

    @Override
    public File create(File file) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String query = "INSERT INTO files (file_name, file_extension, file_data, file_path) VALUES (?, ?, ?, ?);";
            CallableStatement statement = connection.prepareCall(query);
            statement.setString(1, file.getName());
            statement.setString(2, file.getExtension());
            statement.setBytes(3, file.getFileDataByteArray());
            statement.setString(4, file.getPath().toString());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            while (resultSet.next()) {
                long fileId = resultSet.getLong(1);
                file.setId(fileId);
            }
            return file;
        } catch (SQLException e) {
            throw new DataProcessingException("It is impossible to create a file: "
                    + file, e);
        }
    }

    @Override
    public Optional<File> getById(Long id) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String query = "SELECT * FROM files WHERE file_id = ?;";
            CallableStatement statement = connection.prepareCall(query);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                File file = createFileFromResultSet(resultSet);
                return Optional.of(file);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DataProcessingException("Get file with id "
                    + id + " is failed", e);
        }
    }

    @Override
    public File getFileExtensionById(String id) {
        return null;
    }

    @Override
    public List<File> getAll() {
        return null;
    }

    @Override
    public File update(File item) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    private File createFileFromResultSet(ResultSet resultSet) throws SQLException {
        long fileId = resultSet.getLong("file_id");
        String name = resultSet.getString("file_name");
        String extension = resultSet.getString("file_extension");
        String path = resultSet.getString("file_path");
        byte[] data = resultSet.getBytes("file_data");
        return File.builder()
                .id(fileId)
                .name(name)
                .extension(extension)
                .path(Path.of(path))
                .fileDataByteArray(data).build();
    }
}
