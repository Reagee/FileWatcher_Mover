import java.io.IOException;
import java.util.List;

public interface FileMoverInterface {
    void copyFile(List<String> fileTypes) throws IOException;
    void moveFile(List<String> fileTypes) throws IOException;
}
