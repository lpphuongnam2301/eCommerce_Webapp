package com.project.shopapp.services;

import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.models.Category;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.repositories.CategoryRepository;
import com.project.shopapp.repositories.ProductImageRepository;
import com.project.shopapp.repositories.ProductRepository;
import com.project.shopapp.responses.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    @Override
    public Product createProduct(ProductDTO productDTO) throws Exception {
        Category category = categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(() -> new Exception("Cannot find category with ID: " + productDTO.getCategoryId()));

        Product product = Product.builder()
                .category(category)
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .description(productDTO.getDescription())
                .build();
        return productRepository.save(product);
    }

    @Override
    public Product getProductById(Long id) throws Exception {
        return productRepository.findById(id).orElseThrow(() -> new Exception("Cannot find product with ID: " + id));
    }

    @Override
    public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {

        return productRepository.findAll(pageRequest).map(ProductResponse::getProductResponse);
    }

    @Override
    public Product updateProduct(Long id, ProductDTO productDTO) throws Exception {
        Category category = categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(() -> new Exception("Cannot find category with ID: " + productDTO.getCategoryId()));
        Product existsProduct = getProductById(id);
        existsProduct = Product.builder()
                .category(category)
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .description(productDTO.getDescription())
                .build();

        return productRepository.save(existsProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        if(productRepository.findById(id).isPresent())
        {
            productRepository.deleteById(id);
        }
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws Exception
    {
        Product existsProduct = productRepository.findById(productId).orElseThrow(() -> new Exception("Cannot find product with ID: " + productImageDTO.getProductId()));
        ProductImage newProductImage = ProductImage.builder()
                .product(existsProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();
        int size = productImageRepository.findByProductId(productId).size();
        if(size >= ProductImage.MAXIMUM_IMAGES)
        {
            throw new Exception("Cannot upload over "+ProductImage.MAXIMUM_IMAGES+" images for 1 product");
        }
        return productImageRepository.save(newProductImage);
    }
}
