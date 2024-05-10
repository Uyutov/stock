package org.example.file_system.directory;

public interface DirectoryComponent {
    String getComponentName();
    String getPath(String indent);
}
