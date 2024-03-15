package com.project.shopapp.services;

import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IProductService {
    Product createProduct(ProductDTO productDTO) throws Exception;

    Product getProductById(Long id) throws Exception;

    Page<ProductResponse> getAllProducts(PageRequest pageRequest, String keyword, Long id);

    Product updateProduct(Long id, ProductDTO productDTO) throws Exception;

    void deleteProduct(Long id);

    boolean existsByName(String name);

    ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws Exception;

    List<Product> findProductsByIds(List<Long> productIds);
}
