package com.smart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.smart.entity.MyOrder;

@Repository
public interface MyOrderRepository extends JpaRepository<MyOrder, Long> {

	@Query ("select o from MyOrder o where o.order_Id= :orderId ")
    MyOrder findByOrderId(String orderId);
    
    // You can also use the following alternative naming convention
    // MyOrder findByOrderId(String orderId);
}
