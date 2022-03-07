package fr.vahor.simpleschematics.generator;

import org.bukkit.Material;

import java.io.FileWriter;
import java.io.IOException;

public class Generator {

    public static void main(String[] args) throws IOException {
        String packageName = "fr.vahor.simpleschematics.fawe";
        String className = "FaweColorCache";
        FileWriter writer = new FileWriter(className + ".java");
        writer.write("package " + packageName + "\n");
        writer.write("public class " + className + " {\n");
        writer.write("public static void init() {\n");
        writer.write("\n}");
        writer.write("\n}");
        writer.flush();
        writer.close();
    }

    private static void loadImages() {
    }
}
