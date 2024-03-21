package com.project.shopapp.repositories;

import com.project.shopapp.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

//    @Query("SELECT o FROM Order o WHERE o.userId = :userId")
    List<Order> findByUserId(Long userId);

    @Query("SELECT o FROM Order o WHERE o.active = true AND" + "(:keyword IS NULL OR :keyword = '' OR o.fullName LIKE %:keyword% OR" +
            " o.address LIKE %:keyword% OR o.note LIKE %:keyword% OR o.email LIKE %:keyword%)")
    Page<Order> findByKeyWord(String keyword, Pageable pageable);
}
