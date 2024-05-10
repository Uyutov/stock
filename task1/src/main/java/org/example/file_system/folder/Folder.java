package org.example.file_system.folder;

import org.example.file_system.directory.DirectoryComponent;

import java.util.ArrayList;
import java.util.List;

public class Folder implements DirectoryComponent {
    private List<DirectoryComponent> components = new ArrayList<>();
    private String name;

    public Folder(String name) {
        this.name = name;
    }

    public void addDirectoryComponent(DirectoryComponent component)
    {
        components.add(component);
    }
    public List<DirectoryComponent> getDirectoryContent()
    {
        return components;
    }
    @Override
    public String getComponentName() {
        return name;
    }

    @Override
    public String getPath(String indent) {
        String fileSystem = indent.concat(name).concat("\n");
        for (DirectoryComponent component : components){
            fileSystem = fileSystem.concat(component.getPath(indent.concat("  ")));
        }
        return fileSystem;
    }
}
