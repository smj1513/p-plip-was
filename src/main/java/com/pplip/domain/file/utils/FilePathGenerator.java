package com.pplip.domain.file.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class FilePathGenerator {
    public String generatePath(String saveName) {
        LocalDateTime now = LocalDateTime.now();
        String year = String.valueOf(now.getYear());
        String month = String.valueOf(now.getMonth());
        String day = String.valueOf(now.getDayOfMonth());

        StringBuilder sb = new StringBuilder();
        sb.append(year).append("/")
                .append(month).append("/")
                .append(day).append("/")
                .append(saveName);
        return sb.toString();
    }

    public String generateSaveFileName(String originFileName) {
        String extension = originFileName.substring(originFileName.lastIndexOf("."));
        return UUID.randomUUID().toString().replace("-", "") + extension;
    }
}
