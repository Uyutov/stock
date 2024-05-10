package org.example.converters.output;

import org.example.file_system.directory.DirectoryComponent;

public class OutputConverterImpl implements OutputConverter{
    @Override
    public String convert(DirectoryComponent root) {
        return root.getPath("");
    }
}
