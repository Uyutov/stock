package org.example.converters.input;

import org.example.file_system.builder.SystemBuilder;
import org.example.file_system.directory.DirectoryComponent;

import java.util.*;

public class InputConverterImpl implements InputConverter {
    private SystemBuilder builder;

    public InputConverterImpl(SystemBuilder builder) {
        this.builder = builder;
    }

    @Override
    public void convert(String path) throws RuntimeException {
        validate(path);
        builder.start();
        getFolders(path).forEach(folder ->
        {
            builder.addFolder(folder);
        });
        Optional<String> file = getFile(path);
        if(file.isPresent())
        {
            builder.addFile(file.get());
        }
    }

    private void validate(String path) {
        String[] content = path.split("/");
        for (int i = 0; i < content.length - 1; i++) {
            if (content[i].contains(".")) {
                throw new RuntimeException("Cannot add folders or files into files");
            }
        }
    }

    private List<String> getFolders(String path) {
        var directories = new ArrayList<String>(Arrays.asList(path.split("/")));
        if (directories.getLast().contains(".")) {
            directories.removeLast();
        }
        return directories;
    }

    private Optional<String> getFile(String path) {
        var directories = new ArrayList<String>(Arrays.asList(path.split("/")));
        if (directories.getLast().contains(".")) {
            return Optional.of(directories.getLast());
        }
        return Optional.empty();
    }
}
