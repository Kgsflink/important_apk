/*
 * Decompiled with CFR 0.152.
 */
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Scanner;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

public class JavaClassConverter {
    public static void main(String[] stringArray) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose an option:");
        System.out.println("1. Compile .java to .class");
        System.out.println("2. Run .class file");
        System.out.println("3. Decompile .class to .java");
        int n = scanner.nextInt();
        scanner.nextLine();
        if (n == 1) {
            System.out.print("Enter the path to the .java file: ");
            String string = scanner.nextLine();
            JavaClassConverter.compileJavaToClass(string);
        } else if (n == 2) {
            System.out.print("Enter the path to the .class file: ");
            String string = scanner.nextLine();
            JavaClassConverter.runClassFile(string);
        } else if (n == 3) {
            System.out.print("Enter the path to the .class file: ");
            String string = scanner.nextLine();
            JavaClassConverter.decompileClassToJava(string);
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private static void compileJavaToClass(String string) {
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        if (javaCompiler == null) {
            System.out.println("No Java compiler available. Ensure you are using a JDK, not a JRE.");
            return;
        }
        File file = new File(string);
        if (!file.exists()) {
            System.out.println("The specified .java file does not exist.");
            return;
        }
        int n = javaCompiler.run(null, null, null, file.getPath());
        if (n == 0) {
            System.out.println("Compilation successful. .class file should be in the same directory.");
        } else {
            System.out.println("Compilation failed.");
        }
    }

    private static void runClassFile(String string) {
        try {
            String string2 = JavaClassConverter.getClassNameFromFilePath(string);
            if (string2 == null) {
                System.out.println("Invalid .class file path.");
                return;
            }
            File file = new File(string);
            if (!file.exists()) {
                System.out.println("The specified .class file does not exist.");
                return;
            }
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            Class<?> clazz = classLoader.loadClass(string2);
            try {
                Method method = clazz.getMethod("main", String[].class);
                String[] stringArray = new String[]{};
                method.invoke(null, new Object[]{stringArray});
            }
            catch (NoSuchMethodException noSuchMethodException) {
                System.out.println("No main method found in the class.");
            }
        }
        catch (Exception exception) {
            System.out.println("An error occurred while running the class file: " + exception.getMessage());
        }
    }

    private static void decompileClassToJava(String string) {
        try {
            String string2 = "cfr.jar";
            File file = new File(string);
            if (!file.exists()) {
                System.out.println("The specified .class file does not exist.");
                return;
            }
            String string3 = new File(string).getParent();
            if (string3 == null) {
                string3 = ".";
            }
            ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", string2, string, "--outputdir", string3);
            processBuilder.inheritIO();
            Process process = processBuilder.start();
            int n = process.waitFor();
            if (n == 0) {
                System.out.println("Decompilation successful. The .java file should be in: " + string3);
            } else {
                System.out.println("Decompilation failed.");
            }
        }
        catch (IOException | InterruptedException exception) {
            System.out.println("An error occurred while decompiling the class file: " + exception.getMessage());
        }
    }

    private static String getClassNameFromFilePath(String string) {
        if (string.endsWith(".class")) {
            String string2 = new File(string).getName();
            String string3 = string2.substring(0, string2.length() - 6);
            String string4 = string.substring(0, string.length() - string2.length() - 6);
            string4 = string4.replace(File.separator, ".");
            return string4 + string3;
        }
        return null;
    }
}
