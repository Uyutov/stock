package org.example.file_system.file;

import org.example.file_system.directory.DirectoryComponent;

public class File implements DirectoryComponent {
    private String name;

    public File(String name) {
        this.name = name;
    }

    @Override
    public String getComponentName() {
        return name;
    }

    @Override
    public String getPath(String indent) {
        return indent.concat(name).concat("\n");
    }

}
