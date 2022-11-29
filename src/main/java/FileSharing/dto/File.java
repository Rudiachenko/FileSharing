package FileSharing.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.nio.file.Path;

@ToString
@EqualsAndHashCode
@Builder
@Getter
@Setter
public class File {
    private String name;
    private String extension;
    private Path path;
    @ToString.Exclude
    private byte[] fileDataByteArray;
    @EqualsAndHashCode.Exclude
    private Long id;
}
