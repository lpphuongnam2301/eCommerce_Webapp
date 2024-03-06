package com.project.shopapp.repositories;

import com.project.shopapp.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

//    @Query("select * from orders where user_id = ':userId'")
//    List<Order> findByUserId(@Param("userId") Long userId);
}
