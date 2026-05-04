package com.example.FLOAK;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/logs")
@CrossOrigin("*")
public class LogController {

    @PostMapping("/analyze")
    public Map<String, Object> analyzeLog(@RequestParam("file") MultipartFile file) throws Exception {

        String content = new String(file.getBytes());

        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> errors = new ArrayList<>();

        int nullCount = countOccurrences(content, "NullPointerException");
        int memoryCount = countOccurrences(content, "OutOfMemoryError");

        if (nullCount > 0) {
            Map<String, Object> err = new HashMap<>();
            err.put("type", "NullPointerException");
            err.put("count", nullCount);
            err.put("solution", "Check object initialization");
            errors.add(err);
        }

        if (memoryCount > 0) {
            Map<String, Object> err = new HashMap<>();
            err.put("type", "OutOfMemoryError");
            err.put("count", memoryCount);
            err.put("solution", "Increase heap size or optimize memory");
            errors.add(err);
        }

        if (errors.isEmpty()) {
            response.put("summary", "No major issues found");
        } else {
            response.put("summary", errors.size() + " types of errors found");
        }

        response.put("errors", errors);

        return response;
    }

    // Helper method to count occurrences
    private int countOccurrences(String text, String keyword) {
        int count = 0;
        int index = 0;

        while ((index = text.indexOf(keyword, index)) != -1) {
            count++;
            index += keyword.length();
        }

        return count;
    }
}