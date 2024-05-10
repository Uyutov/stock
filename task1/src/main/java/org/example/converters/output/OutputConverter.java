package org.example.converters.output;

import org.example.file_system.directory.DirectoryComponent;

public interface OutputConverter {
    String convert(DirectoryComponent root);
}
