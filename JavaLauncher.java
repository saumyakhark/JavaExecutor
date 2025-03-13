import java.io.*;
import java.util.regex.*;

public class JavaLauncher {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: java JavaLauncher <JavaFile.java>");
            return;
        }

        String filePath = args[0];
        File file = new File(filePath);

        if (!file.exists() || !file.getName().endsWith(".java")) {
            System.out.println("Error: Invalid Java file.");
            return;
        }

        // Extract class name from the Java file
        String className = extractClassName(file);
        if (className == null) {
            System.out.println("Error: No public class found.");
            return;
        }

        // Compile the Java file
        Process compileProcess = Runtime.getRuntime().exec("javac " + filePath);
        compileProcess.waitFor();

        if (compileProcess.exitValue() != 0) {
            System.out.println("Compilation failed.");
            printProcessOutput(compileProcess);
            return;
        }

        // Run the Java file
        Process runProcess = Runtime.getRuntime().exec("java " + className);
        printProcessOutput(runProcess);
    }

    private static String extractClassName(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        Pattern pattern = Pattern.compile("public\\s+class\\s+(\\w+)");

        while ((line = reader.readLine()) != null) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                reader.close();
                return matcher.group(1);
            }
        }

        reader.close();
        return null;
    }

    private static void printProcessOutput(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }
}
