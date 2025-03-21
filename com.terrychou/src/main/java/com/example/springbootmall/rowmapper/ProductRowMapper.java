package com.example.springbootmall.rowmapper;

import com.example.springbootmall.model.Product;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowMapper implements RowMapper<Product> {

    @Override
    public Product mapRow(ResultSet  resultSet, int rowNum) throws SQLException {
        Product  product = new Product();


        product.setProduct_id(resultSet.getInt("product_id"));
        product.setProduct_name(resultSet.getString("product_name"));
        product.setCategory(resultSet.getString("category"));
        product.setImage_url(resultSet.getString("image_url"));
        product.setPrice(resultSet.getInt("price"));
        product.setStock(resultSet.getInt("stock"));
        product.setDescription(resultSet.getString("description"));
        product.setCreatedDate(resultSet.getTimestamp("created_date"));
        product.setLastModifiedDate(resultSet.getTimestamp("last_modified_date"));


        return product;

    }


}
