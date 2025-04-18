package pfe.mandomati.academicms.Service.Impl.Utils;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {
    public static String saveFile(String baseDir, MultipartFile file, String subDir) throws IOException {
        // Create the full directory path (baseDir + optional subDir)
        Path directoryPath = subDir != null ? Paths.get(baseDir, subDir) : Paths.get(baseDir);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath); // Create directory if it doesn't exist
        }

        // Save the file
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = directoryPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        return filePath.toString();
    }

    public static void deleteFile(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file: " + e.getMessage());
        }
    }
}
