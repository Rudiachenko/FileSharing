package FileSharing.dao;

import FileSharing.dto.File;

public interface FileDao extends GenericDao<File, Long> {

    File getFileExtensionById(String id);

}
