package com.virtualdrive.controller;

import com.virtualdrive.model.File;
import com.virtualdrive.services.FileUploadService;
import com.virtualdrive.services.UserService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class FileUploadController {

    private UserService userService;
    private FileUploadService fileUploadService;

    public FileUploadController(UserService userService, FileUploadService fileUploadService) {
        this.userService = userService;
        this.fileUploadService = fileUploadService;
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile uploadFile, Authentication authentication, RedirectAttributes redirectAttributes) throws Exception {
        if (uploadFile.isEmpty()) {
            redirectAttributes.addFlashAttribute("fileError", "Please select file to upload!");
            return "redirect:/result";
        }
        try {
            Integer userId = userService.getUser(authentication.getName()).getUserId();
            String fileName = uploadFile.getOriginalFilename();

            // Check duplicate filename for the user
            if (fileUploadService.findByFileName(fileName, userId) != null) {
                redirectAttributes.addFlashAttribute("fileError", "The filename already exists.");
                return "redirect:/result";
            }

            File file = new File();
            file.setFileName(uploadFile.getOriginalFilename());
            file.setFileSize(String.valueOf(uploadFile.getSize()));
            file.setContentType(uploadFile.getContentType());
            file.setFileData(uploadFile.getBytes());
            file.setUserId(userId);

            int count = fileUploadService.insert(file);

            if (count > 0) {
                redirectAttributes.addFlashAttribute("fileSuccess", "Your file uploaded successfully.");
            } else {
                redirectAttributes.addFlashAttribute("fileError", "An error occurred while uploading your file.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("fileError", "An error occurred while uploading your file.");
        }

        return "redirect:/result";
    }

    @GetMapping("/files/delete/{id}")
    public String deleteFile(@PathVariable(value = "id") Integer fileId, RedirectAttributes redirectAttributes) {
        try {
            fileUploadService.delete(fileId);
            redirectAttributes.addFlashAttribute("fileSuccess", "Your file deleted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("fileError", "An error occurred while deleting your file.");
        }
        return "redirect:/result";
    }

    @GetMapping("/files/download/{id}")
    public ResponseEntity<byte[]> download(@PathVariable(value = "id") Integer fileId, HttpServletResponse response) throws IOException {
        File file = fileUploadService.findById(fileId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.builder("attachment").filename(file.getFileName()).build().toString());
        return ResponseEntity.ok().headers(httpHeaders).body(file.getFileData());
    }
}
