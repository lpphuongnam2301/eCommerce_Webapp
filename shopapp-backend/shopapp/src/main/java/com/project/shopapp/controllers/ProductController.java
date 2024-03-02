package com.project.shopapp.controllers;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.project.shopapp.dtos.ProductDTO;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    @GetMapping("")
    public ResponseEntity<?> getAllProducts
            ( @PathParam("page") int page,
              @PathParam("limit") int limit)
    {
        return ResponseEntity.ok("day la get all product");
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> insertProduct
            ( @Valid @ModelAttribute ProductDTO productDTO,
              BindingResult result) throws IOException
    {
        if (result.hasErrors())
        {
            List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();

            return ResponseEntity.badRequest().body(errorMessages);
        }

        List<MultipartFile> files = productDTO.getFiles();
        files = files == null ? new ArrayList<MultipartFile>() : files;
        for (MultipartFile file : files)
        {
            if (file.getSize() == 0)
            {
                continue;
            }

            if (file.getSize() > 1024 * 20 * 1024) //>20MB
            {
                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File too large! Must be < 10MB!");
            }

            String fileType = file.getContentType();
            if (fileType == null || !fileType.startsWith("image/")) {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("File is not image type!");
            }
            String fileName = saveFile(file);
        }

        return ResponseEntity.ok("Product created successfully!");
    }

    public String saveFile(MultipartFile file) throws IOException
    {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String newFileName = UUID.randomUUID().toString() + "_" + fileName;
        Path fileDirectory = Paths.get("uploads");
        if(!Files.exists(fileDirectory))
        {
            Files.createDirectories(fileDirectory);
        }
        Path destiDirectory = Paths.get(fileDirectory.toString(), newFileName);
        Files.copy(file.getInputStream(), destiDirectory, StandardCopyOption.REPLACE_EXISTING);
        return newFileName;
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable long id)
    {
        return ResponseEntity.ok("day la update product");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable long id)
    {
        return ResponseEntity.ok("day la delete product");
    }
}
