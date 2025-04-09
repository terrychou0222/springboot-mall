package com.example.springbootmall.dao;

import com.example.springbootmall.dto.ProductRequest;
import com.example.springbootmall.model.Product;

import java.util.List;

public interface ProductDao {


    List<Product> getProducts();


    Product getProductById(Integer  productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId, ProductRequest productRequest);

    void deleteProduct(Integer productId);

}
