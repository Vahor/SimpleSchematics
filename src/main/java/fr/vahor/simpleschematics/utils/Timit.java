package fr.vahor.simpleschematics.utils;

public class Timit {

    public static void timit(Runnable runnable) {
        long start = System.currentTimeMillis();
        runnable.run();
        long end = System.currentTimeMillis();
        System.out.println("Diff : " + (start-end) + "ms");
    }
}
