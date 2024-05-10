package org.example;

import org.example.converters.input.InputConverter;
import org.example.converters.input.InputConverterImpl;
import org.example.converters.output.OutputConverter;
import org.example.converters.output.OutputConverterImpl;
import org.example.file_system.builder.SystemBuilder;
import org.example.file_system.builder.SystemBuilderImpl;
import org.example.menu.Menu;

public class Main {
    public static void main(String[] args) {
        SystemBuilder builder = new SystemBuilderImpl();
        OutputConverter outputConverter = new OutputConverterImpl();
        InputConverter inputConverter = new InputConverterImpl(builder);
        Menu menu = new Menu(outputConverter, inputConverter, builder.getFileSystem());
        while (menu.isWorking()) {
            menu.getMenuOptions();
            menu.chooseOption();
        }
    }
}