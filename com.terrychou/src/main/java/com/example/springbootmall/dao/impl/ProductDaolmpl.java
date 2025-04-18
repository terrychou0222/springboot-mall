package com.example.springbootmall.dao.impl;

import com.example.springbootmall.dao.ProductDao;
import com.example.springbootmall.dto.ProductQueryParams;
import com.example.springbootmall.dto.ProductRequest;
import com.example.springbootmall.model.Product;
import com.example.springbootmall.rowmapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductDaolmpl implements ProductDao {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    @Override
    public Integer countProduct(ProductQueryParams productQueryParams) {

        String sql= "SELECT count(*) FROM product WHERE 1=1";

        Map<String,Object>map=new HashMap<>();

        //條件查詢
        sql=addFilteringSql(sql,productQueryParams,map);

        Integer total=namedParameterJdbcTemplate.queryForObject(sql,map,Integer.class);


        return total;



    }

    @Override
    public List<Product> getProducts(ProductQueryParams productQueryParams) {

        String sql = " SELECT product_id, product_name, category,image_url, price,stock, description,"+
                "created_date,last_modified_date FROM product WHERE 1=1 ";

        Map<String,Object>map=new HashMap<>();


        //條件查詢
        sql=addFilteringSql(sql,productQueryParams,map);

        //排序
         sql=sql+" ORDER BY "+productQueryParams.getOrderBy()+" "+productQueryParams.getSort();

         //分頁
         sql=sql+" LIMIT :limit OFFSET :offset";
         map.put("limit",productQueryParams.getLimit());
         map.put("offset",productQueryParams.getOffset());


         //回傳
        List<Product>productList=namedParameterJdbcTemplate.query(sql,map,new ProductRowMapper());
        return productList;



    }









    @Override
    public Product getProductById(Integer productId) {

        String sql="SELECT  product_id,product_name, category, image_url, price, stock, description, created_date, last_modified_date From product  where product_id=:productId";

        Map<String,Object> map=new HashMap<>();
        map.put("productId",productId);

        List<Product> productList=namedParameterJdbcTemplate.query(sql,map,new ProductRowMapper());

        if (productList.isEmpty()) {
            return null;
        } else{
                return productList.get(0);
            }
        }




    @Override
    public Integer createProduct(ProductRequest productRequest) {
        String sql="INSERT INTO product(product_name,category,image_url,price,stock,description,created_date,last_modified_date)"+
                "VALUES(:productName,:category,:imageUrl,:price,:stock,:description,:createdDate,:lastModifiedDate)";

        Map<String,Object> map=new HashMap<>();

        map.put("productName",productRequest.getProductName());
        map.put("category",productRequest.getCategory().name());
        map.put("imageUrl",productRequest.getImage_url());
        map.put("price",productRequest.getPrice());
        map.put("stock",productRequest.getStock());
        map.put("description",productRequest.getDescription());

        Date now=new Date();
        map.put("createdDate",now);
        map.put("lastModifiedDate",now);


        KeyHolder keyHolder=new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql,new MapSqlParameterSource(map),keyHolder);
        int productId=keyHolder.getKey().intValue();
        return productId;
    }



    //實作updateporduct
    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        String sql = "UPDATE product SET product_name=:productName, category=:category, image_url=:imageUrl, price=:price, " +
                "stock=:stock, description=:description, last_modified_date=:lastModifiedDate " +
                "WHERE product_id=:productId";

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().name());
        map.put("imageUrl", productRequest.getImage_url());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());

        Date now = new Date();
        map.put("lastModifiedDate", now);

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map)); // 移除 KeyHolder
    }

    @Override
    public void deleteProduct(Integer productId) {

        String sql = "DELETE FROM product WHERE product_id=:productId";

        Map<String,Object>map=new HashMap<>();
        map.put("productId", productId);

        namedParameterJdbcTemplate.update(sql,map);


    }


    private String addFilteringSql(String sql,ProductQueryParams productQueryParams,Map<String,Object>map) {

        if (productQueryParams.getCategory() != null){
            sql=sql+" AND category= :category";
            map.put("category",productQueryParams.getCategory().name());
            System.out.println(productQueryParams.getCategory());
        }


        if (productQueryParams.getSearch() != null){
            sql=sql+" AND product_name LIKE :search";
            map.put("search","%"+productQueryParams.getSearch()+"%");
        }

        return  sql;
    }


}

