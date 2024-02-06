package dev.glory.demo.domain.file;

import java.util.stream.IntStream;

import dev.glory.demo.common.util.file.FileErrorCode;
import dev.glory.demo.common.util.file.FileException;
import dev.glory.demo.common.util.file.FileUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    public boolean isValidExtension(MultipartFile... files) {
        if (files == null) {
            return true;
        }

        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            boolean validExtension = FileUtil.isValidExtension(fileName);
            if (!validExtension) {
                throw new FileException(FileErrorCode.FILE_FORMAT_ERROR);
            }
        }

        return true;
    }

    public String[] saveFile(String filename, String savePath, MultipartFile... files) {
        if (files == null) {
            return new String[] {};
        }

        String[] filePaths = new String[files.length];
        IntStream.range(0, files.length).forEach(i -> {
            MultipartFile file = files[i];
            String savedFile = FileUtil.saveFile(file, savePath, filename);
            filePaths[i] = savedFile;
        });

        return filePaths;
    }

}
