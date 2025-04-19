package pfe.mandomati.academicms.Service.Impl.Utils;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUtil {
    public static String saveFile(String baseDir, MultipartFile file, String subDir) throws IOException {
        // Validation du nom de fichier
        String fileName = validateFilename(file.getOriginalFilename());

        // Création du répertoire s'il n'existe pas
        Path uploadPath = Paths.get(baseDir, subDir).toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);

        // Vérification de la sécurité du chemin
        Path targetLocation = uploadPath.resolve(fileName);
        validatePathSecurity(targetLocation, uploadPath);

        // Sauvegarde du fichier
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        return targetLocation.toString();
    }

    public static void deleteFile(String filePath) {
        try {
            Path path = Paths.get(filePath).normalize();
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file: " + e.getMessage());
        }
    }

    private static String validateFilename(String filename) {
        if (filename == null || filename.contains("..") || filename.isBlank()) {
            throw new SecurityException("Nom de fichier invalide: " + filename);
        }
        return filename.trim();
    }

    private static void validatePathSecurity(Path targetPath, Path baseDir) {
        if (!targetPath.startsWith(baseDir)) {
            throw new SecurityException("Tentative d'accès non autorisé: " + targetPath);
        }
    }
}
