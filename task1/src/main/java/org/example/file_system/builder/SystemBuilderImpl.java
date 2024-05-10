package org.example.file_system.builder;

import org.example.file_system.directory.DirectoryComponent;
import org.example.file_system.file.File;
import org.example.file_system.folder.Folder;

public class SystemBuilderImpl implements SystemBuilder {
    private final Folder ROOT = new Folder("root");
    private Folder current;

    @Override
    public void start() {
        current = ROOT;
    }

    @Override
    public void addFolder(String folderName) {
        for (DirectoryComponent folder : current.getDirectoryContent()) {
            if (folder.getComponentName().equals(folderName)) {
                current = (Folder) folder;
                return;
            }
        }
        Folder newFolder = new Folder(folderName);
        current.addDirectoryComponent(newFolder);
        current = newFolder;
    }

    @Override
    public void addFile(String fileName) {
        current.getDirectoryContent().forEach(file ->
        {
            if (file.getComponentName().equals(fileName)) {
                throw new RuntimeException("File with such name already exists");
            }
        });
        current.addDirectoryComponent(new File(fileName));
    }

    @Override
    public DirectoryComponent getFileSystem() {
        return ROOT;
    }
}
