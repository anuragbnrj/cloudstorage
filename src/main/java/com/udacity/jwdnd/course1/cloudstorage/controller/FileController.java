package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/files")
@ControllerAdvice
public class FileController {

    private final FileService fileService;
    private final UserService userService;


    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
        // Load file from database
        File file = fileService.findFileById(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(new ByteArrayResource(file.getFileData()));
    }

    @PostMapping
    public String createFile(@RequestParam("fileUpload") MultipartFile multipartFile, Authentication authentication, Model model) {
        User user = userService.getUserByUsername((String) authentication.getPrincipal());
        Long userId = user.getUserId();
        String newFileName = multipartFile.getOriginalFilename();
        if (newFileName.isBlank()) {
            model.addAttribute("result", "error");
            model.addAttribute("errorMessage", "CANNOT UPLOAD file with no name!");
            return "result";
        }

        List<File> userFileList = fileService.findFilesByUserId(userId);
        for (File file : userFileList) {
            if (file.getFileName().equals(newFileName)) {
                model.addAttribute("result", "error");
                model.addAttribute("errorMessage", "CANNOT UPLOAD File with name " + newFileName + ". File with same name ALREADY EXISTS!");
                return "result";
            }
        }

        try {
            int rowsUpdated = fileService.createFile(multipartFile, userId);
            if (rowsUpdated < 1) {
                model.addAttribute("result", "error");
                return "result";
            }
        } catch (IOException exception) {
            model.addAttribute("result", "error");
            return "result";
        }

        model.addAttribute("result", "success");
        return "result";
    }

    @GetMapping("/deleteFile/{fileId}")
    public String deleteFileById(@PathVariable(name = "fileId") Long fileId, Authentication authentication, Model model) {
        User user = userService.getUserByUsername((String) authentication.getPrincipal());
        Long userId = user.getUserId();

        File existingFile = fileService.findFileById(fileId);
        if (existingFile == null) {
            model.addAttribute("result", "error");
            model.addAttribute("errorMessage", "CANNOT DELETE File with id " + fileId + ". File does NOT exist!");
            return "result";
        }
        if (!existingFile.getUserId().equals(userId)) {
            model.addAttribute("result", "error");
            model.addAttribute("errorMessage", "UNAUTHORIZED: File with id " + fileId + " does NOT belong to you!");
            return "result";
        }

        int rowsDeleted = fileService.deleteFileById(fileId);

        if (rowsDeleted < 1) {
            model.addAttribute("result", "error");
            return "result";
        }

        model.addAttribute("result", "success");
        return "result";
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxUploadSizeExceededException(Model model) {
        model.addAttribute("result", "error");
        model.addAttribute("errorMessage", "CANNOT UPLOAD File with size GREATER than 5MB!!");
        return "result";
    }

}
