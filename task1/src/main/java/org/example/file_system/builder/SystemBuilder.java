package org.example.file_system.builder;

import org.example.file_system.directory.DirectoryComponent;

public interface SystemBuilder {
    void start();
    void addFolder(String folderName);
    void addFile(String fileName);
    DirectoryComponent getFileSystem();
}
