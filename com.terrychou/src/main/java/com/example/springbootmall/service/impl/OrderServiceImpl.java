package com.example.springbootmall.service.impl;

import com.example.springbootmall.dao.OrderDao;
import com.example.springbootmall.dao.ProductDao;
import com.example.springbootmall.dao.UserDao;
import com.example.springbootmall.dto.BuyItem;
import com.example.springbootmall.dto.CreateOrderRequest;
import com.example.springbootmall.dto.OrderQueryParams;
import com.example.springbootmall.model.Order;
import com.example.springbootmall.model.OrderItem;
import com.example.springbootmall.model.Product;
import com.example.springbootmall.model.User;
import com.example.springbootmall.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.netty.NettyWebServer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private UserDao userDao;


    @Override
    public Integer countOrder(OrderQueryParams orderQueryParams) {
        return  orderDao.countOrder(orderQueryParams);
    }

    @Override
    public List<Order> getOrders(OrderQueryParams orderQueryParams) {
        List<Order> orderList =orderDao.getOrders(orderQueryParams);
        for (Order order : orderList) {
            List<OrderItem> orderItemList =orderDao.getOrderItemsByOrderId(order.getOrderId());

            order.setOrderItemList(orderItemList);
        }
        return  orderList;

    }

    @Transactional
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {

        //檢查 USER 是否存在

        User user =userDao.getUserById(userId);

        if(user==null){
            log.warn("該userId{}不存在",userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        int totalAmount = 0;

        List<OrderItem> orderItemList = new ArrayList<>();

        for(BuyItem buyItem : createOrderRequest.getBuyItemList()) {

            Product product = productDao.getProductById(buyItem.getProductId());

            //檢查product是否存在 庫存是否足夠

            if(product==null){
                log.warn("該商品{}不存在",buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            else if (   product.getStock()<buyItem.getQuantity()) {
                log.warn("該商品庫存不足{},無法購買。庫存剩餘{},欲購買數量{}",
                        buyItem.getProductId(),product.getStock(),buyItem.getQuantity());
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            productDao.updateStock(product.getProduct_id(),product.getStock()- buyItem.getQuantity());



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

    @Override
    public Order getOrderById(Integer orderId) {

        Order  order= orderDao.getOrderById(orderId);

        List<OrderItem> orderItemList =orderDao. getOrderItemsByOrderId(orderId);


        order.setOrderItemList(orderItemList);


        return order;
    }
}
