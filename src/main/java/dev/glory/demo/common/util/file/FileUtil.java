package dev.glory.demo.common.util.file;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Set;

import jakarta.validation.constraints.NotEmpty;

import lombok.extern.slf4j.Slf4j;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public class FileUtil {


    private static final DateTimeFormatter DATE_FORMATTER        = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter DATE_TIME_FORMATTER   = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final Set<String>       VALID_FILE_EXTENSIONS = Set.of("txt", "csv", "xlsx", "jpg", "jpeg");

    protected FileUtil() {
    }

    /**
     * 디렉토리가 존재하지 않으면 생성한다
     *
     * @param dirPath 디렉토리 경로
     */
    public static void createDirIfNotExists(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 디렉토리를 삭제한다
     *
     * @param dirPath 디렉토리 경로
     */
    public static void deleteDir(String dirPath) {
        deleteDir(dirPath, false);
    }

    /**
     * 디렉토리를 삭제한다, force가 true이면 디렉토리 내부의 파일들도 삭제한다
     *
     * @param dirPath 디렉토리 경로
     */
    public static void deleteDir(String dirPath, boolean force) {
        File dir = new File(dirPath);
        if (dir.exists()) {
            if (force) {
                deleteRecursively(dir);
            }
            dir.delete();
        }
    }

    /**
     * 디렉토리가 존재하는지 확인한다
     *
     * @param dirPath 디렉토리 경로
     */
    public static boolean isExistingDirectory(String dirPath) {
        File dir = new File(dirPath);
        return dir.exists();
    }

    /**
     * 파일이 존재하는지 확인한다
     *
     * @param filePath 파일 경로 (파일명 포함)
     */
    public static boolean isExistingFile(String filePath) {
        return isExistingDirectory(filePath);
    }

    /**
     * 파일 확장자 검증 한다
     *
     * @param filename 파일명
     * @return true: 유효한 확장자, false: 유효하지 않은 확장자
     */
    public static boolean isValidExtension(@NotEmpty String filename) {
        return isValidExtension(filename, null);
    }

    /**
     * 파일 확장자 검증 한다
     *
     * @param filename 파일명
     * @param suffix   확장자
     * @return true: 유효한 확장자, false: 유효하지 않은 확장자
     */
    public static boolean isValidExtension(@NotEmpty String filename, String suffix) {
        Assert.hasText(filename, "filename must not be empty");
        log.debug("==> filename: {}", filename);

        String[] strings = filename.toLowerCase().split("\\.");
        if (strings.length <= 1) {
            return false;
        }
        if (!StringUtils.hasText(suffix)) {
            return VALID_FILE_EXTENSIONS.contains(strings[strings.length - 1]);
        }
        return strings[strings.length - 1].equals(suffix);
    }

    /**
     * 멀티파트 파일을 저장한다
     *
     * @param multiPartFile 멀티파트 파일
     * @param savePath      저장 경로
     * @param fileName      파일명
     * @return 저장된 파일 경로 (파일명 포함)
     */
    public static String saveFile(@NotEmpty MultipartFile multiPartFile,
            @NotEmpty String savePath, @NotEmpty String fileName) {

        if (multiPartFile == null || multiPartFile.isEmpty()) {
            throw new FileException(FileErrorCode.NO_FILE_ERROR);
        }

        String originalFilename = multiPartFile.getOriginalFilename();
        if (!StringUtils.hasText(originalFilename)) {
            throw new FileException(FileErrorCode.FILE_ERROR);
        }

        Path directory = createTargetDirectory(savePath);
        Path targetPath = computeSaveFilePath(directory, originalFilename, fileName);

        transferFile(multiPartFile, targetPath);

        return targetPath.toString();
    }

    /**
     * 파일이 존재하면 파일명 뒤에 숫자를 붙여서 리네임 한다
     *
     * @param filePath 파일 경로 (파일명 포함)
     * @return 리네임된 파일 경로 (파일명 포함)
     */
    public static String renameIfExists(String filePath) {
        Path originalPath = Paths.get(filePath);
        if (!Files.exists(originalPath)) {
            return filePath;
        }

        int counter = 1;
        Path newPath;
        String nameWithoutExtension = getFileNameWithoutExtension(originalPath.getFileName().toString());
        String extension = getFileExtension(originalPath.getFileName().toString());

        while (true) {
            newPath = originalPath.getParent().resolve(nameWithoutExtension + "(" + counter + ")" + extension);
            if (!Files.exists(newPath)) {
                break;
            }
            counter++;
        }

        return newPath.toString();
    }

    private static Path createTargetDirectory(String savePath) {
        String formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Path directory = Paths.get(savePath, formattedDate);

        createDirIfNotExists(directory.toString());
        return directory;
    }

    private static Path computeSaveFilePath(Path directory, String originalFileName, String fileName) {
        int fileIndex = originalFileName.lastIndexOf('.');
        String saveFileName = originalFileName;

        if (fileIndex != -1) {
            String ext = originalFileName.substring(fileIndex + 1).toLowerCase();
            saveFileName = String.format("%s_%s.%s", createFileID(), fileName, ext);
        }

        return directory.resolve(saveFileName);
    }

    private static void transferFile(MultipartFile multiPartFile, Path targetPath) {
        try {
            multiPartFile.transferTo(targetPath);
        } catch (Exception e) {
            throw new FileException(FileErrorCode.FILE_SAVE_ERROR, e);
        }
    }

    private static String createFileID() {
        return LocalDateTime.now().format(DATE_TIME_FORMATTER);
    }

    private static void deleteRecursively(File file) {
        if (file.isDirectory()) {
            Arrays.stream(file.listFiles()).forEach(FileUtil::deleteRecursively);
        }
        file.delete();
    }

    private static String getFileNameWithoutExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0) {
            return fileName.substring(0, dotIndex);
        }
        return fileName;
    }

    private static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return "." + fileName.substring(dotIndex + 1);
        }
        return "";
    }
}
