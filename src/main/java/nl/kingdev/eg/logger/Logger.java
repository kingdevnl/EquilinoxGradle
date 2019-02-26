package nl.kingdev.eg.logger;

public class Logger {
    private static final String prefix = "[EquilinoxGradle ] > ";

    public static void info(Object msg) {
        System.out.println(prefix + msg.toString());
    }
}
