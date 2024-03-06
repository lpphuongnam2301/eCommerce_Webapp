package com.project.shopapp.services;

import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.models.OrderDetail;

import java.util.List;

public interface IOrderDetailService {
    OrderDetail createOrderDetail(OrderDetailDTO newOrderDetailDTO) throws Exception;

    OrderDetail getOrderDetail(Long id) throws Exception;

    OrderDetail updateOrderDetail(Long id, OrderDetailDTO newOrderDetailDTO) throws Exception;

    void deleteOrderDetail(Long id);

    List<OrderDetail> findByOrderId(Long orderId);
}
