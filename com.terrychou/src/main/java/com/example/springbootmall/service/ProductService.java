package com.example.springbootmall.service;

import com.example.springbootmall.dto.ProductRequest;
import com.example.springbootmall.model.Product;

import javax.validation.Valid;
import java.util.List;

public interface ProductService {



    List<Product> getProducts();



    Product getProductById(Integer  productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId, @Valid ProductRequest productRequest);


    void deleteProduct(Integer productId);




}
