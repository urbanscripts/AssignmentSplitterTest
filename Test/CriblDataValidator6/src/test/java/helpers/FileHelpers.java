package helpers;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class FileHelpers {

    private static final int MIN_RANGE = 200000;
    private static final int MAX_RANGE = 700000;
    public int getFileCount(File file) throws IOException {
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.readLine() != null) {
                count++;
            }
        } catch (IOException e) {
            // Handle the exception or rethrow it to the caller.
            throw e;
        }
        return count;
    }

    public boolean isTargetCountWithinRange(int target_count)
    {
        return target_count >= MIN_RANGE && target_count <= MAX_RANGE;
    }

    public boolean checkFileDataMatches(File input_file, File output_file) {
        boolean verifyFileDataMatches = true;

        try (BufferedReader reader1 = new BufferedReader(new FileReader(input_file));
             BufferedReader reader2 = new BufferedReader(new FileReader(output_file))) {

            String line1;
            String line2;
            while ((line1 = reader1.readLine()) != null && (line2 = reader2.readLine()) != null) {
                if (!line1.equalsIgnoreCase(line2)) {
                    verifyFileDataMatches = false;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            verifyFileDataMatches = false;
        }

        return verifyFileDataMatches;
    }

    private static int countLogEntries(String logFilePath, String regex) {
        int count = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(logFilePath))) {
            String line;
            Pattern pattern = Pattern.compile(regex);

            while ((line = br.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    count++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return count;
    }


    public void mergeFiles(String filePath1, String filePath2, String mergedFilePath) throws IOException {
        byte[] buffer = new byte[1024];
        try (InputStream is1 = new FileInputStream(filePath1);
             InputStream is2 = new FileInputStream(filePath2);
             OutputStream os = new FileOutputStream(mergedFilePath)) {
            int bytesRead;
            while ((bytesRead = is1.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            while ((bytesRead = is2.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        }

        // Read the contents of the file into a list
        //List<String> lines = readLinesFromFile(mergedFilePath);

        // Sort the list
        //Collections.sort(lines);

        // Write the sorted lines back to the file
        //writeLinesToFile(mergedFilePath, lines);
    }

    private static List<String> readLinesFromFile(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    private static void writeLinesToFile(String filePath, List<String> lines) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    public boolean compareFiles(String filePath1, String filePath2, String comparisonFilePath) throws IOException {
        Path path1 = Paths.get(filePath1);
        Path path2 = Paths.get(filePath2);
        Path comparisonPath = Paths.get(comparisonFilePath);

        byte[] file1Bytes = Files.readAllBytes(path1);
        byte[] file2Bytes = Files.readAllBytes(path2);
        boolean isFilesEqual = Arrays.equals(file1Bytes, file2Bytes);

        if (!isFilesEqual) {
            Files.write(comparisonPath, file1Bytes);
        }

        return isFilesEqual;
    }


}
