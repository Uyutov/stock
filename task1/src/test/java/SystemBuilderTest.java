import org.example.file_system.builder.SystemBuilderImpl;
import org.example.file_system.directory.DirectoryComponent;
import org.example.file_system.folder.Folder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class SystemBuilderTest {
    private SystemBuilderImpl builder;
    private static DirectoryComponent simpleFolder;
    private static DirectoryComponent simpleFile;
    private static DirectoryComponent complexDirectory;


    @BeforeEach
    public void setupBuilder(){
        builder = new SystemBuilderImpl();
    }

    @BeforeAll
    public static void setupSimpleFolder()
    {
        Folder root = new Folder("root");
        DirectoryComponent folder = new Folder("folder");
        root.addDirectoryComponent(folder);
        simpleFolder = root;
    }
    @BeforeAll
    public static void setupSimpleFile()
    {
        Folder root = new Folder("root");
        DirectoryComponent file = new Folder("file.txt");
        root.addDirectoryComponent(file);
        simpleFile = root;
    }

    @BeforeAll
    public static void setupComplexDirectory()
    {
        Folder root = new Folder("root");
        Folder folder = new Folder("folder");
        DirectoryComponent file = new Folder("file.txt");
        Folder secondFolder = new Folder("java");
        root.addDirectoryComponent(folder);
        folder.addDirectoryComponent(file);
        root.addDirectoryComponent(secondFolder);
        complexDirectory = root;
    }
    @Test
    public void addFolder()
    {
        builder.start();
        builder.addFolder("folder");
        Assertions.assertEquals(builder.getFileSystem().getPath(""), simpleFolder.getPath(""));
    }
    @Test
    public void addFile()
    {
        builder.start();
        builder.addFile("file.txt");
        Assertions.assertEquals(builder.getFileSystem().getPath(""), simpleFile.getPath(""));
    }
    @Test void addFileToFolder()
    {
        builder.start();
        builder.addFolder("folder");
        builder.addFile("file.txt");
        builder.start();
        builder.addFolder("java");
        Assertions.assertEquals(builder.getFileSystem().getPath(""), complexDirectory.getPath(""));
    }
    @Test void checkErrorWhileAddingComponentsToFiles()
    {
        String error = "";
        try{
            builder.start();
            builder.addFile("file.txt");
            builder.start();
            builder.addFile("file.txt");
        }catch (RuntimeException e)
        {
            error = e.getMessage();
        }
        Assertions.assertEquals(error, "File with such name already exists");

    }
}
