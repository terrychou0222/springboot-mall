package com.example.springbootmall.service.impl;

import com.example.springbootmall.dao.ProductDao;
import com.example.springbootmall.dto.ProductRequest;
import com.example.springbootmall.model.Product;
import com.example.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ProducctServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    public Product getProductById(Integer productId) {


        return productDao.getProductById(productId);
    }

    @Override
    public Integer createProduct(ProductRequest productRequest) {

        return  productDao.createProduct(productRequest);

    }


    //實作 updateProduct
    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {

        productDao.updateProduct(productId,productRequest);

    }

    @Override
    public void deleteProduct(Integer productId) {

        productDao.deleteProduct(productId);

    }
}
