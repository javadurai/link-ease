package com.linkease.repository;

import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/doc")
public class DocumentController {

    private final Tika tika = new Tika();

    // Display the upload form
    @GetMapping
    public String showUploadForm() {
        return "doc/upload";
    }

    // Handle the file upload and extract metadata
    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        try (InputStream stream = file.getInputStream()) {
            Metadata metadata = new Metadata();
            tika.parse(stream, metadata);

            Map<String, String> metadataMap = new HashMap<>();
            for (String name : metadata.names()) {
                metadataMap.put(name, metadata.get(name));
            }

            model.addAttribute("metadata", metadataMap);
        } catch (Exception e) {
            model.addAttribute("error", "Failed to process file: " + e.getMessage());
        }
        return "doc/result";
    }
}
