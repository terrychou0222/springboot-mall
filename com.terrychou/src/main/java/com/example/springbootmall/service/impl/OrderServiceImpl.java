package com.example.springbootmall.service.impl;

import com.example.springbootmall.dao.OrderDao;
import com.example.springbootmall.dao.ProductDao;
import com.example.springbootmall.dto.BuyItem;
import com.example.springbootmall.dto.CreateOrderRequest;
import com.example.springbootmall.model.OrderItem;
import com.example.springbootmall.model.Product;
import com.example.springbootmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.netty.NettyWebServer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductDao productDao;



    @Transactional
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {
        int totalAmount = 0;

        List<OrderItem> orderItemList = new ArrayList<>();

        for(BuyItem buyItem : createOrderRequest.getBuyItemList()) {

            Product product = productDao.getProductById(buyItem.getProductId());

            //計算總價格
            int amount = buyItem.getQuantity()*product.getPrice();

            totalAmount += amount;


            // 轉換 BuyItem to OrdeItem


            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(buyItem.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);


            orderItemList.add(orderItem);











        }
        //創建訂單
        Integer orderId = orderDao.createOrder(userId,totalAmount);

        orderDao.createOrderItems(orderId,orderItemList);

        return orderId;
    }
}
