import org.example.converters.input.InputConverter;
import org.example.converters.input.InputConverterImpl;
import org.example.file_system.builder.SystemBuilder;
import org.example.file_system.builder.SystemBuilderImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InputConverterTest {
    private SystemBuilder builder;
    private static SystemBuilder testBuilder;
    @BeforeEach
    public void createBuilder(){
        builder = new SystemBuilderImpl();
    }
    @BeforeAll
    public static void createTestDirectory()
    {
        testBuilder = new SystemBuilderImpl();
        testBuilder.start();
        testBuilder.addFolder("folder");
        testBuilder.addFile("file.txt");

        testBuilder.start();
        testBuilder.addFolder("folder");
        testBuilder.addFolder("nested_folder");
        testBuilder.addFile("nested_file.txt");

        testBuilder.start();
        testBuilder.addFolder("java");
        testBuilder.addFile("application.properties");

    }
    @Test
    public void testGettingDirectoryFromInputConverter(){
        InputConverter input = new InputConverterImpl(builder);
        input.convert("folder/file.txt");
        input.convert("folder/nested_folder/nested_file.txt");
        input.convert("java/application.properties");
        Assertions.assertEquals(builder.getFileSystem().getPath(""), testBuilder.getFileSystem().getPath(""));
    }
}
