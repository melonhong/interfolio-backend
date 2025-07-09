package com.example.interfolio.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/portfolio")
public class UploadController {
    @ResponseBody
    @PostMapping("/upload")
    public ResponseEntity<String> upload(
            @RequestParam("files") List<MultipartFile> files
    ) throws IOException {

        String relativePath = "uploads/"; // 예: 프로젝트 루트에서 uploads 폴더
        String realPath = new File(relativePath).getAbsolutePath(); // 절대 경로로 변환

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String filePath = realPath + File.separator + file.getOriginalFilename();
                file.transferTo(new File(filePath));
            }
        }

        return ResponseEntity.ok("업로드 완료");
    }
}
