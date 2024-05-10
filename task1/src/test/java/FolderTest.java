import org.example.file_system.file.File;
import org.example.file_system.folder.Folder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class FolderTest {
    @Test
    public void checkDirectoryOutput()
    {
        Folder folder = new Folder("root");

        File file = new File("file.txt");
        folder.addDirectoryComponent(file);

        Folder nestedFolder = new Folder("nested");
        File nestedFile = new File("nested_file.txt");
        nestedFolder.addDirectoryComponent(nestedFile);
        folder.addDirectoryComponent(nestedFolder);
        Assertions.assertEquals(folder.getPath(""), "root\n  file.txt\n  nested\n    nested_file.txt\n");
    }

}
