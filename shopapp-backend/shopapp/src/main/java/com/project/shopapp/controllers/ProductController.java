package com.project.shopapp.controllers;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.github.javafaker.Faker;
import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.responses.ProductListResponse;
import com.project.shopapp.responses.ProductResponse;
import com.project.shopapp.services.ProductService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    @GetMapping("")
    public ResponseEntity<?> getProducts
            ( @RequestParam(defaultValue = "0") int page,
              @RequestParam(defaultValue = "6") int limit,
              @RequestParam(defaultValue = "") String keyword,
              @RequestParam(defaultValue = "0", name = "category_id") Long categoryId)
    {
        //phan trang
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("id").ascending());
        Page<ProductResponse> productPage = productService.getAllProducts(pageRequest, keyword, categoryId);
        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();

        return ResponseEntity.ok(ProductListResponse.builder().products(products).totalPages(totalPages).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long id)
    {
        try {
            Product product = productService.getProductById(id);
            return ResponseEntity.ok().body(ProductResponse.getProductResponse(product));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("")
    public ResponseEntity<?> createProduct
            ( @Valid @RequestBody ProductDTO productDTO,
              BindingResult result) throws Exception
    {
        if (result.hasErrors())
        {
            List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();

            return ResponseEntity.badRequest().body(errorMessages);
        }
        Product newProduct = productService.createProduct(productDTO);

        return ResponseEntity.ok("Product created successfully!");
    }

    @PostMapping(value = "uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages (@ModelAttribute("files") List<MultipartFile> files,
                                           @PathVariable("id") Long id)
    {
        try {
            Product exitsProduct = productService.getProductById(id);
            files = files == null ? new ArrayList<MultipartFile>() : files;
            if(files.size() > ProductImage.MAXIMUM_IMAGES)
            {
                return ResponseEntity.badRequest().body("Cannot upload over "+ProductImage.MAXIMUM_IMAGES+" images for 1 product");
            }
            List<ProductImage> productImages = new ArrayList<>();
            for (MultipartFile file : files) {
                if (file.getSize() == 0) {
                    continue;
                }

                if (file.getSize() > 1024 * 20 * 1024) //>20MB
                {
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File too large! Must be < 10MB!");
                }

                String fileName = saveFile(file);
                ProductImageDTO productImageDTO = ProductImageDTO.builder().imageUrl(fileName).build();

                ProductImage productImage = productService.createProductImage(exitsProduct.getId(), productImageDTO);
                productImages.add(productImage);
            }
            return ResponseEntity.ok().body(productImages);
        } catch (Exception e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public String saveFile(MultipartFile file) throws Exception
    {
        if(file.getOriginalFilename() == null || !isImageFiles(file))
        {
            throw new Exception("File is invalid!");
        }
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
    public ResponseEntity<?> updateProduct(@PathVariable long id, @RequestBody ProductDTO productDTO)
    {
        try
        {
            Product product = productService.updateProduct(id, productDTO);
            return ResponseEntity.ok(product);
        } catch (Exception e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable long id)
    {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Delete product with ID: " + id + " successfully");
    }

    private boolean isImageFiles(MultipartFile file)
    {
        String contenType = file.getContentType();
        return contenType != null && contenType.startsWith("image/");
    }

    @GetMapping("/images/{imageName}")
    private ResponseEntity<?> getImaage(@PathVariable(value = "imageName") String imageName)
    {
        try {
            Path imagePath = Paths.get("uploads/"+imageName);
            UrlResource urlResource = new UrlResource(imagePath.toUri());
            if(urlResource.exists())
            {
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(urlResource);
            }
            else {
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(new UrlResource(Paths.get("uploads/404.jpg").toUri()));
            }
        } catch (Exception e)
        {
            return ResponseEntity.notFound().build();
        }
    }
    //@PostMapping("/generateFakeProducts")
    private ResponseEntity<?> generateFake()
    {
        Faker faker = new Faker();
        for(int i = 0; i<300; i++)
        {
            String productName = faker.commerce().productName();
            if(productService.existsByName(productName))
            {
                continue;
            }
            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName).price((float)faker.number().numberBetween(10000, 90000000))
                    .description(faker.lorem().sentence())
                    .thumbnail("")
                    .categoryId((long)faker.number().numberBetween(1, 5)).build();
            try {
                productService.createProduct(productDTO);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok("fake ok");
    }

    @GetMapping("/by-ids")
    public ResponseEntity<?> getProductByIds(@RequestParam("ids") String ids)
    {
        try {
            List<Long> productIds = Arrays.stream(ids.split(","))
                    .map(Long::parseLong)
                    .toList();
            List<Product> products = productService.findProductsByIds(productIds);
            return ResponseEntity.ok(products);
        } catch (Exception e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
