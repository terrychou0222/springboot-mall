package com.example.springbootmall.controller;


import com.example.springbootmall.constant.ProductCategory;
import com.example.springbootmall.dto.ProductQueryParams;
import com.example.springbootmall.dto.ProductRequest;
import com.example.springbootmall.model.Product;
import com.example.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;


@Validated
@RestController

public class ProductController {

    @Autowired
    private ProductService productService;

    //得到 products list
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(
            //查詢條件 Filtering
           @RequestParam(required = false) ProductCategory category,
           @RequestParam(required = false) String search,

           //排序 Sorting
           @RequestParam(defaultValue = "created_date") String orderBy,
           @RequestParam(defaultValue = "desc") String sort,

           //分頁 Paination

           @RequestParam(defaultValue ="5") @Max(1000)  @Min(0) Integer limit,
           @RequestParam(defaultValue = "0") @Min(0) Integer offset


    ) {

        ProductQueryParams productQueryParams = new ProductQueryParams();
        productQueryParams.setCategory(category);
        productQueryParams.setSearch(search);
        productQueryParams.setOrderBy(orderBy);
        productQueryParams.setSort(sort);
        productQueryParams.setLimit(limit);
        productQueryParams.setOffset(offset);


        List<Product> productList=productService.getProducts(productQueryParams);
        return  ResponseEntity.status(HttpStatus.OK).body(productList);
    }





    @GetMapping("/products/{productId}")
    public ResponseEntity<Product>getProduct(@PathVariable Integer productId) {

        Product product = productService.getProductById(productId);

        if (product != null) {
            return ResponseEntity.status(HttpStatus.OK).body(product);

        }
        else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);


        }

    }


    @PostMapping("/products")
    public ResponseEntity<Product>createProduct(@RequestBody  @Valid ProductRequest productRequest) {
          Integer productId=productService.createProduct(productRequest);

          Product product = productService.getProductById(productId);
          return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }



    ///  修改
    @PutMapping("/products/{productId}")
    public ResponseEntity<Product> updateProductc(@PathVariable Integer productId,
                                               @RequestBody @Valid ProductRequest productRequest){

        Product product = productService.getProductById(productId);
        if (product == null) {
            System.out.println("沒有此ID");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

            productService.updateProduct(productId,productRequest);
            Product updatedProduct = productService.getProductById(productId);
            return  ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
    }


    ///  delete product
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId) {

        //檢查這個ID是否存在

        Product product= productService.getProductById(productId);
        if(product==null){
            System.out.println("找不到此ID");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        productService.deleteProduct(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();





    }






    }




