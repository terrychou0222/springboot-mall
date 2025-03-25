package com.example.springbootmall.constant;

public class MyTest {


    public static void main(String[] args) {
        //法1
        ProductCategory category=ProductCategory.FOOD;
        String s = category.name();
        System.out.println(s); //FOOD

        //法2
        String s2="CAR";
        ProductCategory category2=ProductCategory.valueOf(s2);





    }
}
