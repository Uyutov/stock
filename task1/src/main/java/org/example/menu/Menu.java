package org.example.menu;

import org.example.converters.input.InputConverter;
import org.example.converters.output.OutputConverter;
import org.example.file_system.directory.DirectoryComponent;

import java.util.Scanner;

public class Menu {
    private boolean isWorking = true;
    private final OutputConverter outputConverter;
    private final InputConverter inputConverter;
    private final DirectoryComponent root;

    public Menu(OutputConverter outputConverter,
                InputConverter inputConverter,
                DirectoryComponent root) {
        this.root = root;
        this.outputConverter = outputConverter;
        this.inputConverter = inputConverter;
    }

    public void getMenuOptions() {
        System.out.println("Enter 1 for adding new path \n" +
                            "Enter 2 to get existing paths \n" +
                            "Enter anything else to quit");
    }

    public void chooseOption() {
        Scanner scanner = new Scanner(System.in);
        int optionNumber = scanner.nextInt();
        switch (optionNumber) {
            case 1: {
                inputPath();
                break;
            }
            case 2: {
                outputPaths();
                break;
            }
            default: {
                quit();
            }
        }
    }

    void inputPath() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter new path:");
        String path = scanner.nextLine();
        try {
            inputConverter.convert(path);
        }catch (RuntimeException e)
        {
            System.out.println(e.getMessage());
        }
    }

    void outputPaths() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(outputConverter.convert(root));
        System.out.println("Press Enter to continue");
        scanner.nextLine();
    }

    void quit() {
        isWorking = false;
    }

    public boolean isWorking() {
        return isWorking;
    }
}
